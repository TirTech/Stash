package ca.tirtech.stash.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import ca.tirtech.stash.CollectionModel
import ca.tirtech.stash.R
import ca.tirtech.stash.components.FieldConfigEditor
import ca.tirtech.stash.database.AppDatabase.Companion.db
import ca.tirtech.stash.database.entity.Category
import ca.tirtech.stash.database.repositories.Repository
import ca.tirtech.stash.util.value
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class NewCategoryFragment : Fragment() {
    private lateinit var btnCancel: MaterialButton
    private lateinit var btnSave: MaterialButton
    private lateinit var categoryName: TextInputEditText
    private lateinit var fieldConfigEditor: FieldConfigEditor
    private lateinit var model: CollectionModel
    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        model = ViewModelProvider(requireActivity()).get(CollectionModel::class.java)
        navController = Navigation.findNavController(container!!)
        val root = inflater.inflate(R.layout.fragment_new_category, container, false)
        btnCancel = root.findViewById<MaterialButton>(R.id.btn_cancel_new_category).apply {
            setOnClickListener { navController.popBackStack() }
        }
        btnSave = root.findViewById<MaterialButton>(R.id.btn_apply_new_category).apply {
            setOnClickListener(this@NewCategoryFragment::handleSaveClicked)
        }
        categoryName = root.findViewById(R.id.edittxt_category_name)
        fieldConfigEditor = root.findViewById<FieldConfigEditor>(R.id.fieldConfigEditor)
        return root
    }

    fun handleSaveClicked(view: View) {
        val name = categoryName.value()
        if (name.isNotEmpty()) {
            Repository.createCategoryWithFields(
                Category(name, model.currentCategory.value!!.category.id),
                fieldConfigEditor.getConfigs()
            )
            navController.popBackStack()
        } else {
            Snackbar.make(view, R.string.new_category_name_invalid, Snackbar.LENGTH_LONG).show()
        }
    }
}
