# debounce
Debounce your lambdas.

## Table of Contents

* [What is debouncing?](#what-is-debouncing)
* [Problems to solve](#problems-to-solve)
* [Reasons to use](#reasons-to-use)
* [Installation](#installation)
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

## Examples of use

Basic:
``` kotlin
val onClick = debounced(400.milliseconds) { println("Clicked!") }
onClick() // "Clicked!"
onClick() // debounced
delay(500.milliseconds) // timeout has passed
onClick() // "Clicked!"
onClick() // debounced
```

Advanced, general callback
``` kotlin
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
``` kotlin
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
