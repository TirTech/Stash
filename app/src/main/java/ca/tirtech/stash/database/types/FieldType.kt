package ca.tirtech.stash.database.types

enum class FieldType(var code: Int, var fiendlyName: String) {
    STRING(0, "Text"), NUMBER(1, "Number"), BOOLEAN(2, "Yes/No"), SINGLE_CHOICE(3, "Single Choice"), MULTI_CHOICE(4, "Multi Choice");

    override fun toString(): String {
        return fiendlyName
    }

    companion object {
        fun valueFor(type: Int): FieldType = values().find { f -> f.code == type } ?: STRING
    }
}
