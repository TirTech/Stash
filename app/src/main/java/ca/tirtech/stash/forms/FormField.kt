package ca.tirtech.stash.forms

typealias Accumulator<T> = (Any, T) -> Unit

data class FormField(val name: String, val type: String, val config: Map<String, *>, val onSubmit: Accumulator<Any> = {_, _ -> }, val defaultProvider: (suspend () -> Any?)? = null)
