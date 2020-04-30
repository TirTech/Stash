package ca.tirtech.stash.database.dao

import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ca.tirtech.stash.database.entity.FieldConfig

internal interface FieldConfigDAO {
    @Query("SELECT * FROM FieldConfig")
    fun getAllFieldConfigs(): List<FieldConfig?>?

    @Query("SELECT * FROM FieldConfig WHERE categoryId == :id")
    fun getAllFieldConfigsForCategory(id: Int): List<FieldConfig?>?

    @Insert
    fun insertFieldConfig(config: FieldConfig?)

    @Update
    fun updateFieldConfig(config: FieldConfig?)
}
