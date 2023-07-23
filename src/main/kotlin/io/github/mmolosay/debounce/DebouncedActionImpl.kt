package io.github.mmolosay.debounce

import kotlin.time.Duration
import kotlin.time.Duration.Companion.ZERO
import kotlin.time.Duration.Companion.nanoseconds

/*
 * Copyright 2023 Mikhail Malasai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Wrapper for some [action], that stands as a proxy to its invocation and debounces it.
 *
 * Original [action]'s invocation will be [debounced](https://en.wiktionary.org/wiki/debounce), if previous
 * successful invocation happened within the specified [timeout] from now.
 * It means, that attempts to invoke [action] before [timeout] had exceeded will be discarded.
 *
 * Additionally, there's a functionality of specifying [postInvoke] callback, that will be called after an attempt to
 * invoke [action].
 */
internal class DebouncedActionImpl(
    private val timeout: Duration,
    private val now: () -> Long = { System.nanoTime() },
    private val postInvoke: PostInvokeAction? = null,
    private val action: () -> Unit,
) : DebouncedAction {

    init {
        require(timeout.isPositive()) { "Can\'t create \'DebouncedAction\' with non-positive \'timeout\'" }
    }

    override val isReady: Boolean
        get() = elapsed()?.let { it - timeout >= ZERO } ?: true

    private var lastInvocationTime: Long? = null

    override fun invoke() {
        val elapsed = elapsed()
        if (elapsed == null) { // very first invocation
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
        lastInvocationTime = now()
        postInvoke?.onExecuted()
    }

    private fun elapsed(): Duration? =
        lastInvocationTime?.let {
            (now() - it).nanoseconds
        }
}