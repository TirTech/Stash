package ca.tirtech.stash.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.tirtech.stash.CollectionModel
import ca.tirtech.stash.R
import ca.tirtech.stash.components.ChipList
import ca.tirtech.stash.database.AppDatabase.Companion.db
import ca.tirtech.stash.database.entity.FieldValueWithConfig
import ca.tirtech.stash.database.entity.Item
import ca.tirtech.stash.database.entity.ItemPhoto
import ca.tirtech.stash.database.entity.ItemWithFieldValuesAndConfigs
import ca.tirtech.stash.fragments.ItemsFragment.ItemAdapter.ItemViewHolder
import ca.tirtech.stash.util.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.launch

class ItemsFragment : Fragment() {
    private lateinit var model: CollectionModel
    private lateinit var navController: NavController
    private lateinit var ghost: ImageView
    private lateinit var recycler: RecyclerView
    private lateinit var btnAddItem: MaterialButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        model = ViewModelProvider(requireActivity()).get(CollectionModel::class.java)
        navController = Navigation.findNavController(container!!)
        val adapter = ItemAdapter()
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

    inner class ItemAdapter : RecyclerView.Adapter<ItemViewHolder>() {
        private val items: ArrayList<Pair<ItemWithFieldValuesAndConfigs, ItemPhoto?>> = ArrayList()

        inner class ItemViewHolder(val root: MaterialCardView) : RecyclerView.ViewHolder(root) {
            val title: TextView = root.findViewById(R.id.txt_item_title)
            val description: TextView = root.findViewById(R.id.txt_item_description)
            val image: ImageView = root.findViewById(R.id.img_item_photo)
            val chpgrpLabels: ChipList<FieldValueWithConfig> = root.findViewById<ChipList<FieldValueWithConfig>>(R.id.chpgrp_item_labels).apply {
                chipBuilder = { fv, c ->
                    c.text = "${fv.fieldConfig.name}: ${fv.fieldValue.value}"
                    c.closeIcon = null
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder = ItemViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_rv_card, parent, false) as MaterialCardView
        )

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            val data = items[position]
            holder.apply {
                title.text = data.first.item.title
                description.text = data.first.item.description
                chpgrpLabels.addAllItems(data.first.fieldValues.filter { it.fieldConfig.showAsLabel })
                root.setOnClickListener {
                    val b = Bundle()
                    b.putInt(ItemDetailFragment.ITEM_ID, data.first.item.id)
                    navController.navigate(R.id.action_itemsFragment_to_itemDetailFragment, b)
                }
                data.second?.apply { image.loadFromFile(fileName) }
            }
        }

        override fun getItemCount(): Int = items.size

        init {
            model.items.observe(viewLifecycleOwner) { catItems ->
                refreshItems(catItems)
            }
        }

        private fun refreshItems(catItems: List<Item>) = lifecycleScope.launch {
            items.clear()
            items.addAll(
                catItems
                    .map {
                        db.itemDAO().getItemWithFieldValuesAndConfigs(it.id) to db.itemPhotoDAO().getItemCoverPhotoByItemId(it.id)
                    }
                    .filter { it.first != null }
                        as List<Pair<ItemWithFieldValuesAndConfigs, ItemPhoto?>>
            )
        }.invokeOnCompletion {
            notifyDataSetChanged()
        }
    }
}
