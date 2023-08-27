import io.github.mmolosay.debounce.debounce
import io.github.mmolosay.debounce.identity.DebounceStateIdentity
import kotlin.time.Duration.Companion.seconds

class Main {

    val buttonState = DebounceStateIdentity()

    fun onButtonClick() =
        buttonState.debounce {
            println() // do work
            release()
            releaseIn(2.seconds)
        }
}