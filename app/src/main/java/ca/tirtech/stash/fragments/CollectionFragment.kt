package ca.tirtech.stash.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import ca.tirtech.stash.R
import ca.tirtech.stash.databinding.FragmentCollectionBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

class CollectionFragment : Fragment() {
    private lateinit var binding: FragmentCollectionBinding
    private lateinit var tabCategories: TabLayout.Tab
    private lateinit var tabItems: TabLayout.Tab
    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCollectionBinding.inflate(inflater,container,false)
        binding.tabs.apply {
            addTab(newTab().setText(R.string.tab_text_category).also { tabCategories = it })
            addTab(newTab().setText(R.string.tab_text_items).also { tabItems = it })
            addOnTabSelectedListener(object : OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) = when (tab) {
                    tabCategories -> navController.navigate(R.id.action_itemsFragment_to_categoriesFragment)
                    tabItems -> navController.navigate(R.id.action_categoriesFragment_to_itemsFragment)
                    else -> Unit
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
                    else -> Unit
                }
                binding.invalidateAll()
            }
        }
        return binding.root
    }
}
