Here are some samples of how to use library's API.

## `DebounceStateIndentity.debounce()`

### Inside View, Presenter or ViewModel

```kotlin
val signInButtonState = DebounceStateIdentity()

fun onSignInButtonClick() =
    signInButtonState.debounce {
        yourCoroutineScope.launch {
            authApi.signIn() // suspending call
            navigateToMainScreen()
            releaseIn(600.milliseconds) // or release() if you want release debouncing immediately
        }
    }
```

### Inside [Jetpack Compose](https://developer.android.com/jetpack/compose)

> [!IMPORTANT]
> It is crucial to [`remember()`](https://developer.android.com/jetpack/compose/state) `DebounceStateIdentity()` to preserve debouncing state across recomposition.

> [!NOTE]
> You should also [`remember()`](https://developer.android.com/jetpack/compose/state) `onClick` action due to [unstable lambdas](https://multithreaded.stitchfix.com/blog/2022/08/05/jetpack-compose-recomposition/#:~:text=Gotcha%20%2D%20Unstable%20Lambdas) or use [method reference](https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html).

```kotlin
val coroutineScope = rememberCoroutineScope()
val buttonState = remember { DebounceStateIdentity() }

val onClick: () -> Unit = remember {
    {
        buttonState.debounce {
            coroutineScope.launch {
                animateUi() // suspending call
                release()
            }
        }
    }
}

Button(onClick = onClick) {
    Text("Animate UI")
}
```

## `debounced()`

### Basic:
```kotlin
val onClick = debounced(400.milliseconds) { println("Clicked!") }
onClick() // "Clicked!"
onClick() // debounced
delay(500.milliseconds) // timeout has passed
onClick() // "Clicked!"
onClick() // debounced
```

### Advanced, general callback
```kotlin
val onClick = debounced(
    timeout = 400.milliseconds,
    action = { println("Clicked!") },
    onInvoke = { wasExecuted -> println("Action was executed: $wasExecuted") },
)
onClick() // "Clicked!", "Action was executed: true"
onClick() // debounced, "Action was executed: false"
delay(500.milliseconds) // timeout has passed
onClick() // "Clicked!", "Action was executed: true"
onClick() // debounced, "Action was executed: false"
```

### Advanced, specific callbacks
```kotlin
val onClick = debounced(
    timeout = 400.milliseconds,
    action = { println("Clicked!") },
    onExecuted = { println("Action was executed") },
    onDebounced = { timeLeft -> println("Action was debounced, time left: $timeLeft") },
)
onClick() // "Clicked!", "Action was executed"
onClick() // debounced, "Action was debounced, time left: 399.86ms"
delay(500.milliseconds) // timeout has passed
onClick() // "Clicked!", "Action was executed"
delay(300.milliseconds) // timeout hasn't passed
onClick() // debounced, "Action was debounced, time left: 88.81ms"
```

### Inside [Jetpack Compose](https://developer.android.com/jetpack/compose)

> [!IMPORTANT]
> It is crucial to [`remember()`](https://developer.android.com/jetpack/compose/state) `debounced()` action to preserve debouncing state across recomposition.

```kotlin
val onClick = remember {
    debounced(2.seconds) {
        navigator.navigateToNextScreen()
    }
}
Button(onClick = onClick) {
    Text("Go to next screen")
}
```

### Observing debouncing state
> There is no observable provided by the library out of the box.
> You can implement your own with the technology you're using, like [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines) or [RxJava](https://github.com/ReactiveX/RxJava).
> Following __simplified__ example uses Kotlin Coroutines and Flows.

```kotlin
val timeout = 1.seconds
// 1. create mutable Flow
val isReadyFlow = MutableStateFlow(true)
val action = debounced(
    timeout = timeout,
    onExecuted = {
        // 2. update isReadyFlow
        yourCoroutineScope.launch { isReadyFlow.updateOnExecuted(timeout) }
    },
    action = { println("Action executed!") },
)

// 3. subscribe to value updates
yourCoroutineScope.launch {
    isReadyFlow.collect { isReady ->
        println("isReady is $isReady")
    }
}

// 4. action is ready to be used
action()

/*
 * Once action was executed, we update our Flow with false,
 * meaning that successive calls will be debounced.
 * After timeout used to create a debounced action has passed,
 * we update the Flow with true, meaning that successive call will be executed.
 */
private suspend fun MutableStateFlow<Boolean>.updateOnExecuted(timeout: Duration) {
    value = false
    delay(timeout)
    value = true
}
```