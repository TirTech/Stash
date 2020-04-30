package ca.tirtech.stash.database.types

enum class FieldType(var code: Int) {
    STRING(0),
    NUMBER(1),
    BOOLEAN(2),
    SINGLE_CHOICE(3),
    MULTI_CHOICE(4);

    companion object {
        fun valueFor(type: Int): FieldType = values().find { f -> f.code == type } ?: STRING
    }
}
