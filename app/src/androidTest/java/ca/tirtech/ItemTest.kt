package ca.tirtech

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import ca.tirtech.TestInteractions.createCategoryWithSingleSelectConfig
import ca.tirtech.TestInteractions.createCategoryWithStringConfig
import ca.tirtech.TestInteractions.createItem
import ca.tirtech.TestInteractions.navigateToCategory
import ca.tirtech.stash.MainActivity
import ca.tirtech.stash.database.types.FieldType
import ca.tirtech.stash.util.EspressoCoroutineIdlingResource
import org.hamcrest.Matchers.startsWith
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random.Default.nextInt

@RunWith(AndroidJUnit4::class)
@LargeTest
class ItemTest {

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
    fun createNewBasicItem() {
        val newItemName = randString(10)
        val newItemDesc = randString(20)
        createItem(newItemName, newItemDesc, emptyMap())
        onView(withText(newItemName)).check(matches(isDisplayed()))
    }

    @Test
    fun createNewItemWithStringField() {
        val newItemName = randString(10)
        val newItemDesc = randString(20)
        val newCategoryName = randString(10)
        val fieldName = randString(5)
        val fieldDefault = randString(5)
        val fieldValue = randString(5)
        createCategoryWithStringConfig(newCategoryName, fieldName, fieldDefault)
        navigateToCategory(newCategoryName)
        createItem(newItemName, newItemDesc, mapOf(fieldName to (FieldType.STRING to fieldValue)))
        onView(withText(newItemName)).check(matches(isDisplayed())).perform(click())
        onView(withText(startsWith(fieldName))).check(matches(isDisplayed()))
        onView(withText(fieldValue)).check(matches(isDisplayed()))
    }

    @Test
    fun createNewItemWithSingleSelectField() {
        val newItemName = randString(10)
        val newItemDesc = randString(20)
        val newCategoryName = randString(10)
        val fieldName = randString(5)
        val options = (1..5).map { randString(4) }
        val default = nextInt(1,options.size-1)
        val fieldValue = options[nextInt(1,options.size-1)]
        createCategoryWithSingleSelectConfig(newCategoryName, fieldName, options, default)
        navigateToCategory(newCategoryName)
        createItem(newItemName, newItemDesc, mapOf(fieldName to (FieldType.SINGLE_CHOICE to fieldValue)))
        onView(withText(newItemName)).check(matches(isDisplayed())).perform(click())
        onView(withText(startsWith(fieldName))).check(matches(isDisplayed()))
        onView(withText(fieldValue)).check(matches(isDisplayed()))
    }

    fun randString(len: Int): String = (1..len).map { nextInt('A'.toInt(), 'Z'.toInt()).toChar() }.joinToString("")
}
