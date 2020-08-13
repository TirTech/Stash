package ca.tirtech.stash.database.dao

import androidx.room.*
import ca.tirtech.stash.database.entity.ItemPhoto

@Dao
interface ItemPhotoDAO {

    @Update
    suspend fun updateItemPhoto(itemPhoto: ItemPhoto)

    @Insert
    suspend fun createItemPhoto(itemPhoto: ItemPhoto): Long

    @Transaction
    @Query("SELECT * FROM ItemPhoto WHERE id==:id")
    suspend fun getItemPhotoById(id: Int): ItemPhoto

    @Transaction
    @Query("SELECT * FROM ItemPhoto WHERE itemId==:id")
    suspend fun getItemPhotoByItemId(id: Int): List<ItemPhoto>

    @Transaction
    @Query("SELECT * FROM ItemPhoto WHERE itemId==:id AND isCoverImage == 1")
    suspend fun getItemCoverPhotoByItemId(id: Int): ItemPhoto?
}
