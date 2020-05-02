package ca.tirtech.stash.util

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.LiveData

class DeepLiveData<T : Any> private constructor(private val initialVal: T) : LiveData<T>(initialVal) {

    override fun getValue(): T {
        return super.getValue()!!
    }

    private fun poke() {
        this.value = this.value
    }

    companion object {
        fun <T> observableList(): DeepLiveData<ObservableArrayList<T>> = ObservableArrayList<T>().run {
            DeepLiveData(this).also { dld ->
                addOnListChangedCallback(ObservableListChangeListener { dld.poke() })
            }
        }
    }
}
