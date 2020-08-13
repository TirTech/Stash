package ca.tirtech.stash.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ca.tirtech.stash.database.entity.FieldConfig

@Dao
interface FieldConfigDAO {
    @Query("SELECT * FROM FieldConfig")
    suspend fun getAllFieldConfigs(): List<FieldConfig>

    @Query("SELECT * FROM FieldConfig WHERE categoryId == :id")
    suspend fun getAllFieldConfigsForCategory(id: Int): List<FieldConfig>

    @Insert
    suspend fun insertFieldConfig(config: FieldConfig)

    @Update
    suspend fun updateFieldConfig(config: FieldConfig)

    @Query("SELECT * FROM FieldConfig WHERE categoryId IN (:ids)")
    suspend fun getAllFieldConfigsForCategoryIds(ids: List<Int>): List<FieldConfig>
}
