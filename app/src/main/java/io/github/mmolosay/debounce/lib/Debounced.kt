package io.github.mmolosay.debounce.lib

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * Creates new instance of debounced [action].
 *
 * Invoking returned lambda will execute wrapped [action] only if the specified
 * [timeout] is exceeded since the moment of last execution (if there was such).
 *
 * This is the most basic variant of this function. See other overloads for more functionality.
 */
fun debounced(
    timeout: Duration = 400.milliseconds,
    action: () -> Unit,
): () -> Unit =
    DebouncedAction(
        timeout = timeout,
        action = action,
        postInvoke = null,
    )

/**
 * Variant of [debounced] function. See basic definition for details.
 *
 * Specified [postInvoke] block will be called after every invokation of returned lambda.
 * It will be invoked with `true` as a parameter, if wrapped [action] was actually executed in
 * this invokation, or with `false` otherwise.
 */
fun debounced(
    timeout: Duration = 400.milliseconds,
    postInvoke: (Boolean) -> Unit,
    action: () -> Unit,
): () -> Unit =
    DebouncedAction(
        timeout = timeout,
        action = action,
        postInvoke = postInvoke,
    )

/**
 * Variant of [debounced] function. See basic definition for details.
 *
 * This overload separates post invoke action in two [onExecuted] and [onDebounced] actions.
 * [onExecuted] is called, if wrapped [action] was actually executed in this invokation.
 * [onDebounced] is called, if wrapped [action] was debounced and was not executed.
 *
 * @throws IllegalArgumentException if called without specifying at least one post invoke action
 * ([onExecuted] or [onDebounced]).
 */
fun debounced(
    timeout: Duration = 400.milliseconds,
    onExecuted: (() -> Unit)? = null,
    onDebounced: (() -> Unit)? = null,
    action: () -> Unit,
): () -> Unit =
    DebouncedAction(
        timeout = timeout,
        action = action,
        postInvoke = makePostInvokeAction(onExecuted, onDebounced),
    )

private fun makePostInvokeAction(
    onExecuted: (() -> Unit)? = null,
    onDebounced: (() -> Unit)? = null,
): (Boolean) -> Unit {
    require(onExecuted != null || onDebounced != null) {
        "You must specify \'onExecuted\' or \'onDebounced\' action"
    }
    return { wasInvoked ->
        if (wasInvoked) onExecuted?.invoke()
        else onDebounced?.invoke()
    }
}