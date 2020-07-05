package ca.tirtech.stash.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import ca.tirtech.stash.CollectionModel
import ca.tirtech.stash.R
import ca.tirtech.stash.databinding.FragmentCollectionBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

class CollectionFragment : Fragment() {
    private lateinit var binding: FragmentCollectionBinding
    private lateinit var tabCategories: TabLayout.Tab
    private lateinit var tabItems: TabLayout.Tab
    private lateinit var navController: NavController
    private lateinit var model: CollectionModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        model = ViewModelProvider(requireActivity()).get(CollectionModel::class.java)
        model.currentCategory.observe(viewLifecycleOwner, Observer {
            binding.collectionBreadcrumbs.setCrumbs(model.categoryStack.map { it.category.name })
        })
        binding = FragmentCollectionBinding.inflate(inflater, container, false)
        binding.tabs.apply {
            addTab(newTab().setText(R.string.tab_text_category).also { tabCategories = it })
            addTab(newTab().setText(R.string.tab_text_items).also { tabItems = it })
            addOnTabSelectedListener(object : OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    when (tab) {
                        tabCategories -> if (navController.currentDestination?.id != R.id.categoriesFragment) navController.navigate(R.id.action_itemsFragment_to_categoriesFragment)
                        tabItems -> if (navController.currentDestination?.id != R.id.itemsFragment) navController.navigate(R.id.action_categoriesFragment_to_itemsFragment)
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {}
                override fun onTabReselected(tab: TabLayout.Tab) {}
            })
        }
        navController = (childFragmentManager.findFragmentById(R.id.nav_fragment_collection) as NavHostFragment).navController.also {
            it.addOnDestinationChangedListener { _: NavController?, destination: NavDestination, _: Bundle? ->
                when (destination.id) {
                    R.id.categoriesFragment -> binding.apply {
                        tabs.selectTab(tabCategories)
                        showTabs = true
                    }
                    R.id.itemsFragment -> binding.apply {
                        tabs.selectTab(tabItems)
                        showTabs = true
                    }
                    R.id.newCategoryFragment -> binding.showTabs = false
                    R.id.newItemFragment -> binding.showTabs = false
                    R.id.itemDetailFragment -> binding.showTabs = false
                    else -> Unit
                }
                binding.invalidateAll()
            }
        }
        binding.collectionBreadcrumbs.setCrumbs(arrayListOf("Test Top"))
        return binding.root
    }
}
