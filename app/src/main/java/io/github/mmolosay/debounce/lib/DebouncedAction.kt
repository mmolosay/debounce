package io.github.mmolosay.debounce.lib

import androidx.compose.runtime.Stable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

private typealias Action = () -> Unit
private typealias PostInvokeAction = (Boolean) -> Unit

@Stable
internal class DebouncedAction internal constructor(
    val timeout: Duration,
    val action: Action,
    private val postInvoke: PostInvokeAction? = null,
) : Action {

    private var lastInvokedTime: Long? = null

    override fun invoke() {
        val shouldExecute = hadTimeoutPassed() // and wasExecuted
        if (shouldExecute) {
            action()
            lastInvokedTime = now()
        }
        postInvoke?.invoke(shouldExecute)
    }

    private fun hadTimeoutPassed(): Boolean =
        lastInvokedTime?.let {
            val elapsed = now() - it
            elapsed.milliseconds >= timeout
        } ?: true

    private fun now(): Long =
        System.currentTimeMillis()
}

/**
 * Creates new instance of debounced [action].
 *
 * Invoking returned lambda will execute wrapped [action] only if the specified
 * [timeout] is exceeded since the moment of last execution (if there was such).
 *
 * Specified [postInvoke] block will be called after every invokation of returned lambda.
 * It will be invoked with `true` as a parameter, if wrapped [action] was actually executed in
 * this invokation, or with `false`, if it was not.
 *
 * This is the most basic variant of this function. See other overloads for more functionality.
 */
fun debounced(
    timeout: Duration = 400.milliseconds,
    postInvoke: ((Boolean) -> Unit)? = null,
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
 */
fun debounced(
    timeout: Duration = 400.milliseconds,
    onExecuted: () -> Unit,
    onDebounced: (() -> Unit)? = null,
    action: () -> Unit,
): () -> Unit =
    DebouncedAction(
        timeout = timeout,
        action = action,
        postInvoke = makePostInvokeAction(onExecuted, onDebounced)
    )

//@Composable
//private fun debounced(debounced: DebouncedAction): Action =
//    remember(debounced.timeout, debounced.action) { debounced }

private fun makePostInvokeAction(
    onExecuted: Action,
    onDebounced: Action? = null,
): PostInvokeAction =
    { wasInvoked -> if (wasInvoked) onExecuted() else onDebounced?.invoke() }