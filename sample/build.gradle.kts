plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":debounce"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}