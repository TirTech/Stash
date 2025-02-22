package ca.tirtech.stash.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ItemWithFieldValuesAndConfigs(
    @Embedded var item: Item,
    @Relation(
        entity=FieldValue::class,
        parentColumn = "id",
        entityColumn = "itemId"
    ) var fieldValues: List<FieldValueWithConfig>
)
