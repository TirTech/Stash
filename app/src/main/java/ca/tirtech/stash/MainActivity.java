package ca.tirtech.stash;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		NavController navController = ((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_fragment_main)).getNavController();
		BottomNavigationView bottom_nav = findViewById(R.id.bottom_navigation);
		bottom_nav.setOnNavigationItemSelectedListener(
				item -> {
					switch (item.getItemId()) {
						case R.id.bottom_nav_collection:
							navController.navigate(R.id.collectionFragment);
							break;
						case R.id.bottom_nav_projects:
							navController.navigate(R.id.projectFragment);
							break;
						case R.id.bottom_nav_patterns:
							navController.navigate(R.id.patternFragment);
							break;
					}
					return true;
				});
	}
}
