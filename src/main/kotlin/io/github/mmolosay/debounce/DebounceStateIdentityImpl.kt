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
    var timeout: Duration? = null,
    private val now: InstantProducer = InstantProducerFactory.create(),
) : DebounceStateIdentity {

    override val isReady: Boolean
        get() = !hasEnteredDebounce && hasTimeoutPassed()

    var hasEnteredDebounce = false
    private var releaseStartTime: Long? = null

    fun recordReleaseStart() {
        releaseStartTime = now()
    }

    private fun hasTimeoutPassed(): Boolean {
        val timeout = timeout ?: return true // no timeout set means has passed
        val startTime = releaseStartTime ?: return true // never started means has passed
        val elapsed = elapsed(since = startTime, until = now())
        return elapsed >= timeout
    }
}