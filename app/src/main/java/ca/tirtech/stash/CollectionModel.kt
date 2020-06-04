package ca.tirtech.stash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ca.tirtech.stash.database.AppDatabase.Companion.db
import ca.tirtech.stash.database.entity.CategoryWithSubcategory
import ca.tirtech.stash.util.popTo
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.*

class CollectionModel : ViewModel() {

    private val database = db
    private val selectedCategoryId = MutableLiveData(0)
    val currentCategory = Transformations.map(selectedCategoryId, this::mapIdToCategory)
    var categoryStack: Stack<CategoryWithSubcategory> = Stack()
    val items = Transformations.switchMap(selectedCategoryId) { id: Int -> database.itemDAO().getItemsForCategory(id) }

    fun setSelectedCategoryId(id: Int) {
        selectedCategoryId.value = id
    }

    fun traverseToParentCategory() = currentCategory.value?.category?.parentId?.let { setSelectedCategoryId(it) }

    private fun mapIdToCategory(id: Int): CategoryWithSubcategory =
        database.categoryDAO().getCategoryWithSubcategories(id).also {
            solveCategoryStack(it)
        }

    init {
        database.categoryDAO().getRootCategory()?.let { selectedCategoryId.value = it.category.id }
    }

    fun refreshCategory() {
         MainScope().launch {
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
                categoryStack.popTo({change.category.id == it.category.id}, true)
                categoryStack.add(change)
            }

        }
    }
}
