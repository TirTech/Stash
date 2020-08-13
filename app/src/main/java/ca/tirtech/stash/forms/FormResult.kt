package ca.tirtech.stash.forms

class FormResult(val name: String) {
    val fieldValues = ArrayList<Pair<FormField,Any>>()
    val subformValues = ArrayList<FormResult>()

    fun valueFor(id: String) = fieldValues.find { it.first.name == id } ?: throw NoSuchElementException("There is no form field with the id $id")
    fun subresultFor(id: String): FormResult = subformValues.find { it.name == id } ?: throw NoSuchElementException("There is no form result with the id $id")
}
