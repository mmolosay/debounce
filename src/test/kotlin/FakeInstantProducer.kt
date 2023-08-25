import io.github.mmolosay.debounce.time.InstantProducer
import kotlin.time.Duration

class FakeInstantProducer(
    var advancement: Duration = Duration.ZERO,
) : InstantProducer {
    override fun invoke(): Long =
        advancement.inWholeNanoseconds
}

infix fun FakeInstantProducer.advanceBy(amount: Duration) {
    advancement += amount
}