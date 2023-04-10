package io.github.mmolosay.debounce.lib

import kotlin.time.Duration
import kotlin.time.Duration.Companion.ZERO
import kotlin.time.Duration.Companion.nanoseconds

internal class DebouncedAction internal constructor(
    private val timeout: Duration,
    private val action: () -> Unit,
    private val postInvoke: PostInvokeAction? = null,
) : () -> Unit {

    private var lastInvokationTime: Long? = null

    override fun invoke() {
        val elapsed = elapsed()
        if (elapsed == null) { // very first invokation
            executeAction()
        } else {
            val timeoutTimeLeft = timeout - elapsed
            if (timeoutTimeLeft <= ZERO) { // elapsed >= timeout
                executeAction()
            } else {
                postInvoke?.onDebounced(timeoutTimeLeft)
            }
        }
    }

    private fun executeAction() {
        action()
        lastInvokationTime = now()
        postInvoke?.onExecuted()
    }

    private fun elapsed(): Duration? =
        lastInvokationTime?.let {
            (now() - it).nanoseconds
        }

    private fun now(): Long =
        System.nanoTime()
}