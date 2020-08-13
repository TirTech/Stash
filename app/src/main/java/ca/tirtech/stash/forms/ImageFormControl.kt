package ca.tirtech.stash.forms

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import ca.tirtech.stash.R
import ca.tirtech.stash.database.entity.ItemPhoto
import ca.tirtech.stash.util.firsts
import ca.tirtech.stash.util.loadFromFile
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ImageFormControl(val field: FormField, context: Context) : FormControl(field, context) {

    private val photoContainer: LinearLayout
    private var photos: ArrayList<Pair<ItemPhoto, ImageView>> = ArrayList()

    init {
        val root = View.inflate(context, R.layout.custom_image_form_control, this)
        photoContainer = root.findViewById(R.id.ll_photo_container)
        root.findViewById<MaterialButton>(R.id.btn_new_item_add_photo).also {
            setOnClickListener(this::handlePhotoAddClicked)
        }
    }

    private fun addPhotoToView(file: String) = addPhotoToView(ItemPhoto(file))

    private fun addPhotoToView(ip: ItemPhoto) {
        val iv = ImageView(context)
        val pair = ip to iv
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
        val overlay = resources.getDrawable(R.drawable.image, context.theme).apply {
            setTint(resources.getColor(R.color.primaryDarkColor, context.theme))
        }
        val overlaySize = photoContainer.width / 6
        overlay.setBounds(0, 0, overlaySize, overlaySize)
        iv.overlay.add(overlay)
    }

    private fun handlePhotoAddClicked(view: View) {
        val filename = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(Date())
        val photoFile = File.createTempFile(filename, ".jpg", context.getExternalFilesDir("item_pictures"))
        val photoContentURI = FileProvider.getUriForFile(context, context.applicationContext.packageName, photoFile)
        try {
            val frag = findFragment<Fragment>()
            val activityResLauncher = frag.registerForActivityResult(ActivityResultContracts.TakePicture()) {
                if (it) {
                    Snackbar.make(view, "Picture Taken: ${photoFile.absolutePath}", Snackbar.LENGTH_SHORT).show()
                    addPhotoToView(photoFile.absolutePath)
                } else {
                    Snackbar.make(view, "Picture Failed", Snackbar.LENGTH_SHORT).show()
                }
            }

            activityResLauncher.launch(photoContentURI)
        } catch (ex: IllegalStateException) {
            Log.e("ImageFormControl","No parent fragment was found when taking a picture. Picture was not saved")
        }
    }

    override fun getValue(): Any {
        return photos.firsts()
    }

    override fun onDefaultsReady(value: Any?) {
        if (value != null) {
            (value as Collection<ItemPhoto>).forEach {
                addPhotoToView(it)
            }
        }
    }

    override fun onFormCancel() {
        photos.forEach { if (it.first.id == null) File(it.first.fileName).delete() }
    }
}
