package ca.tirtech.stash.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ca.tirtech.stash.database.entity.Item
import ca.tirtech.stash.database.entity.ItemWithFieldValuesAndConfigs

@Dao
interface ItemDAO {
    @Update
    suspend fun updateItem(item: Item)

    @Insert
    suspend fun insertItem(item: Item): Long

    @Query("SELECT * FROM Item WHERE categoryId == :categoryId")
    suspend fun getItemsForCategory(categoryId: Int): List<Item>

    @Transaction
    @Query("SELECT * FROM Item WHERE id == :id")
    suspend fun getItemWithFieldValuesAndConfigs(id: Int): ItemWithFieldValuesAndConfigs?

    @Query("SELECT * FROM Item WHERE id == :id")
    suspend fun getItemForId(id: Int): Item?
}
