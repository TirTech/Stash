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
import ca.tirtech.stash.database.AppDatabase.Companion.db
import ca.tirtech.stash.database.entity.Category
import ca.tirtech.stash.databinding.FragmentNewCategoryBinding
import com.google.android.material.snackbar.Snackbar

class NewCategoryFragment : Fragment() {
    private lateinit var binding: FragmentNewCategoryBinding
    private lateinit var model: CollectionModel
    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        model = ViewModelProvider(requireActivity()).get(CollectionModel::class.java)
        navController = Navigation.findNavController(container!!)
        binding = FragmentNewCategoryBinding.inflate(inflater, container, false)
        binding.navController = navController
        binding.fragment = this
        return binding.root
    }

    fun handleSaveClicked(view: View?) {
        val name = binding.edittxtCategoryName.text.toString().trim()
        if (name.isNotEmpty()) {
            db.categoryDAO().insertCategory(Category(name, model.currentCategory.value!!.category.id))
            navController.popBackStack()
        } else {
            Snackbar.make(binding.root, R.string.new_category_name_invalid, Snackbar.LENGTH_LONG).show()
        }
    }
}
