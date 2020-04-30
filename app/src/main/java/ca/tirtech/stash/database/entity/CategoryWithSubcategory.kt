package ca.tirtech.stash.database.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

data class CategoryWithSubcategory (
	@Embedded var category: Category,
	@Relation(
			parentColumn = "id",
			entityColumn = "parentId"
	)
	var subcategories: List<Category>
)
