package io.github.mmolosay.debounce.lib

import kotlin.time.Duration

internal object PostInvokeActionFactory {

    fun make(
        onInvoked: (Boolean) -> Unit,
    ): PostInvokeAction =
        object : PostInvokeAction.General {
            override fun invoke(wasExecuted: Boolean) =
                onInvoked(wasExecuted)
        }

    fun make(
        onExecuted: (() -> Unit)?,
        onDebounced: ((Duration) -> Unit)?,
    ): PostInvokeAction {
        require(onExecuted != null || onDebounced != null) {
            "You must specify \'onExecuted\' or/and \'onDebounced\' action"
        }
        return object : PostInvokeAction.Specific {

            override fun onExecuted() {
                onExecuted?.invoke()
            }

            override fun onDebounced(timeoutTimeLeft: Duration) {
                onDebounced?.invoke(timeoutTimeLeft)
            }
        }
    }
}