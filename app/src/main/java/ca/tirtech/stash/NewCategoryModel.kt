package ca.tirtech.stash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ca.tirtech.stash.database.entity.FieldConfig
import ca.tirtech.stash.database.types.FieldType
import ca.tirtech.stash.util.DeepLiveData

class NewCategoryModel : ViewModel() {
    val showEditor: MutableLiveData<Boolean> = MutableLiveData()
    val fieldConfigs = DeepLiveData.observableList<FieldConfig>()

    init {
        fieldConfigs.value.addAll(
            arrayOf(
                FieldConfig("Name A - Bool", FieldType.BOOLEAN, null, true, "true", ArrayList()),
                FieldConfig("Name B - Str", FieldType.STRING, null, true, "Hello", ArrayList()),
                FieldConfig("Name C - MC", FieldType.MULTI_CHOICE, null, true, "['A','B']", arrayListOf("A","B","C")),
                FieldConfig("Name D - Num", FieldType.NUMBER, null, true, "5", ArrayList())
            )
        )
    }
}
