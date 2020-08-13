package ca.tirtech.stash.database.dao

import androidx.room.*
import ca.tirtech.stash.database.entity.Category
import ca.tirtech.stash.database.entity.CategoryWithFieldConfigs
import ca.tirtech.stash.database.entity.CategoryWithSubcategory

@Dao
interface CategoryDAO {
    @Transaction
    @Query("SELECT * FROM Category WHERE id==:id")
    suspend fun getCategoryWithSubcategories(id: Int): CategoryWithSubcategory

    @Query("SELECT * FROM Category WHERE parentId is NULL")
    @Transaction
    suspend fun getRootCategory(): CategoryWithSubcategory?

    @Query("SELECT * FROM Category")
    suspend fun getAllCategories(): List<Category>

    @Insert
    suspend fun insertCategory(category: Category): Long

    @Update
    suspend fun updateCategory(category: Category)

    @Transaction
    @Query("SELECT * FROM Category WHERE id==:id")
    suspend fun getCategoryWithFieldConfigs(id: Int): CategoryWithFieldConfigs?
}
