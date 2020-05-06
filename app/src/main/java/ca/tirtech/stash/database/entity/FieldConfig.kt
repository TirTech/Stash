package ca.tirtech.stash.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ca.tirtech.stash.database.types.FieldType

@Entity
data class FieldConfig(
    var name: String,
    var type: FieldType,
    var categoryId: Int?,
    var showAsLabel: Boolean,
    var defaultValue: String,
    var choices: ArrayList<String> = ArrayList(),
    @PrimaryKey(autoGenerate = true) var id: Int = 0
)
