package ca.tirtech.stash

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import ca.tirtech.stash.database.AppDatabase.Companion.dbinit
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbinit(this)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        val navController = (supportFragmentManager.findFragmentById(R.id.nav_fragment_main) as NavHostFragment).navController
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.bottom_nav_collection -> navController.navigate(R.id.action_global_collectionFragment)
                R.id.bottom_nav_projects -> navController.navigate(R.id.action_global_projectFragment)
                R.id.bottom_nav_patterns -> navController.navigate(R.id.action_global_patternFragment)
            }
            true
        }
    }
}
