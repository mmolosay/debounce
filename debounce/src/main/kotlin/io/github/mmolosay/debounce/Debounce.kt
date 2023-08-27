package io.github.mmolosay.debounce

import io.github.mmolosay.debounce.identity.DebounceStateIdentity
import io.github.mmolosay.debounce.identity.DebounceStateIdentityImpl
import io.github.mmolosay.debounce.release.DebounceReleaseScope
import io.github.mmolosay.debounce.release.DebounceReleaseScopeImpl

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
 * Executes specified [action], debouncing subsequent [debounce] calls on this [DebounceStateIdentity]
 * until released.
 *
 * Provided [DebounceReleaseScope] must be used inside the [action] to `release` debouncing.
 * Otherwise, subsequent calls will debounce [action] forever.
 *
 * Release should happen when an async work of [action] is finished.
 * For instance, if [action] executes a network call, then release should be called when
 * it is done.
 *
 * @return `true` if [action] was actually executed, `false` if it was debounced.
 * @see DebounceReleaseScope
 */
public fun DebounceStateIdentity.debounce(
    action: DebounceReleaseScope.() -> Unit,
): Boolean {
    require(this is DebounceStateIdentityImpl) { "debounce() is only allowed on DebounceReferenceImpl instances" }
    return debounce(
        action = action,
    )
}

private fun DebounceStateIdentityImpl.debounce(
    action: DebounceReleaseScope.() -> Unit,
): Boolean {
    if (!isReady) return false // debounced
    onEnterDebounce()
    DebounceReleaseScopeImpl(this).action()
    return true
}


