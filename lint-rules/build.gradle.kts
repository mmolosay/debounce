plugins {
    kotlin("jvm")
    id("org.gradle.java-library")
    id("org.gradle.maven-publish")
}

dependencies {
    val detektVersion = "1.21.0" // https://detekt.dev/docs/introduction/compatibility

    compileOnly("io.gitlab.arturbosch.detekt:detekt-api:$detektVersion")

    testImplementation("io.gitlab.arturbosch.detekt:detekt-test:$detektVersion")
    testImplementation("io.kotest:kotest-assertions-core:5.5.5")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8

    withJavadocJar()
    withSourcesJar()
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}