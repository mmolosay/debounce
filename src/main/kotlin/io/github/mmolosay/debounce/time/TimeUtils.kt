package io.github.mmolosay.debounce.time

import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds

internal object TimeUtils {

    /**
     * Calculates time amount elapsed starting from [since] moment and up to [until] moment with nanoseconds precision.
     *
     * @param since time in nanoseconds since which elapsed time is calculated.
     * @param until epoch time in nanoseconds until which elapsed time is calculated.
     */
    fun elapsed(since: Long, until: Long): Duration =
        (until - since).nanoseconds
}