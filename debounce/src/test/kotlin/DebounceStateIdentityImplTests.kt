import io.github.mmolosay.debounce.identity.DebounceStateIdentityImpl
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.ZERO
import kotlin.time.Duration.Companion.milliseconds

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

internal class DebounceStateIdentityImplTests {

    val clock = FakeInstantProducer()
    val state = DebounceStateIdentityImpl(now = clock)

    @Test
    fun `state is ready when created`() {
        state.isReady shouldBe true
    }

    @Test
    fun `on entering debounce, state becomes unready`() {
        state.onEnterDebounce()

        state.isReady shouldBe false
    }

    @Test
    fun `on release without timeout, state becomes ready`() {
        state.onEnterDebounce()

        state.onRelease(timeout = null)

        state.isReady shouldBe true
    }

    @Test
    fun `on release with negative timeout, releases instantly and state becomes ready`() {
        state.onEnterDebounce()

        state.onRelease(-Timeout)
        // no time advancement is needed

        state.isReady shouldBe true
    }

    @Test
    fun `on release with zero timeout, releases instantly and state becomes ready`() {
        state.onEnterDebounce()

        state.onRelease(timeout = ZERO)
        // no time advancement is needed

        state.isReady shouldBe true
    }

    @Test
    fun `on release with timeout, state remains unready if timeout has not passed yet #1`() {
        state.onEnterDebounce()

        state.onRelease(Timeout)
        // we are not advancing time here, so timeout hasn't passed

        state.isReady shouldBe false
    }

    @Test
    fun `on release with timeout, state remains unready if timeout has not passed yet #2`() {
        state.onEnterDebounce()

        state.onRelease(Timeout)
        clock advanceBy LessThanTimeout

        state.isReady shouldBe false
    }

    @Test
    fun `on release with timeout, state becomes ready if timeout has just passed`() {
        state.onEnterDebounce()

        state.onRelease(Timeout)
        clock advanceBy ExactlyTimeout

        state.isReady shouldBe true
    }

    @Test
    fun `on release with timeout, state becomes ready if timeout has already passed`() {
        state.onEnterDebounce()

        state.onRelease(Timeout)
        clock advanceBy MoreThanTimeout

        state.isReady shouldBe true
    }

    private companion object {
        val Timeout = 10.milliseconds
        val ExactlyTimeout = Timeout
        val MoreThanTimeout = Timeout + 1.milliseconds
        val LessThanTimeout = Timeout - 1.milliseconds
    }
}