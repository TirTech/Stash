package ca.tirtech.stash.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ca.tirtech.stash.database.dao.CategoryDAO
import ca.tirtech.stash.database.dao.ItemDAO
import ca.tirtech.stash.database.entity.Category
import ca.tirtech.stash.database.entity.FieldConfig
import ca.tirtech.stash.database.entity.FieldValue
import ca.tirtech.stash.database.entity.Item
import ca.tirtech.stash.database.types.RoomConverters

@Database(entities = [Category::class, Item::class, FieldConfig::class, FieldValue::class], version = 3)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDAO(): CategoryDAO
    abstract fun itemDAO(): ItemDAO

    companion object {
        lateinit var db: AppDatabase
        fun dbinit(context: Context) {
            context.deleteDatabase("stash-database")
            db = Room.databaseBuilder(context, AppDatabase::class.java, "stash-database").allowMainThreadQueries().build().apply {
                categoryDAO().insertCategory(Category("Root Category", null))
                val id = categoryDAO().getRootCategory()!!.category.id
                categoryDAO().insertCategory(Category("Category A", id))
                categoryDAO().insertCategory(Category("Category B", id))
                categoryDAO().insertCategory(Category("Category C", id))
                itemDAO().insertItem(Item(id, "Item A", "This is the description for Item A."))
                itemDAO().insertItem(Item(id, "Item B", "This is the description for Item B."))
                itemDAO().insertItem(Item(id, "Item C", "This is the description for Item C."))
            }
        }
    }
}
