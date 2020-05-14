package ca.tirtech.stash.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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
import ca.tirtech.stash.fragments.ItemsFragment.ItemAdapter.ItemViewHolder
import ca.tirtech.stash.util.MarginItemDecorator
import ca.tirtech.stash.util.navigateOnClick
import ca.tirtech.stash.util.setVisibility
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class ItemsFragment : Fragment() {
    private lateinit var model: CollectionModel
    private lateinit var navController: NavController
    private lateinit var ghost: ImageView
    private lateinit var recycler: RecyclerView
    private lateinit var btnAddItem: MaterialButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        model = ViewModelProvider(requireActivity()).get(CollectionModel::class.java)
        navController = Navigation.findNavController(container!!)
        val adapter = ItemAdapter(model.items)

        val root = inflater.inflate(R.layout.fragment_items, container, false)
        ghost = root.findViewById(R.id.iv_items_ghost)
        recycler = root.findViewById<RecyclerView>(R.id.rv_items).also {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = adapter
            it.addItemDecoration(MarginItemDecorator(5))
        }
        btnAddItem = root.findViewById<MaterialButton>(R.id.btn_add_item)
            .navigateOnClick(navController, R.id.action_itemsFragment_to_newItemFragment)

        model.items.observe(viewLifecycleOwner, Observer {
            recycler.setVisibility(it.isNotEmpty())
            ghost.setVisibility(it.isEmpty())
        })

        return root
    }

    inner class ItemAdapter(items: LiveData<List<Item>?>) : RecyclerView.Adapter<ItemViewHolder>() {
        val items: LiveData<List<Item>?>

        inner class ItemViewHolder(val root: MaterialCardView) : RecyclerView.ViewHolder(root) {
            val title: TextView = root.findViewById(R.id.txt_item_title)
            val description: TextView = root.findViewById(R.id.txt_item_description)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder = ItemViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_rv_card, parent, false) as MaterialCardView
        )

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            val data = items.value!![position]
            holder.apply {
                title.text = data.title
                description.text = data.description
                root.setOnClickListener {
                    val b = Bundle()
                    b.putInt(ItemDetailFragment.ITEM_ID, data.id)
                    navController.navigate(R.id.action_itemsFragment_to_itemDetailFragment, b)
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
