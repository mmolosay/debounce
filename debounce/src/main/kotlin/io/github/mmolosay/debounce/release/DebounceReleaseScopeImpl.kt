package io.github.mmolosay.debounce.release

import io.github.mmolosay.debounce.identity.DebounceStateIdentityImpl
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

internal class DebounceReleaseScopeImpl(
    private val state: DebounceStateIdentityImpl,
) : DebounceReleaseScope {

    private var wasReleaseCalled = false

    override fun release() =
        release(timeout = null)

    override fun releaseIn(timeout: Duration) =
        release(timeout = timeout)

    private fun release(timeout: Duration?) {
        if (wasReleaseCalled) throw IllegalStateException("multiple release calls are not allowed")
        wasReleaseCalled = true

        state.onRelease(timeout)
    }
}