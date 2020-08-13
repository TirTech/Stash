package ca.tirtech.stash.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ca.tirtech.stash.database.dao.*
import ca.tirtech.stash.database.entity.*
import ca.tirtech.stash.database.types.RoomConverters

@Database(entities = [Category::class, Item::class, FieldConfig::class, FieldValue::class, Project::class, ItemPhoto::class], version = 8)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDAO(): CategoryDAO
    abstract fun itemDAO(): ItemDAO
    abstract fun fieldConfigDAO(): FieldConfigDAO
    abstract fun fieldValueDAO(): FieldValueDAO
    abstract fun itemPhotoDAO(): ItemPhotoDAO

    companion object {
        lateinit var db: AppDatabase
        fun dbinit(context: Context) {
            db = Room.databaseBuilder(context, AppDatabase::class.java, "stash.db")
            .createFromAsset("databases/stash-v8.db")
            .build()
        }
    }
}
