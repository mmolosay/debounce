plugins {
    kotlin("jvm")
    id("org.gradle.java-library")
    id("org.gradle.maven-publish")
}

dependencies {
    val detektVersion = "1.21.0" // https://detekt.dev/docs/introduction/compatibility

    compileOnly("io.gitlab.arturbosch.detekt:detekt-api:$detektVersion")

    testImplementation("io.gitlab.arturbosch.detekt:detekt-test:$detektVersion")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}