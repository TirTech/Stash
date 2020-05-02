package ca.tirtech.stash.database.types

import androidx.room.TypeConverter
import ca.tirtech.stash.util.fromJsonString
import org.json.JSONArray
import java.util.*

object RoomConverters {
    @TypeConverter
    fun arrayListToString(list: ArrayList<String>?): String = JSONArray(list).toString()

    @TypeConverter
    fun stringToArrayList(list: String): ArrayList<String> = ArrayList<String>().fromJsonString(list)

    @TypeConverter
    fun intToFieldType(type: Int): FieldType = FieldType.valueFor(type)

    @TypeConverter
    fun fieldTypeToInt(type: FieldType): Int = type.code
}
