package ca.tirtech.stash.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ItemPhoto(
    var fileName: String,
    var isCoverImage: Boolean = false,
    var itemId: Int? = null,
    @PrimaryKey(autoGenerate = true) var id: Int? = null
)
