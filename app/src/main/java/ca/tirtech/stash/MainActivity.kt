package ca.tirtech.stash

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import ca.tirtech.stash.database.AppDatabase.Companion.dbinit
import ca.tirtech.stash.forms.FormControlRegistry
import ca.tirtech.stash.forms.ImageFormControl
import ca.tirtech.stash.forms.MultiFormControl
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbinit(this)
        registerFormControls()
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

    private fun registerFormControls() {
        FormControlRegistry.registerControl(FormControlRegistry.Control.STRING, MultiFormControl::class)
        FormControlRegistry.registerControl(FormControlRegistry.Control.INT, MultiFormControl::class)
        FormControlRegistry.registerControl(FormControlRegistry.Control.BOOLEAN, MultiFormControl::class)
        FormControlRegistry.registerControl(FormControlRegistry.Control.SINGLE_CHOICE, MultiFormControl::class)
        FormControlRegistry.registerControl(FormControlRegistry.Control.MULTI_CHOICE, MultiFormControl::class)
        FormControlRegistry.registerControl(FormControlRegistry.Control.PHOTO, ImageFormControl::class)
    }
}
