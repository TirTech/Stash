package ca.tirtech.stash.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ca.tirtech.stash.database.entity.FieldConfig

@Dao
interface FieldConfigDAO {
    @Query("SELECT * FROM FieldConfig")
    fun getAllFieldConfigs(): List<FieldConfig>

    @Query("SELECT * FROM FieldConfig WHERE categoryId == :id")
    fun getAllFieldConfigsForCategory(id: Int): List<FieldConfig>

    @Insert
    fun insertFieldConfig(config: FieldConfig)

    @Update
    fun updateFieldConfig(config: FieldConfig)

    @Query("SELECT * FROM FieldConfig WHERE categoryId IN (:ids)")
    fun getAllFieldConfigsForCategoryIds(ids: List<Int>): List<FieldConfig>
}
