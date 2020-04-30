package ca.tirtech.stash.database.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Category (
	var name: String,
	var parentId: Int? = null,
	@PrimaryKey(autoGenerate = true) var id: Int = 0
)
