package ca.tirtech

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import ca.tirtech.stash.R
import ca.tirtech.stash.database.AppDatabase.Companion.db
import ca.tirtech.stash.database.entity.Category
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

class MetaTest: BaseTest() {

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
