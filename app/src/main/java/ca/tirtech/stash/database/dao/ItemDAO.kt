package ca.tirtech.stash.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ca.tirtech.stash.database.entity.Item
import ca.tirtech.stash.database.entity.ItemWithFieldValuesAndConfigs

@Dao
interface ItemDAO {
    @Update
    fun updateItem(item: Item?)

    @Insert
    fun insertItem(item: Item?): Long

    @Query("SELECT * FROM Item WHERE categoryId == :categoryId")
    fun getItemsForCategory(categoryId: Int): LiveData<List<Item>>

    @Transaction
    @Query("SELECT * FROM Item WHERE id == :id")
    fun getItemWithFieldValuesAndConfigs(id: Int): ItemWithFieldValuesAndConfigs?

    @Query("SELECT * FROM Item WHERE id == :id")
    suspend fun getItemForId(id: Int): Item?
}
