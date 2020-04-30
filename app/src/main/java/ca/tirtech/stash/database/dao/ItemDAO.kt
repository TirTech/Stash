package ca.tirtech.stash.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ca.tirtech.stash.database.entity.Item
import ca.tirtech.stash.database.entity.ItemWithFieldValues

@Dao
interface ItemDAO {
    @Update
    fun updateItem(item: Item?)

    @Insert
    fun insertItem(item: Item?)

    @Query("SELECT * FROM Item WHERE categoryId == :categoryId")
    fun getItemsForCategory(categoryId: Int): LiveData<List<Item>?>

    @Transaction
    @Query("SELECT * FROM Item WHERE id == :id")
    fun getItemWithFieldValues(id: Int): List<ItemWithFieldValues>?
}
