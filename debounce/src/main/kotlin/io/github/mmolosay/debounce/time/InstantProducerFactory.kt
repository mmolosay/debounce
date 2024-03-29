package io.github.mmolosay.debounce.time

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
 * Produces instants of "now" in a format of Epoch time in nanoseconds.
 */
internal fun interface InstantProducer {
    operator fun invoke(): Long
}

internal object InstantProducerFactory {

    fun create() =
        InstantProducer {
            System.nanoTime()
        }
}