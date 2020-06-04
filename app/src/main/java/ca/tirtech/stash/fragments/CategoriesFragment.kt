package ca.tirtech.stash.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.tirtech.stash.CollectionModel
import ca.tirtech.stash.R
import ca.tirtech.stash.database.entity.Category
import ca.tirtech.stash.fragments.CategoriesFragment.CategoryAdapter.CategoryViewHolder
import ca.tirtech.stash.util.MarginItemDecorator
import ca.tirtech.stash.util.navigateOnClick
import ca.tirtech.stash.util.observe
import ca.tirtech.stash.util.setVisibility
import com.google.android.material.button.MaterialButton

class CategoriesFragment : Fragment() {
    private lateinit var model: CollectionModel
    private lateinit var navController: NavController
    private lateinit var ghost: ImageView
    private lateinit var recycler: RecyclerView
    private lateinit var backButton: ImageView
    private lateinit var btnAddCategory: MaterialButton

    private val backPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            model.traverseToParentCategory()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backPressedCallback)

        model = ViewModelProvider(requireActivity()).get(CollectionModel::class.java)
        navController = Navigation.findNavController(container!!)
        val adapter = CategoryAdapter()

        val root = inflater.inflate(R.layout.fragment_categories, container, false)
        ghost = root.findViewById(R.id.iv_categories_ghost)
        recycler = root.findViewById<RecyclerView>(R.id.rv_categories).also {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = adapter
            it.addItemDecoration(MarginItemDecorator(5))
        }
        backButton = root.findViewById<ImageView>(R.id.btn_up_category).also {
            it.setOnClickListener {
                model.traverseToParentCategory()
            }
        }
        btnAddCategory = root.findViewById<MaterialButton>(R.id.btn_add_category)
            .navigateOnClick(navController, R.id.action_categoriesFragment_to_newCategoryFragment)

        model.currentCategory.observe(viewLifecycleOwner) {
            recycler.setVisibility(it.subcategories.isNotEmpty())
            ghost.setVisibility(it.subcategories.isEmpty())
        }

        return root
    }

    inner class CategoryAdapter : RecyclerView.Adapter<CategoryViewHolder>() {
        var categories: ArrayList<Category> = ArrayList()

        inner class CategoryViewHolder(var cardView: CardView) : RecyclerView.ViewHolder(cardView) {
            var txtName: TextView = cardView.findViewById(R.id.txt_category_name)
            var btnEdit: ImageView = cardView.findViewById(R.id.iv_edit_category)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder = CategoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.category_rv_card, parent, false) as CardView
        )

        override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
            val (name, _, id) = categories[position]
            holder.txtName.text = name
            holder.cardView.setOnClickListener {
                model.setSelectedCategoryId(id)
            }
            holder.btnEdit.setOnClickListener {
                val b = Bundle()
                b.putInt(NewCategoryFragment.CATEGORY_ID, id)
                navController.navigate(R.id.action_categoriesFragment_to_newCategoryFragment, b)
            }
        }

        override fun getItemCount(): Int = categories.size

        init {
            model.currentCategory.observe (viewLifecycleOwner) {(_, subcategories) ->
                categories.clear()
                categories.addAll(subcategories)
                notifyDataSetChanged()
            }
        }
    }
}
