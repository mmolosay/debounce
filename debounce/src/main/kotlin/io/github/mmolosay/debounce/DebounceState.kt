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
 * A state of the associated component in terms of debouncing.
 */
public interface DebounceState {

    /**
     * The result of the next invocation. Answers the question:
     * _"Is the component ready to be used right now?"_
     *
     * Returns `true` if the component is ready to be used (executed).
     * Returns `false` if an attempt to use (execute) it right now will be debounced.
     */
    public val isReady: Boolean
}