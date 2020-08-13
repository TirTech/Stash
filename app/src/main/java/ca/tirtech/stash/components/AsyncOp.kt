package ca.tirtech.stash.components

import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicReference

class AsyncOp<T>(val op: suspend () -> T) {
    private val ref: AtomicReference<Deferred<T>> = AtomicReference()
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)

    suspend fun getValue(): T {
        return ref.updateAndGet {
            it ?: scope.async {op()}
        }.await()
    }

    fun onComplete(op: suspend (T) -> Unit) = scope.launch {
        op(getValue())
    }

    fun getOrNull(): T? {
        return try {
            ref.get().getCompleted()
        } catch (e: Exception) {
            null
        }
    }
}
