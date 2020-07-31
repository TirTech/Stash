package ca.tirtech

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
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
class MetaTest {

    @get:Rule
    val activityRule = CustomActivityTestRule(MainActivity::class.java)

    @Before
    fun setup() {
        ApplicationProvider.getApplicationContext<Application>().deleteDatabase("stash.db")
        AppDatabase.dbinit(ApplicationProvider.getApplicationContext())
    }

    val catName = "Test Cat"

    @Test
    fun appLaunchesToHomePage() {
        onView(withId(R.id.root_collection_fragment)).check(matches(isDisplayed()))
    }

    /**
     * Verify the database remains clean between runs.
     * Either of these two tests will cause the other to fail if the database is dirty between runs.
     */
    @Test
    fun dbLoadTestA() {
        assertNull(db.categoryDAO().getAllCategories().firstOrNull { it.name == catName })
        db.categoryDAO().insertCategory(Category(catName,1))
    }

    /**
     * Verify the database remains clean between runs.
     * Either of these two tests will cause the other to fail if the database is dirty between runs.
     */
    @Test
    fun dbLoadTestB() {
        assertNull(db.categoryDAO().getAllCategories().firstOrNull { it.name == catName })
        db.categoryDAO().insertCategory(Category(catName,1))
    }
}
