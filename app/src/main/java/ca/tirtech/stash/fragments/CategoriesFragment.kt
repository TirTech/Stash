package ca.tirtech.stash.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.tirtech.stash.CollectionModel
import ca.tirtech.stash.R
import ca.tirtech.stash.database.entity.Category
import ca.tirtech.stash.database.entity.CategoryWithSubcategory
import ca.tirtech.stash.databinding.FragmentCategoriesBinding
import ca.tirtech.stash.fragments.CategoriesFragment.CategoryAdapter.CategoryViewHolder
import ca.tirtech.stash.util.MarginItemDecorator

class CategoriesFragment : Fragment() {
    private lateinit var model: CollectionModel
    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var navController: NavController

    private val backPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            model.traverseToParentCategory()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backPressedCallback)

        model = ViewModelProvider(requireActivity()).get(CollectionModel::class.java)
        navController = Navigation.findNavController(container!!)
        val adapter = CategoryAdapter(model.currentCategory)

        binding = FragmentCategoriesBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = this
            it.rvCategoryAdapter = adapter
            it.navController = navController
            it.model = model
            it.rvCategories.also { rv ->
                rv.layoutManager = LinearLayoutManager(context)
                rv.adapter = adapter
                rv.addItemDecoration(MarginItemDecorator(5))
            }
        }

        return binding.root
    }

    inner class CategoryAdapter(currentCategory: LiveData<CategoryWithSubcategory>) : RecyclerView.Adapter<CategoryViewHolder>() {
        val categories: LiveData<List<Category>?>

        inner class CategoryViewHolder(var cardView: CardView) : RecyclerView.ViewHolder(cardView) {
            var txtName: TextView = cardView.findViewById(R.id.txt_category_name)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder = CategoryViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.category_rv_card, parent, false) as CardView)

        override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
            val (name, _, id) = categories.value!![position]
            holder.txtName.text = name
            holder.cardView.setOnClickListener {
                model.setSelectedCategoryId(id)
            }
        }

        override fun getItemCount(): Int = if (categories.value == null) 0 else categories.value!!.size

        init {
            categories = Transformations.map(currentCategory) { (_, subcategories) -> subcategories }.apply {
                observe(viewLifecycleOwner, Observer { notifyDataSetChanged() })
            }
        }
    }
}
