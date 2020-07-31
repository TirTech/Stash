package ca.tirtech

import android.app.Activity
import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.rule.ActivityTestRule

class CustomActivityTestRule<T: Activity>(activityClass: Class<T>): ActivityTestRule<T>(activityClass) {
    override fun beforeActivityLaunched() {
        ApplicationProvider.getApplicationContext<Application>().deleteDatabase("stash.db")
    }
}
