package ca.tirtech

import android.app.Application
import androidx.test.InstrumentationRegistry.getTargetContext
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import ca.tirtech.stash.MainActivity
import ca.tirtech.stash.R
import ca.tirtech.stash.database.AppDatabase
import ca.tirtech.stash.database.AppDatabase.Companion.db
import ca.tirtech.stash.database.entity.Category
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MetaTest {

    val catName = "Test Cat"

    @Before
    fun setup() {
        ApplicationProvider.getApplicationContext<Application>().deleteDatabase("stash.db")
        AppDatabase.dbinit(ApplicationProvider.getApplicationContext())
    }

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun appLaunchesToHomePage() {
        onView(withId(R.id.root_collection_fragment)).check(matches(isDisplayed()))
    }

    @Test
    fun dbTestA() {
        assertNull(db.categoryDAO().getAllCategories().firstOrNull { it.name == catName })
        db.categoryDAO().insertCategory(Category(catName,1))
    }

    @Test
    fun dbTestB() {
        assertNull(db.categoryDAO().getAllCategories().firstOrNull { it.name == catName })
        db.categoryDAO().insertCategory(Category(catName,1))
    }
}
