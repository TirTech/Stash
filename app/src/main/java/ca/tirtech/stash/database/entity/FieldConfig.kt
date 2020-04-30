package ca.tirtech.stash.database.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ca.tirtech.stash.database.types.FieldType
import java.util.*
import kotlin.collections.ArrayList

@Entity
data class FieldConfig (
    var name: String,
    var type: FieldType,
    var categoryId: Int?,
    var showAsLabel: Boolean,
    var defaultValue: String,
    var choices: ArrayList<String> = ArrayList(),
    @PrimaryKey(autoGenerate = true) var id: Int = 0
)
