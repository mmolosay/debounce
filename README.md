[![Maven metadata URL](https://img.shields.io/maven-metadata/v?color=blue&metadataUrl=https://s01.oss.sonatype.org/service/local/repo_groups/public/content/io/github/mmolosay/debounce/maven-metadata.xml&style=for-the-badge)](https://search.maven.org/artifact/io.github.mmolosay/debounce)
[![License Apache 2.0](https://img.shields.io/github/license/mmolosay/StringAnnotations.svg?style=for-the-badge&color=orange)](https://opensource.org/licenses/Apache-2.0)
[![kotlin](https://img.shields.io/github/languages/top/mmolosay/StringAnnotations.svg?style=for-the-badge&color=blueviolet)](https://kotlinlang.org/)

# debounce
Debounce your lambdas.

![code snippet](https://user-images.githubusercontent.com/32337243/231155507-d95f3ed1-1f2d-429d-87e5-c6b2ffe21cfc.png)

## Table of Contents

* [What is debouncing?](#what-is-debouncing)
* [Problems to solve](#problems-to-solve)
* [Reasons to use](#reasons-to-use)
* [Installation](#installation)
* [Migration](#migration)
* [Examples of use](#examples-of-use)
* [License](#license)

-----

## What is debouncing?

In programming, "debouncing" refers to a technique used to prevent multiple triggering of an event or action due to rapid or repeated signals.
Specifically, it is a process of filtering out unwanted, extraneous signals or noise from an input signal that can result in multiple events being triggered when only one is desired.

> More here: [article](https://www.techtarget.com/whatis/definition/debouncing)

## Problems to solve

* Prevent multiple clicks on a button from triggering the same action multiple times.
* Delay the processing of text field input until the user has finished typing, to improve performance and prevent unwanted intermediate results, like in search queries.
* Filter out noise and ensure that only valid sensor readings are used in decision-making processes.

## Reasons to use

* Convenient to use.
* Small source code size.
* 100% documented.
* Covered in unit tests.

## Installation

Using [Gradle Kotlin DSL](https://docs.gradle.org/current/userguide/kotlin_dsl.html):
```kotlin
repositories {
    mavenCentral()
}
dependencies {
    implementation("io.github.mmolosay:debounce:VERSION")
}
```
You can find the most recent version at the top of this file in __Maven__ badge.

## Migration

Some versions contain breaking changes.
Check [releases page](https://github.com/mmolosay/debounce/releases) for migration guide.

## Examples of use

Basic:
```kotlin
val onClick = debounced(400.milliseconds) { println("Clicked!") }
onClick() // "Clicked!"
onClick() // debounced
delay(500.milliseconds) // timeout has passed
onClick() // "Clicked!"
onClick() // debounced
```

Advanced, general callback
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

Advanced, specific callbacks
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

Observing debouncing state
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

## License

```text
Copyright 2023 Mikhail Malasai

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
