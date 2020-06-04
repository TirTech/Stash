package ca.tirtech.stash.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryWithFieldConfigs(
    @Embedded var category: Category,
    @Relation(parentColumn = "id", entityColumn = "categoryId") var fieldConfigs: List<FieldConfig>
)
