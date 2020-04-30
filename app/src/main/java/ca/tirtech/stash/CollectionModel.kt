package ca.tirtech.stash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ca.tirtech.stash.database.AppDatabase.Companion.db

class CollectionModel : ViewModel() {
    private val database = db
    private val selectedCategoryId = MutableLiveData<Int>()
    val currentCategory =
        Transformations.switchMap(selectedCategoryId) { id: Int -> database.categoryDAO().getCategoryWithSubcategories(id) }
    val items =
        Transformations.switchMap(selectedCategoryId) { id: Int -> database.itemDAO().getItemsForCategory(id) }

    fun setSelectedCategoryId(id: Int) {
        selectedCategoryId.value = id
    }

    fun traverseToParentCategory() = currentCategory.value?.category?.parentId?.let { selectedCategoryId.value = it }

    init {
        database.categoryDAO().getRootCategory()?.let { selectedCategoryId.value = it.category.id }
    }
}
