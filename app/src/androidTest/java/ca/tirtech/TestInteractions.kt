package ca.tirtech

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import ca.tirtech.stash.R
import org.hamcrest.Matchers

object TestInteractions {
    fun createBasicCategory(name: String) {
        onView(withId(R.id.btn_add_category)).perform(click())
        onView(withId(R.id.edittxt_category_name)).perform(typeText(name))
        onView(withId(R.id.btn_apply_new_category)).perform(click())
    }

    fun createCategoryWithStringConfig(name: String, configName: String, defaultVal: String) {
        onView(withId(R.id.btn_add_category)).perform(click())
        onView(withId(R.id.edittxt_category_name)).perform(typeText(name))
        makeStringFieldConfig(configName, defaultVal)
        onView(withId(R.id.btn_apply_new_category)).perform(click())
    }

    fun makeStringFieldConfig(configName: String, default: String) {
        onView(withId(R.id.btn_add_new_field)).perform(scrollTo()).perform(click())
        onView(withId(R.id.edittxt_field_name)).perform(scrollTo()).perform(typeText(configName))
        onView(withId(R.id.edittxt_field_value)).perform(scrollTo()).perform(typeText(default))
        onView(withId(R.id.btn_confirm_field_add)).perform(scrollTo()).perform(click())
    }

    fun createItem(name: String, desc: String, fieldVals: Map<String,String>) {
        onView(withId(R.id.btn_add_item)).perform(click())
        onView(withId(R.id.edittxt_item_title)).perform(typeText(name))
        onView(withId(R.id.edittxt_item_description)).perform(typeText(desc))
        fieldVals.forEach { k, v ->
            onView(Matchers.allOf(withHint(k), withId(R.id.edittxt_field_value)))
                .perform(typeText(v))
        }
        onView(withId(R.id.btn_apply_new_item)).perform(click())
    }

    fun selectItemTab() {
        onView(withText(R.string.tab_text_items)).perform(click())
    }

    fun selectCategoryTab() {
        onView(withText(R.string.tab_text_category)).perform(click())
    }
}
