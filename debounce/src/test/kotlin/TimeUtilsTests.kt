import io.github.mmolosay.debounce.time.TimeUtils.elapsed
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.nanoseconds

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

class TimeUtilsTests {

    @Test
    fun `elapsed 15 nanoseconds`() {
        val elapsed = elapsed(since = 0, until = 15)

        elapsed shouldBe 15.nanoseconds
    }

    @Test
    fun `elapsed 22 milliseconds`() {
        val elapsed = elapsed(since = 0, until = 22 * NanosecondsInMillisecond)

        elapsed shouldBe 22.milliseconds
    }

    @Test
    fun `elapsed -8 milliseconds`() {
        val elapsed = elapsed(since = 8 * NanosecondsInMillisecond, until = 0)

        elapsed shouldBe (-8).milliseconds
    }

    private companion object {
        const val NanosecondsInMillisecond = 1_000_000L
    }
}