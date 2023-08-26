package io.github.mmolosay.debounce.action

import kotlin.time.Duration

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
 * Describes an action to be executed after (post-) an invocation of some other, main action.
 */
internal sealed interface PostInvokeAction {

    /**
     * Type of [PostInvokeAction], that have a single callback method for both cases of main action
     * invocation (executed and debounced).
     */
    interface General : PostInvokeAction {
        operator fun invoke(wasExecuted: Boolean)
    }

    /**
     * Type of [PostInvokeAction], that have individual callback methods for cases of main action
     * invocation (executed and debounced).
     */
    interface Specific : PostInvokeAction {
        fun onExecuted()
        fun onDebounced(timeoutTimeLeft: Duration)
    }
}

/**
 * Invokes receiver [PostInvokeAction]'s callback, that stands for a result of successful invocation of main action.
 */
internal fun PostInvokeAction.onExecuted() =
    when (this) {
        is PostInvokeAction.General -> invoke(wasExecuted = true)
        is PostInvokeAction.Specific -> onExecuted()
    }

/**
 * Invokes receiver [PostInvokeAction]'s callback, that stands for a result of unsuccessful invocation of main action.
 */
internal fun PostInvokeAction.onDebounced(timeoutTimeLeft: Duration) =
    when (this) {
        is PostInvokeAction.General -> invoke(wasExecuted = false)
        is PostInvokeAction.Specific -> onDebounced(timeoutTimeLeft)
    }