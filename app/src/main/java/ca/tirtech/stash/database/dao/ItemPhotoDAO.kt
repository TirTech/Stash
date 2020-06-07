package ca.tirtech.stash.database.dao

import androidx.room.*
import ca.tirtech.stash.database.entity.ItemPhoto

@Dao
interface ItemPhotoDAO {

    @Update
    fun updateItemPhoto(itemPhoto: ItemPhoto)

    @Insert
    fun createItemPhoto(itemPhoto: ItemPhoto): Long

    @Transaction
    @Query("SELECT * FROM ItemPhoto WHERE id==:id")
    fun getItemPhotoById(id: Int): ItemPhoto

    @Transaction
    @Query("SELECT * FROM ItemPhoto WHERE itemId==:id")
    fun getItemPhotoByItemId(id: Int): List<ItemPhoto>

    @Transaction
    @Query("SELECT * FROM ItemPhoto WHERE itemId==:id AND isCoverImage == 1")
    fun getItemCoverPhotoByItemId(id: Int): ItemPhoto?
}
