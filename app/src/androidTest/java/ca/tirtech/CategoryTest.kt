package ca.tirtech

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import ca.tirtech.stash.R
import org.junit.Test

class CategoryTest: BaseTest() {

    @Test
    fun createNewBasicCategory() {
        onView(withId(R.id.btn_add_category)).perform(click())
    }
}
