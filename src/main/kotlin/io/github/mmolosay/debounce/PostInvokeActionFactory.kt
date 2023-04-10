package io.github.mmolosay.debounce

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