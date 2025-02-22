package ca.tirtech.stash.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Project(var title: String, var description: String, @PrimaryKey(autoGenerate = true) var id: Int = 0)
