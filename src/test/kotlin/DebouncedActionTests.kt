import io.github.mmolosay.debounce.DebouncedAction
import io.github.mmolosay.debounce.PostInvokeActionFactory
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import kotlin.time.Duration
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

class DebouncedActionTests {

    // region Creation

    @Test
    fun `creating debounced action with negative timeout throws exception`() {
        val timeout = (-20).milliseconds
        try {
            DebouncedAction(timeout) {}
        } catch (_: IllegalArgumentException) {
            // passed
        }
    }

    @Test
    fun `creating debounced action with zero timeout throws exception`() {
        val timeout = Duration.ZERO
        try {
            DebouncedAction(timeout) {}
        } catch (_: IllegalArgumentException) {
            // passed
        }
    }

    // endregion

    // region Invocation

    @Test
    fun `when debounced just created, invocation executes action`() {
        var wasActionExecuted = false
        val debounced = DebouncedAction(20.milliseconds) { wasActionExecuted = true }

        debounced()

        wasActionExecuted shouldBe true
    }

    @Test
    fun `when timeout has passed exactly, invocation executes action`() {
        var wasActionExecuted: Boolean
        val clock = MutableClock()
        val timeout = 20.milliseconds
        val debounced = DebouncedAction(timeout, clock) { wasActionExecuted = true }

        debounced() // first call, now timeout has started
        wasActionExecuted = false // reset
        clock advanceBy timeout
        debounced()

        wasActionExecuted shouldBe true
    }

    @Test
    fun `when timeout has passed greater, invocation executes action`() {
        var wasActionExecuted: Boolean
        val clock = MutableClock()
        val timeout = 20.milliseconds
        val debounced = DebouncedAction(timeout, clock) { wasActionExecuted = true }

        debounced() // first call, now timeout has started
        wasActionExecuted = false // reset
        clock advanceBy timeout + 1.milliseconds
        debounced()

        wasActionExecuted shouldBe true
    }

    @Test
    fun `when timeout has not passed, invocation debounces action`() {
        var wasActionExecuted: Boolean
        val clock = MutableClock()
        val timeout = 20.milliseconds
        val debounced = DebouncedAction(timeout, clock) { wasActionExecuted = true }

        debounced() // first call, now timeout has started
        wasActionExecuted = false // reset
        clock advanceBy (timeout - 1.milliseconds)
        debounced()

        wasActionExecuted shouldBe false
    }

    // endregion

    // region Post invoke, generic

    @Test
    fun `after invocation, post invoke action invoked`() {
        var wasPostInvokeCalled = false
        val debounced = DebouncedAction(
            timeout = 20.milliseconds,
            postInvoke = PostInvokeActionFactory.make { wasPostInvokeCalled = true },
            action = {},
        )

        debounced()

        wasPostInvokeCalled shouldBe true
    }

    @Test
    fun `after action was executed, post invoke action is invoked with wasExecuted=true`() {
        var postInvokeParam: Boolean? = null
        val debounced = DebouncedAction(
            timeout = 20.milliseconds,
            postInvoke = PostInvokeActionFactory.make { wasExecuted -> postInvokeParam = wasExecuted },
            action = {},
        )

        debounced() // we know from other tests that it would indeed execute action

        postInvokeParam shouldBe true
    }

    @Test
    fun `after action was debounced, post invoke action is invoked with wasExecuted=false`() {
        var postInvokeParam: Boolean? = null
        val clock = MutableClock()
        val timeout = 20.milliseconds
        val debounced = DebouncedAction(
            timeout = timeout,
            clock = clock,
            postInvoke = PostInvokeActionFactory.make { wasExecuted -> postInvokeParam = wasExecuted },
            action = {},
        )

        debounced() // first call, now timeout has started
        clock advanceBy (timeout - 1.milliseconds)
        debounced() // we know from other tests that it would indeed debounce action

        postInvokeParam shouldBe false
    }

    // endregion

    // region Post invoke, specific

    // endregion

    // region Utils

    private class MutableClock(
        private val instant: Instant = Instant.now(),
    ) : Clock() {

        var advancement: Duration = Duration.ZERO

        override fun getZone(): ZoneId =
            ZoneId.systemDefault()

        override fun withZone(zone: ZoneId?): Clock =
            this // insensible

        override fun instant(): Instant =
            instant + java.time.Duration.ofNanos(advancement.inWholeNanoseconds)
    }

    private infix fun MutableClock.advanceBy(amount: Duration) {
        advancement += amount
    }

    // endregion
}