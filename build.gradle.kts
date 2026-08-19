plugins {
    id("java")
}

group = "dev.speedslicer"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val buildEagler188 = tasks.register("buildEagler188") {
    group = "build"
    description = "Builds the Eaglercraft 1.8.8 client and all platform jars."
    dependsOn(gradle.includedBuild("1_8_8").task(":buildAllPlatforms"))
}

tasks.named("build") {
    dependsOn(buildEagler188)
}

