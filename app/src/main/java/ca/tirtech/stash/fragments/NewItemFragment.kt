package ca.tirtech.stash.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import ca.tirtech.stash.CollectionModel
import ca.tirtech.stash.R
import ca.tirtech.stash.components.AsyncOp
import ca.tirtech.stash.database.AppDatabase.Companion.db
import ca.tirtech.stash.database.entity.*
import ca.tirtech.stash.database.repositories.Repository
import ca.tirtech.stash.database.types.FieldType
import ca.tirtech.stash.forms.*
import ca.tirtech.stash.util.fromJsonString
import ca.tirtech.stash.util.toJsonString
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*

class NewItemFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val navController: NavController = Navigation.findNavController(container!!)
        val model: CollectionModel = ViewModelProvider(requireActivity()).get(CollectionModel::class.java)
        val editId: Int? = arguments?.getInt(ITEM_ID)

        val itemWithVals = AsyncOp {
            editId?.let {
                db.itemDAO().getItemWithFieldValuesAndConfigs(it)
            }
        }

        val fieldsForm = Subform(R.string.new_item_field_header, requireContext())
        val form = Form(
            R.string.add_item,
            requireContext(),
            onCancel = { navController.popBackStack() },
            onSubmitComplete = {
                val res = it as ItemFormResult
                when {
                    res.item.description.isEmpty() -> Snackbar.make(requireView(), R.string.new_item_description_invalid, Snackbar.LENGTH_LONG)
                        .show()
                    res.item.title.isEmpty() -> Snackbar.make(requireView(), R.string.new_item_title_invalid, Snackbar.LENGTH_LONG).show()
                    else -> {
                        lifecycleScope.launch {
                            if (editId == null) Repository.createItemWithFieldsAndPhotos(res.item, res.fieldVals, res.photos)
                            else Repository.updateItemWithFieldsAndPhotos(res.item, res.fieldVals, res.photos)
                            model.refreshCategory()
                            navController.popBackStack()
                        }
                    }
                }
            },
            onSubmit = { ItemFormResult(itemWithVals.getOrNull()?.item) }
        )
            .addField(
                R.string.item_title,
                FormControlRegistry.Control.STRING,
                default = { itemWithVals.getValue()?.item?.title },
                onSubmit = { r, a -> (a as ItemFormResult).item.title = (r as String) }
            )
            .addField(
                R.string.item_description,
                FormControlRegistry.Control.STRING,
                default = { itemWithVals.getValue()?.item?.description },
                onSubmit = { r, a -> (a as ItemFormResult).item.description = (r as String) }
            )
            .addForm(fieldsForm)
            .addForm(
                Subform(R.string.new_item_photos_header, requireContext())
                    .addField(
                        R.string.new_item_photos_header,
                        FormControlRegistry.Control.PHOTO,
                        default = { editId?.let { db.itemPhotoDAO().getItemPhotoByItemId(it) } },
                        onSubmit = { r, a -> (a as ItemFormResult).photos.addAll(r as List<ItemPhoto>) }
                    )
            )
            .build()

        itemWithVals.onComplete { iwv ->
            if (iwv == null) Repository.getCategoryFieldConfigsForItem(model.currentCategory.value!!.category.id)
                .forEach { addField(fieldsForm, it) }
            else iwv.fieldValues.forEach { fv -> addField(fieldsForm, fv.fieldConfig, fv.fieldValue) }
            withContext(Dispatchers.Main) {
                form.build()
            }
        }
        return form.root
    }

    private fun addField(form: Subform, config: FieldConfig, fieldValue: FieldValue? = null) {
        when (config.type) {
            FieldType.STRING -> form.addField(
                config.name,
                FormControlRegistry.Control.STRING,
                default = { fieldValue?.value },
                onSubmit = { r, a -> (a as ItemFormResult).setFieldVal(config.id, fieldValue, r as String) })
            FieldType.NUMBER -> form.addField(
                config.name,
                FormControlRegistry.Control.INT,
                default = { fieldValue?.value },
                onSubmit = { r, a -> (a as ItemFormResult).setFieldVal(config.id, fieldValue, (r as Int).toString()) })
            FieldType.BOOLEAN -> form.addField(
                config.name,
                FormControlRegistry.Control.BOOLEAN,
                default = { fieldValue?.value },
                onSubmit = { r, a -> (a as ItemFormResult).setFieldVal(config.id, fieldValue, (r as Boolean).toString()) })
            FieldType.SINGLE_CHOICE -> form.addField(
                config.name,
                FormControlRegistry.Control.SINGLE_CHOICE,
                configs = mapOf("options" to config.choices),
                default = { fieldValue?.value }, onSubmit = { r, a -> (a as ItemFormResult).setFieldVal(config.id, fieldValue, (r as String)) })
            FieldType.MULTI_CHOICE -> form.addField(
                config.name,
                FormControlRegistry.Control.MULTI_CHOICE,
                configs = mapOf("options" to config.choices),
                default = {
                    fieldValue?.value?.let {
                        ArrayList<String>().fromJsonString(it)
                    }
                },
                onSubmit = { r, a -> (a as ItemFormResult).setFieldVal(config.id, fieldValue, (r as ArrayList<String>).toJsonString()) }
            )
        }
    }

    companion object {
        const val ITEM_ID = "ITEM_ID"
    }

    private class ItemFormResult(i: Item?) {
        var item: Item = i ?: Item(0, "", "")
        var fieldVals = ArrayList<FieldValue>()
        var photos = ArrayList<ItemPhoto>()

        fun setFieldVal(c: Int, f: FieldValue?, v: String) {
            fieldVals.add(f?.also { it.value = v } ?: FieldValue(c, v))
        }
    }
}
