import io.github.mmolosay.debounce.DebounceStateIdentityImpl
import io.github.mmolosay.debounce.debounce
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

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

internal class DebounceTests {

    val state: DebounceStateIdentityImpl = mockk(relaxed = true, relaxUnitFun = true)

    @Test
    fun `debounce() executes action if state is ready`() {
        var wasActionExecuted = false
        every { state.isReady } returns true

        state.debounce { wasActionExecuted = true }

        wasActionExecuted shouldBe true
    }

    @Test
    fun `debounce() does not execute action if state is not ready`() {
        var wasActionExecuted = false
        every { state.isReady } returns false

        state.debounce { wasActionExecuted = true }

        wasActionExecuted shouldBe false
    }

    @Test
    fun `debounce() returns true if state is ready`() {
        every { state.isReady } returns true

        val result = state.debounce { /* executed */ }

        result shouldBe true
    }

    @Test
    fun `debounce() returns false if state is not ready`() {
        every { state.isReady } returns false

        val result = state.debounce { /* debounced */ }

        result shouldBe false
    }
}