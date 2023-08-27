allprojects {
    group = "io.github.mmolosay"
    version = "1.2.0"
}

plugins {
    kotlin("jvm") version "1.6.21" apply false
    id("io.gitlab.arturbosch.detekt") version "1.21.0" apply false
}