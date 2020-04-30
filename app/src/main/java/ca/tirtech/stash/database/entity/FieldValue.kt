package ca.tirtech.stash.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FieldValue(
    var configId: Int,
    var value: String,
    var itemId: Int,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
)
