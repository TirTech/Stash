package ca.tirtech.stash.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class FieldValueWithConfig (
    @Embedded var fieldValue: FieldValue,
    @Relation(
        parentColumn = "configId",
        entityColumn = "id"
    ) var fieldConfig: FieldConfig
)
