package io.github.mmolosay.debounce.lib

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

internal class DebouncedAction internal constructor(
    private val timeout: Duration,
    private val action: () -> Unit,
    private val postInvoke: ((Boolean) -> Unit)? = null,
) : () -> Unit {

    private var lastInvokationTime: Long? = null

    override fun invoke() {
        val shouldExecute = hadTimeoutPassed() // and wasExecuted
        if (shouldExecute) {
            action()
            lastInvokationTime = now()
        }
        postInvoke?.invoke(shouldExecute)
    }

    private fun hadTimeoutPassed(): Boolean =
        lastInvokationTime?.let {
            val elapsed = now() - it
            elapsed.milliseconds >= timeout
        } ?: true

    private fun now(): Long =
        System.currentTimeMillis()
}