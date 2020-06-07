package ca.tirtech.stash.fragments

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import ca.tirtech.stash.R
import ca.tirtech.stash.components.ChipList
import ca.tirtech.stash.database.AppDatabase.Companion.db
import ca.tirtech.stash.database.entity.FieldValueWithConfig
import ca.tirtech.stash.database.entity.ItemPhoto
import ca.tirtech.stash.database.entity.ItemWithFieldValuesAndConfigs
import ca.tirtech.stash.database.types.FieldType
import ca.tirtech.stash.util.fromJsonString
import ca.tirtech.stash.util.loadFromFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItemDetailFragment : Fragment() {
    private lateinit var item: ItemWithFieldValuesAndConfigs
    private lateinit var tvTitle: TextView
    private lateinit var tvDesc: TextView
    private lateinit var detailContainer: LinearLayout
    private lateinit var btnEdit: ImageView
    private lateinit var navController: NavController
    private lateinit var photoContainer: LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_item_details, container, false)
        val itemId = requireArguments().getInt(ITEM_ID)
        detailContainer = root.findViewById(R.id.ll_item_detail_fields)
        navController = Navigation.findNavController(container!!)
        tvTitle = root.findViewById(R.id.tv_item_detail_title)
        tvDesc = root.findViewById(R.id.tv_item_detail_description)
        btnEdit = root.findViewById<ImageView>(R.id.iv_edit_item).also {
            it.setOnClickListener {
                val b = Bundle()
                b.putInt(NewItemFragment.ITEM_ID, item.item.id)
                navController.navigate(R.id.action_itemDetailFragment_to_newItemFragment, b)
            }
        }
        photoContainer = root.findViewById(R.id.ll_item_detail_photo_container)

        CoroutineScope(Dispatchers.Main).launch {
            db.itemDAO().getItemWithFieldValuesAndConfigs(itemId)?.also {
                this@ItemDetailFragment.item = it
                this@ItemDetailFragment.setupDetails()
            }
            db.itemPhotoDAO().getItemPhotoByItemId(itemId).forEach {
                addPhotoToView(it)
            }
        }
        return root
    }

    private fun addPhotoToView(ip: ItemPhoto) {
        val iv = ImageView(requireContext())
        iv.loadFromFile(ip.fileName)
        iv.setPadding(0, 0, 0, resources.getDimensionPixelSize(R.dimen.item_spacing))
        photoContainer.addView(iv, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        if (ip.isCoverImage) addOverlay(iv)
    }

    private fun addOverlay(iv: ImageView) {
        val overlay = resources.getDrawable(R.drawable.image, requireContext().theme).apply {
            setTint(resources.getColor(R.color.primaryDarkColor, requireContext().theme))
        }
        val overlaySize = photoContainer.width / 6
        overlay.setBounds(0, 0, overlaySize, overlaySize)
        iv.overlay.add(overlay)
    }

    private fun setupDetails() {
        tvTitle.text = item.item.title
        tvDesc.text = item.item.description
        detailContainer.removeAllViews()
        if (item.fieldValues.isEmpty())
            detailContainer.addView(
                TextView(context).also {
                    it.setTextAppearance(R.style.TextAppearance_MaterialComponents_Body1)
                    it.setText(R.string.no_fields)
                },
                LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            )
        else
            item.fieldValues.forEach {
                makeKVViewPair(it)
            }
    }

    private fun makeKVViewPair(item: FieldValueWithConfig) {
        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
        }

        layout.addView(
            TextView(context).also {
                it.setTextAppearance(R.style.TextAppearance_MaterialComponents_Body1)
                it.text = item.fieldConfig.name + ":"
            },
            LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f)
        )

        layout.addView(
            when (item.fieldConfig.type) {
                FieldType.MULTI_CHOICE -> {
                    ChipList<String>(context).also {
                        it.setItems(ArrayList<String>().fromJsonString(item.fieldValue.value))
                    }
                }
                else -> {
                    TextView(context).also {
                        it.setTextAppearance(R.style.TextAppearance_MaterialComponents_Body2)
                        it.text = item.fieldValue.value
                    }
                }
            },
            LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        )
        detailContainer.addView(
            layout,
            LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        )

        layout.setPadding(0, 0, 0, resources.getDimensionPixelOffset(R.dimen.item_spacing))
    }

    companion object {
        const val ITEM_ID = "ITEM_ID"
    }
}
