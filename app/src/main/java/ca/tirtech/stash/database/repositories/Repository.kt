package ca.tirtech.stash.database.repositories

import ca.tirtech.stash.database.AppDatabase.Companion.db
import ca.tirtech.stash.database.entity.Category
import ca.tirtech.stash.database.entity.FieldConfig
import ca.tirtech.stash.database.entity.FieldValue
import ca.tirtech.stash.database.entity.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object Repository {
    fun createCategoryWithFields(category: Category, fields: List<FieldConfig>) = transactionInCoroutine {
        val cid = db.categoryDAO().insertCategory(category).toInt()
        fields.forEach {
            it.categoryId = cid
            db.fieldConfigDAO().insertFieldConfig(it)
        }
    }

    suspend fun getCategoryFieldConfigsForItem(categoryId: Int): List<FieldConfig> = withContext(Dispatchers.Default) {
        val categoryMap = HashMap<Int, Category>()
        db.categoryDAO().getAllCategories().forEach {
            categoryMap[it.id] = it
        }
        val categoryIds = ArrayList<Int>()
        var id: Int? = categoryId
        while (id != null) {
            categoryIds.add(id)
            id = categoryMap[id]?.parentId
        }
        db.fieldConfigDAO().getAllFieldConfigsForCategoryIds(categoryIds)
    }

    fun createItemWithFields(item: Item, fields: List<FieldValue>) = transactionInCoroutine {
        val iid = db.itemDAO().insertItem(item).toInt()
        fields.forEach {
            it.itemId = iid
            db.fieldValueDAO().insertFieldValue(it)
        }
    }

    private fun transactionInCoroutine(f: () -> Unit) =
        GlobalScope.launch {
            db.runInTransaction(f)
        }

    fun updateItemWithFields(item: Item, values: List<FieldValue>) = transactionInCoroutine {
        db.itemDAO().updateItem(item)
        values.forEach { db.fieldValueDAO().updateFieldValue(it) }
    }

    fun updateCategoryWithFieldConfigs(category: Category, configs: List<FieldConfig>) = transactionInCoroutine {
        db.categoryDAO().updateCategory(category)
        // TODO update/create the configs and items that use them (use the default value for the new configs)
    }
}
