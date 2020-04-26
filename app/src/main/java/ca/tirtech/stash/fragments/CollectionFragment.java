package ca.tirtech.stash.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import ca.tirtech.stash.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class CollectionFragment extends Fragment {
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		ConstraintLayout root = (ConstraintLayout) inflater.inflate(R.layout.fragment_collection, container, false);
		TabLayout tabs = root.findViewById(R.id.tabs);
		TabLayout.Tab tab_categories = tabs.newTab().setText(R.string.tab_text_category);
		TabLayout.Tab tab_items = tabs.newTab().setText(R.string.tab_text_items);
		tabs.addTab(tab_categories);
		tabs.addTab(tab_items);
		NavController navController = ((NavHostFragment) getChildFragmentManager().findFragmentById(R.id.nav_fragment_collection)).getNavController();
		navController.addOnDestinationChangedListener((NavController controller, NavDestination destination, Bundle arguments) -> {
			if (destination.getId() == R.id.categoriesFragment) {
				tabs.selectTab(tab_categories);
			} else if (destination.getId() == R.id.itemsFragment) {
				tabs.selectTab(tab_items);
			}
		});
		
		tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				if (tab == tab_categories) {
					navController.navigate(R.id.categoriesFragment);
				} else if (tab == tab_items) {
					navController.navigate(R.id.itemsFragment);
				}
			}
			
			@Override
			public void onTabUnselected(TabLayout.Tab tab) {}
			
			@Override
			public void onTabReselected(TabLayout.Tab tab) {}
		});
		return root;
	}
}
