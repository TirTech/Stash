package ca.tirtech.stash.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ca.tirtech.stash.database.entity.FieldValue

@Dao
interface FieldValueDAO {
    @Insert
    fun insertFieldValue(value: FieldValue?): Long

    @Update
    fun updateFieldValue(value: FieldValue?)

    @Query("SELECT * FROM FieldValue")
    fun getAllFieldValues(): List<FieldValue?>?

    @Query("SELECT * FROM FieldValue WHERE itemId == :id")
    fun getFieldValuesForItemId(id: Int): List<FieldValue?>?
}
