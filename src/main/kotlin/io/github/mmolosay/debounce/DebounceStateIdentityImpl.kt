package io.github.mmolosay.debounce

import io.github.mmolosay.debounce.time.InstantProducer
import io.github.mmolosay.debounce.time.InstantProducerFactory
import io.github.mmolosay.debounce.time.TimeUtils.elapsed
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

internal class DebounceStateIdentityImpl(
    private val now: InstantProducer = InstantProducerFactory.create(),
) : DebounceStateIdentity {

    override val isReady: Boolean // whether it is released or not
        get() = wasReleaseCalled ?: true && hasTimeoutPassed()

    private var timeout: Duration? = null // exposing mutable fields makes unit testing hard
    private var wasReleaseCalled: Boolean? = null
    private var releaseTimeoutStartTime: Long? = null

    fun enterReleaseScope() {
        wasReleaseCalled = false
    }

    fun leaveReleaseScope() {
        wasReleaseCalled = true
    }

    fun recordReleaseStart() {
        releaseTimeoutStartTime = now()
    }

    fun setTimeout(timeout: Duration?) {
        this.timeout = timeout
    }

    private fun hasTimeoutPassed(): Boolean {
        val timeout = timeout ?: return true // no timeout set means has passed
        val startTime = releaseTimeoutStartTime ?: return true // never started means has passed
        val elapsed = elapsed(since = startTime, until = now())
        return elapsed >= timeout
    }
}