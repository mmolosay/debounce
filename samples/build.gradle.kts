plugins {
    kotlin("jvm")
}

dependencies {
    /*
     * You should use
     * implementation("io.github.mmolosay:debounce:VERSION")
     */
    implementation(project(":debounce"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}