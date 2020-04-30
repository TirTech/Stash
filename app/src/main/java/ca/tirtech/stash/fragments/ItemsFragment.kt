package ca.tirtech.stash.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.SurfaceControl
import android.view.View
import android.view.ViewGroup
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
import ca.tirtech.stash.database.entity.Item
import ca.tirtech.stash.databinding.FragmentItemsBinding
import ca.tirtech.stash.databinding.ItemRvCardBinding
import ca.tirtech.stash.fragments.ItemsFragment.ItemAdapter.ItemViewHolder
import ca.tirtech.stash.util.MarginItemDecorator
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_items.*

class ItemsFragment : Fragment() {
    private lateinit var model: CollectionModel
    private lateinit var binding: FragmentItemsBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        model = ViewModelProvider(requireActivity()).get(CollectionModel::class.java)
        navController = Navigation.findNavController(container!!)
        val adapter = ItemAdapter(model.items)

        binding = FragmentItemsBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = this
            it.rvItemAdapter = adapter
            it.navController = navController
            it.model = model
            it.rvItems.also { rv ->
                rv.layoutManager = LinearLayoutManager(context)
                rv.adapter = adapter
                rv.addItemDecoration(MarginItemDecorator(5))
            }
        }

        return binding.root
    }

    inner class ItemAdapter(items: LiveData<List<Item>?>) :
        RecyclerView.Adapter<ItemViewHolder>() {
        val items: LiveData<List<Item>?>

        inner class ItemViewHolder(var binding: ItemRvCardBinding) :
            RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
            ItemViewHolder(ItemRvCardBinding.inflate(LayoutInflater.from(context), parent, false))

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            val data = items.value!![position]
            holder.binding.apply {
                txtItemTitle.text = data.title
                txtItemDescription.text = data.description
                root.setOnClickListener {
                    Snackbar.make(root, R.string.not_implemented, Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        override fun getItemCount(): Int = if (items.value == null) 0 else items.value!!.size

        init {
            this.items = Transformations.map(items) { itemList: List<Item>? -> itemList }.apply {
                observe(viewLifecycleOwner, Observer { notifyDataSetChanged() })
            }
        }
    }
}
