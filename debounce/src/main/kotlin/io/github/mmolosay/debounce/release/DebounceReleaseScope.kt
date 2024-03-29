package io.github.mmolosay.debounce.release

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
 * Receiver scope for an action being debounced.
 * Offers ways to release debouncing.
 *
 * Only __one `release` method__ can be called per scope.
 * An attempt to release multiple times will result in [IllegalStateException].
 */
public interface DebounceReleaseScope {

    /**
     * Releases debouncing instantly.
     *
     * @throws [IllegalStateException] if any `release` method was already called.
     */
    public fun release()

    /**
     * Releases debouncing in specified [timeout].
     *
     * If [timeout] is not positive then the method will behave as [release].
     *
     * @throws [IllegalStateException] if any `release` method was already called.
     */
    public fun releaseIn(timeout: Duration)
}