package io.github.mmolosay.debounce.lib

import kotlin.time.Duration

internal sealed interface PostInvokeAction {

    interface General : PostInvokeAction {
        operator fun invoke(wasExecuted: Boolean)
    }

    interface Specific : PostInvokeAction {
        fun onExecuted()
        fun onDebounced(timeoutTimeLeft: Duration)
    }
}

internal fun PostInvokeAction.onExecuted() =
    when (this) {
        is PostInvokeAction.General -> invoke(wasExecuted = true)
        is PostInvokeAction.Specific -> onExecuted()
    }

internal fun PostInvokeAction.onDebounced(timeoutTimeLeft: Duration) =
    when (this) {
        is PostInvokeAction.General -> invoke(wasExecuted = false)
        is PostInvokeAction.Specific -> onDebounced(timeoutTimeLeft)
    }