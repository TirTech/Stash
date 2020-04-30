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
import ca.tirtech.stash.database.entity.Item
import ca.tirtech.stash.databinding.FragmentNewItemBinding
import com.google.android.material.snackbar.Snackbar

class NewItemFragment : Fragment() {
    private lateinit var model: CollectionModel
    private lateinit var binding: FragmentNewItemBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        navController = Navigation.findNavController(container!!)
        model = ViewModelProvider(requireActivity()).get(CollectionModel::class.java)
        binding = FragmentNewItemBinding.inflate(inflater, container, false).also {
            it.navController = navController
            it.fragment = this
        }
        return binding.root
    }

    fun handleSaveClicked(view: View?) {
        val title = binding.edittxtItemTitle.text.toString().trim()
        val description = binding.edittxtItemDescription.text.toString().trim()
        if (title.isNotEmpty()) {
            if (description.isNotEmpty()) {
                db.itemDAO().insertItem(Item(model.currentCategory.value!!.category.id, title, description))
                navController.popBackStack()
            } else Snackbar.make(binding.root, R.string.new_item_description_invalid, Snackbar.LENGTH_LONG).show()
        } else Snackbar.make(binding.root, R.string.new_item_title_invalid, Snackbar.LENGTH_LONG).show()
    }
}
