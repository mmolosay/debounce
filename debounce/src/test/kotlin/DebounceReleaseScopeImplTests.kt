import io.github.mmolosay.debounce.release.DebounceReleaseScope
import io.github.mmolosay.debounce.release.DebounceReleaseScopeImpl
import io.github.mmolosay.debounce.identity.DebounceStateIdentityImpl
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrowAny
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

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

internal class DebounceReleaseScopeImplTests {

    val state: DebounceStateIdentityImpl = mockk(relaxed = true, relaxUnitFun = true) {
        every { isReady } returns true
    }
    val scope: DebounceReleaseScope = DebounceReleaseScopeImpl(state)

    @Test
    fun `release() method notifies state`() {
        scope.release()

        verify { state.onRelease(timeout = null) }
    }

    @Test
    fun `releaseIn() method exits debounce`() {
        val timeout = 2.seconds
        scope.releaseIn(timeout)

        verify { state.onRelease(timeout = timeout) }
    }

    @Test
    fun `calling more than one release method throws exception #1`() {
        scope.releaseIn(2.seconds)

        shouldThrowAny {
            scope.release()
        }
    }

    @Test
    fun `calling more than one release method throws exception #2`() {
        scope.release()

        shouldThrowAny {
            scope.releaseIn(2.seconds)
        }
    }

    @Test
    fun `calling single release method per scope is OK`() {
        shouldNotThrowAny {
            if (Random.nextBoolean()) {
                if (Random.nextBoolean()) {
                    scope.release()
                } else {
                    scope.releaseIn(2.seconds)
                }
            } else {
                scope.release()
            }
        }
    }
}