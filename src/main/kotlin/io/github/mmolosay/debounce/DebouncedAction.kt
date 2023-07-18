package io.github.mmolosay.debounce

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
 * An action `() -> Unit` that may be debounced on invocation.
 */
interface DebouncedAction : () -> Unit {

    /**
     * The result of the next invocation. Answers to the question:
     * "Will this action be actually executed if I invoke it right now?".
     *
     * Returns `true` if the next invocation will execute action.
     * Returns `false` if invocation launched right now will be debounced and action will not be executed.
     */
    val isReady: Boolean
}