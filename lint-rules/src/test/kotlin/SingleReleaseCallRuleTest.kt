import io.github.mmolosay.debounce.rules.SingleReleaseCallRule
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.test.lint
import io.kotest.matchers.collections.shouldHaveSize
import org.junit.jupiter.api.Test

class SingleReleaseCallRuleTest {

    @Test
    fun `finds one violation when two release methods called`() {
        val rule = SingleReleaseCallRule(Config.empty)
        val code = """
            import io.github.mmolosay.debounce.debounce
            import io.github.mmolosay.debounce.identity.DebounceStateIdentity
            import kotlin.time.Duration.Companion.seconds

            val buttonState = DebounceStateIdentity()

            fun onButtonClick() =
                buttonState.debounce {
                    println("doing work")
                    release()
                    releaseIn(2.seconds)
                }
        """.trimIndent()

        val findings = rule.lint(code)

        findings shouldHaveSize 1
    }
}