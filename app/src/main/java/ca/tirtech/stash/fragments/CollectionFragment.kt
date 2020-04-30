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
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

class CollectionFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_collection, container, false) as ConstraintLayout
        val tabs = (root.findViewById(R.id.tabs) as TabLayout)
        val tabCategories = tabs.newTab().setText(R.string.tab_text_category).also { tabs.addTab(it) }
        val tabItems = tabs.newTab().setText(R.string.tab_text_items).also { tabs.addTab(it) }
        val navController = (childFragmentManager.findFragmentById(R.id.nav_fragment_collection) as NavHostFragment)
            .navController.also {
                it.addOnDestinationChangedListener { _: NavController?, destination: NavDestination, _: Bundle? ->
                    when (destination.id) {
                        R.id.categoriesFragment -> tabs.selectTab(tabCategories)
                        R.id.itemsFragment -> tabs.selectTab(tabItems)
                        else -> Unit
                    }
                }
            }
        tabs.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) =
                when (tab) {
                    tabCategories -> navController.navigate(R.id.categoriesFragment)
                    tabItems -> navController.navigate(R.id.itemsFragment)
                    else -> Unit
                }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        return root
    }
}
