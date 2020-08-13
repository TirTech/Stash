package ca.tirtech.stash.database.repositories

import androidx.room.withTransaction
import ca.tirtech.stash.database.AppDatabase.Companion.db
import ca.tirtech.stash.database.entity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object Repository {
    suspend fun createCategoryWithFields(category: Category, fields: List<FieldConfig>) = transactionInCoroutine {
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

    suspend fun createItemWithFieldsAndPhotos(item: Item, fields: List<FieldValue>, photos: List<ItemPhoto>) = transactionInCoroutine {
        val iid = db.itemDAO().insertItem(item).toInt()
        fields.forEach {
            it.itemId = iid
            db.fieldValueDAO().insertFieldValue(it)
        }
        photos.forEach {
            it.itemId = iid
            db.itemPhotoDAO().createItemPhoto(it)
        }
    }

    private suspend fun transactionInCoroutine(f: suspend () -> Unit) =
        db.withTransaction(f)

    suspend fun updateItemWithFieldsAndPhotos(item: Item, values: List<FieldValue>, photos: List<ItemPhoto>) = transactionInCoroutine {
        db.itemDAO().updateItem(item)
        values.forEach { db.fieldValueDAO().updateFieldValue(it) }
        photos.forEach {
            if (it.id != null) {
                db.itemPhotoDAO().updateItemPhoto(it)
            } else {
                it.itemId = item.id
                db.itemPhotoDAO().createItemPhoto(it)
            }
        }
    }

    suspend fun updateCategoryWithFieldConfigs(category: Category, configs: List<FieldConfig>) = transactionInCoroutine {
        db.categoryDAO().updateCategory(category)
        configs.forEach {
            it.categoryId = category.id
            if (it.id > 0) {
                db.fieldConfigDAO().updateFieldConfig(it)
            } else {
                db.fieldConfigDAO().insertFieldConfig(it)
            }
        }
        // TODO update items that use new configs (use the default value for the new configs)
    }
}
