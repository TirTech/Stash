package ca.tirtech.stash

import androidx.lifecycle.*
import ca.tirtech.stash.database.AppDatabase.Companion.db
import ca.tirtech.stash.database.entity.CategoryWithSubcategory
import ca.tirtech.stash.util.popTo
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class CollectionModel : ViewModel() {

    private val database = db
    private val selectedCategoryId = MutableLiveData(0)
    val currentCategory = selectedCategoryId.switchMap { id ->
        liveData {
                emit(database.categoryDAO().getCategoryWithSubcategories(id).also {
                    solveCategoryStack(it)
                })
        }
    }
    var categoryStack: Stack<CategoryWithSubcategory> = Stack()
    val items = selectedCategoryId.switchMap { id ->
        liveData {
            emit(database.itemDAO().getItemsForCategory(id))
        }
    }

    fun setSelectedCategoryId(id: Int) {
        selectedCategoryId.value = id
    }

    fun traverseToParentCategory() = currentCategory.value?.category?.parentId?.let { setSelectedCategoryId(it) }

    init {
        runBlocking {
           database.categoryDAO().getRootCategory()?.let { selectedCategoryId.value = it.category.id }
        }
    }

    fun refreshCategory() {
        viewModelScope.launch {
            selectedCategoryId.value = selectedCategoryId.value
        }
    }

    private fun solveCategoryStack(change: CategoryWithSubcategory) {
        /*Cases:
            Child -> add to stack
            Parent -> pop parent off stack
            Distant Parent -> pop till you get to the parent (this and single parent are the same)
            Distant Child/Cousin -> we're screwed (assume it's a distant parent and pray it's there)
         */
        when {
            categoryStack.isEmpty() -> categoryStack.add(change)
            change.category.parentId == categoryStack.peek().category.id -> categoryStack.add(change)
            else -> {
                categoryStack.popTo({ change.category.id == it.category.id }, true)
                categoryStack.add(change)
            }

        }
    }
}
