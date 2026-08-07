plugins {
    id("java")
    id("application")

}

group = "dev.speedslicer"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
}

application {
    mainClass.set("dev.speedslicer.Main")
}