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
 * Specified [onInvoke] block will be called after every invokation of returned lambda.
 * It will be invoked with `true` as a parameter, if wrapped [action] was actually executed in
 * this invokation, or with `false` otherwise.
 */
fun debounced(
    timeout: Duration = 400.milliseconds,
    onInvoke: (Boolean) -> Unit,
    action: () -> Unit,
): () -> Unit =
    DebouncedAction(
        timeout = timeout,
        action = action,
        postInvoke = PostInvokeActionFactory.make(onInvoke),
    )

/**
 * Variant of [debounced] function. See basic definition for details.
 *
 * This overload separates post invoke action in two [onExecuted] and [onDebounced] actions.
 *
 * [onExecuted] is called, if wrapped [action] was actually executed in this invokation.
 * [onDebounced] is called, if wrapped [action] was debounced and was not executed. Timeout time left
 * will be used as its parameter.
 *
 * @throws IllegalArgumentException if called without specifying at least one post invoke action
 * ([onExecuted] or [onDebounced]).
 */
fun debounced(
    timeout: Duration = 400.milliseconds,
    onExecuted: (() -> Unit)? = null,
    onDebounced: ((Duration) -> Unit)? = null,
    action: () -> Unit,
): () -> Unit =
    DebouncedAction(
        timeout = timeout,
        action = action,
        postInvoke = PostInvokeActionFactory.make(onExecuted, onDebounced),
    )
