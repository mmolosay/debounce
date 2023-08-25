import io.github.mmolosay.debounce.DebounceReleaseScope
import io.github.mmolosay.debounce.DebounceReleaseScopeImpl
import io.github.mmolosay.debounce.DebounceStateIdentityImpl
import io.kotest.matchers.shouldBe
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

internal class DebounceReleaseScopeImplTests {

    val state = DebounceStateIdentityImpl()
    val scope: DebounceReleaseScope = DebounceReleaseScopeImpl(state)

    @Test
    fun `release() method exits debounce`() {
        scope.release()

        state.hasEnteredDebounce shouldBe false
    }

    @Test
    fun `releaseIn() method exits debounce`() {
        scope.release()

        state.hasEnteredDebounce shouldBe false
    }
}