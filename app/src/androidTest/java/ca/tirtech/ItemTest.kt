package ca.tirtech

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import ca.tirtech.TestInteractions.createItem
import ca.tirtech.TestInteractions.selectItemTab
import ca.tirtech.stash.MainActivity
import ca.tirtech.stash.util.EspressoCoroutineIdlingResource
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
        selectItemTab()
        createItem(newItemName, newItemDesc, emptyMap())
        onView(withText(newItemName)).check(matches(isDisplayed()))
    }

    fun randString(len: Int): String = (1..len).map { nextInt('A'.toInt(), 'Z'.toInt()).toChar() }.joinToString("")
}
