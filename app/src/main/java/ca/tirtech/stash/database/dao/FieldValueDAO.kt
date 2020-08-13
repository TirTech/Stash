package ca.tirtech.stash.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ca.tirtech.stash.database.entity.FieldValue

@Dao
interface FieldValueDAO {
    @Insert
    suspend fun insertFieldValue(value: FieldValue?): Long

    @Update
    suspend fun updateFieldValue(value: FieldValue?)

    @Query("SELECT * FROM FieldValue")
    suspend fun getAllFieldValues(): List<FieldValue?>?

    @Query("SELECT * FROM FieldValue WHERE itemId == :id")
    suspend fun getFieldValuesForItemId(id: Int): List<FieldValue?>?
}
