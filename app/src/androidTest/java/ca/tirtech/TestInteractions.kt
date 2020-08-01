package ca.tirtech

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import ca.tirtech.stash.R
import ca.tirtech.stash.database.types.FieldType
import org.hamcrest.Matchers

object TestInteractions {
    fun createBasicCategory(name: String) {
        selectCategoryTab()
        onView(withId(R.id.btn_add_category)).perform(click())
        onView(withId(R.id.edittxt_category_name)).perform(typeText(name))
        onView(withId(R.id.btn_apply_new_category)).perform(click())
    }

    fun createCategoryWithStringConfig(name: String, configName: String, defaultVal: String) {
        selectCategoryTab()
        onView(withId(R.id.btn_add_category)).perform(click())
        onView(withId(R.id.edittxt_category_name)).perform(typeText(name))
        makeStringFieldConfig(configName, defaultVal)
        onView(withId(R.id.btn_apply_new_category)).perform(click())
    }

    fun createCategoryWithSingleSelectConfig(name: String, configName: String, options: List<String>, defaultIndex: Int) {
        selectCategoryTab()
        onView(withId(R.id.btn_add_category)).perform(click())
        onView(withId(R.id.edittxt_category_name)).perform(typeText(name))
        makeSingleSelectFieldConfig(configName, options, defaultIndex)
        onView(withId(R.id.btn_apply_new_category)).perform(click())
    }

    fun makeStringFieldConfig(configName: String, default: String) {
        onView(withId(R.id.btn_add_new_field)).perform(scrollTo(), click())
        onView(withId(R.id.edittxt_field_name)).perform(scrollTo(), typeText(configName))
        onView(withId(R.id.edittxt_field_value)).perform(scrollTo(), typeText(default))
        onView(withId(R.id.btn_confirm_field_add)).perform(scrollTo(), click())
    }

    fun makeSingleSelectFieldConfig(configName: String, options: List<String>, defaultIndex: Int) {
        onView(withId(R.id.btn_add_new_field)).perform(scrollTo(), click())
        onView(withId(R.id.edittxt_field_name)).perform(scrollTo(), typeText(configName))
        onView(withId(R.id.spn_field_type)).perform(scrollTo(), click())
        onView(withText(FieldType.SINGLE_CHOICE.fiendlyName)).perform(click())
        onView(withId(R.id.lstb_choices)).perform(scrollTo())
        for (option: String in options) {
            onView(withId(R.id.txt_lst_edit_value)).perform(typeText(option))
            onView(withId(R.id.btn_lst_editor_add)).perform(click())
        }
        onView(withId(R.id.spn_field_value)).perform(scrollTo(), click())
        onView(withText(options[defaultIndex])).perform(click())
        onView(withId(R.id.btn_confirm_field_add)).perform(scrollTo(), click())
    }

    fun createItem(name: String, desc: String, fieldVals: Map<String, Pair<FieldType, String>>) {
        selectItemTab()
        onView(withId(R.id.btn_add_item)).perform(click())
        onView(withId(R.id.edittxt_item_title)).perform(scrollTo(), typeText(name))
        onView(withId(R.id.edittxt_item_description)).perform(scrollTo(), typeText(desc))
        fieldVals.forEach { k, vp->
            val type = vp.first
            val v = vp.second
            when(type) {
                FieldType.STRING -> {
                    onView(Matchers.allOf(withHint(k), withId(R.id.edittxt_field_value)))
                        .perform(scrollTo(), clearText(), typeText(v))
                }
                FieldType.NUMBER -> {
                    onView(Matchers.allOf(withHint(k), withId(R.id.edittxt_field_value)))
                        .perform(scrollTo(), clearText(), typeText(v))
                }
                FieldType.BOOLEAN -> TODO()
                FieldType.SINGLE_CHOICE -> {
                    onView(withId(R.id.spn_field_value)).perform(scrollTo(), click())
                    onView(withText(v)).perform(click())
                }
                FieldType.MULTI_CHOICE -> TODO()
            }
        }
        onView(withId(R.id.btn_apply_new_item)).perform(click())
    }

    fun selectItemTab() {
        onView(withText(R.string.tab_text_items)).perform(click())
    }

    fun selectCategoryTab() {
        onView(withText(R.string.tab_text_category)).perform(click())
    }

    fun navigateToCategory(name: String) {
        selectCategoryTab()
        onView(withText(name)).perform(click())
    }
}
