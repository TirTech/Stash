package ca.tirtech

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import ca.tirtech.stash.MainActivity
import ca.tirtech.stash.database.AppDatabase
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
abstract class BaseTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setup() {
        ApplicationProvider.getApplicationContext<Application>().deleteDatabase("stash.db")
        AppDatabase.dbinit(ApplicationProvider.getApplicationContext())
    }
}
