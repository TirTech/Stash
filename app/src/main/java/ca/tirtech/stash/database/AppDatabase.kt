package ca.tirtech.stash.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ca.tirtech.stash.database.dao.CategoryDAO
import ca.tirtech.stash.database.dao.FieldConfigDAO
import ca.tirtech.stash.database.dao.FieldValueDAO
import ca.tirtech.stash.database.dao.ItemDAO
import ca.tirtech.stash.database.entity.*
import ca.tirtech.stash.database.types.FieldType
import ca.tirtech.stash.database.types.RoomConverters

@Database(entities = [Category::class, Item::class, FieldConfig::class, FieldValue::class, Project::class], version = 5)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDAO(): CategoryDAO
    abstract fun itemDAO(): ItemDAO
    abstract fun fieldConfigDAO(): FieldConfigDAO
    abstract fun fieldValueDAO(): FieldValueDAO

    companion object {
        private var RESET = false
        lateinit var db: AppDatabase
        fun dbinit(context: Context) {
            if (RESET) context.deleteDatabase("stash-database")
            db = Room.databaseBuilder(context, AppDatabase::class.java, "stash-database").allowMainThreadQueries().build().apply {
                if (RESET) {
                    categoryDAO().insertCategory(Category("Root Category", null))
                    val id = categoryDAO().getRootCategory()!!.category.id
                    val ca = categoryDAO().insertCategory(Category("Category A", id)).toInt()
                    val cb = categoryDAO().insertCategory(Category("Category B", id)).toInt()
                    val cc = categoryDAO().insertCategory(Category("Category C", id)).toInt()
                    itemDAO().insertItem(Item(id, "Item A", "This is the description for Item A."))
                    itemDAO().insertItem(Item(id, "Item B", "This is the description for Item B."))
                    itemDAO().insertItem(Item(id, "Item C", "This is the description for Item C."))
                    val ca1 = categoryDAO().insertCategory(Category("Category A1", ca)).toInt()
                    fieldConfigDAO().insertFieldConfig(FieldConfig("Test Field", FieldType.STRING, ca1, true, "Testing", ArrayList()))
                }
            }
        }
    }
}
