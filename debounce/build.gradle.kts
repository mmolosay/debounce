import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

// region Plugins & dependencies

plugins {
    kotlin("jvm")
    id("org.gradle.java-library")
    id("org.gradle.maven-publish")
    id("org.gradle.signing")
}

dependencies {
    testImplementation("io.mockk:mockk:1.13.2") // more recent versions are incompatible
    testImplementation("io.kotest:kotest-assertions-core:5.5.5")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

// endregion

// region Publishing & signing

/*
 * 1. update version at the top
 * 2. apply Gradle changes
 * 3. ./gradlew publish
 * 4. https://s01.oss.sonatype.org/#stagingRepositories
 * 5. Close
 * 6. Release
 */

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "debounce"
            from(components["java"])
            pom {
                name.set("debounce")
                description.set("Debounce your lambdas.")
                url.set("https://github.com/mmolosay/debounce")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("mmolosay")
                        name.set("Misha Malasai")
                        email.set("mmolosays@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/mmolosay/debounce.git")
                    developerConnection.set("scm:git:ssh://github.com/mmolosay/debounce.git")
                    url.set("https://github.com/mmolosay/debounce")
                }
            }
        }
    }
    repositories {
        maven {
            name = "MavenCentral"
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = properties["ossrhUsername"] as String
                password = properties["ossrhPassword"] as String
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}


// endregion

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8

    withJavadocJar()
    withSourcesJar()
}

kotlin {
    explicitApi = ExplicitApiMode.Strict
}

tasks.test {
    useJUnitPlatform()
}