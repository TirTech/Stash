package ca.tirtech.stash.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import ca.tirtech.stash.CollectionModel
import ca.tirtech.stash.R
import ca.tirtech.stash.components.FieldEntry
import ca.tirtech.stash.database.AppDatabase.Companion.db
import ca.tirtech.stash.database.entity.*
import ca.tirtech.stash.database.repositories.Repository
import ca.tirtech.stash.util.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NewItemFragment : Fragment() {
    private lateinit var btnCancel: MaterialButton
    private lateinit var btnSave: MaterialButton
    private lateinit var model: CollectionModel
    private lateinit var navController: NavController
    private lateinit var txtItemTitle: TextInputEditText
    private lateinit var txtItemDescription: TextInputEditText
    private var fieldEntries: ArrayList<FieldEntry> = ArrayList()
    private lateinit var entryContainer: ViewGroup
    private var editingItem: ItemWithFieldValuesAndConfigs? = null
    private lateinit var btnAddImage: MaterialButton
    private lateinit var photoContainer: LinearLayout
    private var photos: ArrayList<Pair<ItemPhoto, ImageView>> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        navController = Navigation.findNavController(container!!)
        model = ViewModelProvider(requireActivity()).get(CollectionModel::class.java)
        val root = inflater.inflate(R.layout.fragment_new_item, container, false)
        txtItemTitle = root.findViewById<TextInputEditText>(R.id.edittxt_item_title).apply {
            autoHideKeyboard()
        }
        txtItemDescription = root.findViewById<TextInputEditText>(R.id.edittxt_item_description).apply {
            autoHideKeyboard()
        }
        btnCancel = root.findViewById<MaterialButton>(R.id.btn_cancel_new_item).navigateOnClick(navController) {
            photos.forEach { if (it.first.id == null) File(it.first.fileName).delete() }
        }
        btnSave = root.findViewById<MaterialButton>(R.id.btn_apply_new_item).apply {
            setOnClickListener(this@NewItemFragment::handleSaveClicked)
        }
        entryContainer = root.findViewById(R.id.editor_container)
        btnAddImage = root.findViewById<MaterialButton>(R.id.btn_new_item_add_photo).apply {
            setOnClickListener(this@NewItemFragment::handlePhotoAddClicked)
        }
        photoContainer = root.findViewById(R.id.gl_new_item_photo_container)

        val editId = arguments?.getInt(ITEM_ID)

        CoroutineScope(Dispatchers.Main).launch {
            if (editId == null) {
                Repository.getCategoryFieldConfigsForItem(model.currentCategory.value!!.category.id).forEach { addEditor(it) }
            } else {
                db.itemDAO().getItemWithFieldValuesAndConfigs(editId)?.also {
                    this@NewItemFragment.editingItem = it
                    txtItemTitle.setText(it.item.title)
                    txtItemDescription.setText(it.item.description)
                    it.fieldValues.forEach { fv -> addEditor(fv.fieldConfig, fv.fieldValue) }
                }
                db.itemPhotoDAO().getItemPhotoByItemId(editId).forEach {
                    addPhotoToView(it)
                }
            }
        }
        return root
    }

    private fun addPhotoToView(file: String) = addPhotoToView(ItemPhoto(file))

    private fun addPhotoToView(ip: ItemPhoto) {
        val iv = ImageView(requireContext())
        val pair = Pair(ip, iv)
        iv.loadFromFile(ip.fileName)
        iv.setPadding(0, 0, 0, resources.getDimensionPixelSize(R.dimen.item_spacing))
        iv.setOnClickListener {
            setImageSelected(pair)
        }
        photoContainer.addView(iv, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        photos.add(pair)
        if (ip.isCoverImage) addOverlay(iv)
    }

    private fun setImageSelected(imgPair: Pair<ItemPhoto, ImageView>) {
        val (ip, iv) = imgPair
        if (ip.isCoverImage) {
            ip.isCoverImage = false
            iv.overlay.clear()
        } else {
            // Clear old overlay
            photos
                .filter { it.first.isCoverImage }
                .forEach {
                    it.first.isCoverImage = false
                    it.second.overlay.clear()
                }

            // Set new overlay
            addOverlay(iv)
            ip.isCoverImage = true
        }
    }

    private fun addOverlay(iv: ImageView) {
        val overlay = resources.getDrawable(R.drawable.image, requireContext().theme).apply {
            setTint(resources.getColor(R.color.primaryDarkColor, requireContext().theme))
        }
        val overlaySize = photoContainer.width / 6
        overlay.setBounds(0, 0, overlaySize, overlaySize)
        iv.overlay.add(overlay)
    }

    private fun handlePhotoAddClicked(view: View) {
        val filename = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(Date())
        val photoFile = File.createTempFile(filename, ".jpg", requireContext().getExternalFilesDir("item_pictures"))
        val photoContentURI = FileProvider.getUriForFile(requireContext(), requireContext().applicationContext.packageName, photoFile)
        val activityResLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) {
                Snackbar.make(view, "Picture Taken: ${photoFile.absolutePath}", Snackbar.LENGTH_SHORT).show()
                addPhotoToView(photoFile.absolutePath)
            } else {
                Snackbar.make(view, "Picture Failed", Snackbar.LENGTH_SHORT).show()
            }
        }

        activityResLauncher.launch(photoContentURI)
    }

    private fun handleSaveClicked(view: View) {
        val title = txtItemTitle.value()
        val description = txtItemDescription.value()
        if (title.isNotEmpty()) {
            if (description.isNotEmpty()) {
                if (editingItem == null) {
                    Repository.createItemWithFieldsAndPhotos(
                        Item(model.currentCategory.value!!.category.id, title, description),
                        fieldEntries.map { it.getValue() },
                        photos.firsts()
                    ).invokeOnCompletion {
                        model.refreshCategory()
                    }
                } else {
                    editingItem?.also { ei ->
                        ei.item.title = title
                        ei.item.description = description
                        Repository.updateItemWithFieldsAndPhotos(
                            ei.item,
                            fieldEntries.map { it.getValue() },
                            photos.firsts()
                        ).invokeOnCompletion {
                            model.refreshCategory()
                        }
                    }
                }
                navController.popBackStack()
            } else Snackbar.make(view, R.string.new_item_description_invalid, Snackbar.LENGTH_LONG).show()
        } else Snackbar.make(view, R.string.new_item_title_invalid, Snackbar.LENGTH_LONG).show()
    }

    private fun addEditor(config: FieldConfig, fieldValue: FieldValue? = null) {
        val entry = FieldEntry(context!!, null)
        entryContainer.addView(entry, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        entry.setPadding(0, 0, 0, resources.getDimensionPixelSize(R.dimen.item_spacing))
        entry.setFieldConfig(config, fieldValue)
        fieldEntries.add(entry)
    }

    companion object {
        const val ITEM_ID = "ITEM_ID"
    }
}
