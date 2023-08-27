package io.github.mmolosay.debounce

import io.github.mmolosay.debounce.identity.DebounceStateIdentity
import kotlin.time.Duration.Companion.seconds
import io.github.mmolosay.debounce.debounce

internal class Main {

    val buttonState = DebounceStateIdentity()

    fun onButtonClick() {
        buttonState.debounce {
            println("doing work")
            release()
            releaseIn(2.seconds)
        }
    }
}