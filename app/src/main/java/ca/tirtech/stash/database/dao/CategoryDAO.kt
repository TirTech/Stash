package ca.tirtech.stash.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ca.tirtech.stash.database.entity.Category
import ca.tirtech.stash.database.entity.CategoryWithSubcategory
import ca.tirtech.stash.database.entity.FieldConfig

@Dao
interface CategoryDAO {
    @Transaction
    @Query("SELECT * FROM Category WHERE id==:id")
    fun getCategoryWithSubcategories(id: Int): CategoryWithSubcategory

    @Query("SELECT * FROM Category WHERE parentId is NULL")
    @Transaction
    fun getRootCategory(): CategoryWithSubcategory?

    @Query("SELECT * FROM Category")
    fun getAllCategories(): List<Category>

    @Insert
    fun insertCategory(category: Category): Long

    @Update
    fun updateCategory(category: Category)
}
