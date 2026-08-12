plugins {
    id("java")
}

group = "dev.speedslicer"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(rootProject)
    implementation("net.lenni0451.classtransform:mixinsdummy:1.15.1")
}

