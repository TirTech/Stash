package ca.tirtech

import android.widget.ImageView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import ca.tirtech.TestInteractions.createBasicCategory
import ca.tirtech.TestInteractions.createCategoryWithStringConfig
import ca.tirtech.stash.MainActivity
import ca.tirtech.stash.R
import ca.tirtech.stash.util.EspressoCoroutineIdlingResource
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.instanceOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random.Default.nextInt

@RunWith(AndroidJUnit4::class)
@LargeTest
class CategoryTest {

    @get:Rule
    val activityRule = CustomActivityTestRule(MainActivity::class.java)

    private var dbIdleResource: IdlingResource? = null

    @Before
    fun setup() {
        dbIdleResource = EspressoCoroutineIdlingResource.getIdlingResource()
        IdlingRegistry.getInstance().register(dbIdleResource)
    }

    @After
    fun teardown() {
        IdlingRegistry.getInstance().unregister(dbIdleResource)
    }

    @Test
    fun createNewBasicCategory() {
        val newCategoryName = randString(10)
        createBasicCategory(newCategoryName)
        onView(withText(newCategoryName)).check(matches(isDisplayed()))
    }

    @Test
    fun editCategoryNameOnly() {
        val newCategoryName = randString(10)
        val editedCategoryName = randString(10)
        createBasicCategory(newCategoryName)
        onView(withText(newCategoryName)).check(matches(isDisplayed()))
        onView(allOf(hasSibling(withText(newCategoryName)), instanceOf(ImageView::class.java))).perform(click())
        onView(withId(R.id.edittxt_category_name)).perform(clearText()).perform(typeText(editedCategoryName))
        onView(withId(R.id.btn_apply_new_category)).perform(click())
        onView(withText(newCategoryName)).check(doesNotExist())
        onView(withText(editedCategoryName)).check(matches(isDisplayed()))
    }

    @Test
    fun createCategoryWithFieldConfig() {
        val newCategoryName = randString(10)
        val fieldName = randString(5)
        val fieldDefault = randString(5)
        createCategoryWithStringConfig(newCategoryName, fieldName, fieldDefault)
        onView(allOf(hasSibling(withText(newCategoryName)), instanceOf(ImageView::class.java))).perform(click())
        onView(withText(fieldName)).check(matches(isDisplayed()))
    }

    fun randString(len: Int): String = (1..len).map { nextInt('A'.toInt(), 'Z'.toInt()).toChar() }.joinToString("")
}
