package io.github.mmolosay.debounce.identity

import io.github.mmolosay.debounce.DebounceState

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
 * A [DebounceState] that is associated with a particular component that is being debounced.
 */
public sealed interface DebounceStateIdentity : DebounceState

/**
 * Creates new [DebounceStateIdentity].
 *
 * Every component you intend to debounce (e.g. button) should have a unique [DebounceStateIdentity]
 * created for it.
 */
public fun DebounceStateIdentity(): DebounceStateIdentity =
    DebounceStateIdentityImpl()