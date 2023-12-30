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
* [What to use](#what-to-use)
* [Migration](#migration)
* [💡Examples](#examples)
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

## What to use
The centerpieces of this library are two functions:
 - [`debounced()`](/debounce/src/main/kotlin/io/github/mmolosay/debounce/Debounced.kt)
 - [`DebounceStateIdentity.debounce()`](/debounce/src/main/kotlin/io/github/mmolosay/debounce/Debounce.kt)

The main difference between them is the moment when debouncing timeout starts.

For `debounced()` timeout starts right after an execution of the action.
It suits for cases without any lasting async work. 

For `DebounceStateIdentity.debounce()` it is you who decide when to start a release timeout.
It's a right choice for actions which perform lasting async work.

> [!NOTE]
> Q: Does your action contain lasting async work?
> 
> A: NO → [`debounced()`](/debounce/src/main/kotlin/io/github/mmolosay/debounce/Debounced.kt)
> 
> A: YES → [`DebounceStateIdentity.debounce()`](/debounce/src/main/kotlin/io/github/mmolosay/debounce/Debounce.kt)

For examples of use see [Examples](#examples) section down below.

## Migration

Some versions contain breaking changes.
Check [releases page](https://github.com/mmolosay/debounce/releases) for migration guide.

## 💡Examples

Examples can be found [here](SAMPLES.md).

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
