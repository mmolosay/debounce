package io.github.mmolosay.debounce

import io.github.mmolosay.debounce.action.DebouncedAction
import io.github.mmolosay.debounce.action.DebouncedActionImpl
import io.github.mmolosay.debounce.action.PostInvokeActionFactory
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

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

/*
 * Constructor functions for creating debounced actions.
 */

/**
 * Creates new instance of debounced [action].
 *
 * Invoking returned lambda will execute wrapped [action] only if the specified
 * [timeout] is exceeded since the moment of last execution (if there was such).
 *
 * This is the most basic variant of this function. See other overloads for more functionality.
 */
public fun debounced(
    timeout: Duration = 400.milliseconds,
    action: () -> Unit,
): DebouncedAction =
    DebouncedActionImpl(
        timeout = timeout,
        postInvoke = null,
        action = action,
    )

/**
 * Variant of [debounced] function. See basic definition for details.
 *
 * Specified [onInvoke] block will be called after every invocation of returned lambda.
 * It will be invoked with `true` as a parameter, if wrapped [action] was actually executed in
 * this invocation, or with `false` otherwise.
 */
public fun debounced(
    timeout: Duration = 400.milliseconds,
    onInvoke: (Boolean) -> Unit,
    action: () -> Unit,
): DebouncedAction =
    DebouncedActionImpl(
        timeout = timeout,
        postInvoke = PostInvokeActionFactory.make(onInvoke),
        action = action,
    )

/**
 * Variant of [debounced] function. See basic definition for details.
 *
 * This overload separates post invoke action in two [onExecuted] and [onDebounced] actions.
 *
 * [onExecuted] is called, if wrapped [action] was actually executed in this invocation.
 * [onDebounced] is called, if wrapped [action] was debounced and was not executed. Timeout time left
 * will be used as its parameter.
 *
 * @throws IllegalArgumentException if called without specifying at least one post invoke action
 * ([onExecuted] or [onDebounced]).
 */
public fun debounced(
    timeout: Duration = 400.milliseconds,
    onExecuted: (() -> Unit)? = null,
    onDebounced: ((Duration) -> Unit)? = null,
    action: () -> Unit,
): DebouncedAction =
    DebouncedActionImpl(
        timeout = timeout,
        postInvoke = PostInvokeActionFactory.make(onExecuted, onDebounced),
        action = action,
    )
