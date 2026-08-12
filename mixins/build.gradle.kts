plugins {
    java
    application
    id("com.gradleup.shadow") version "9.6.1"
}

group = "net.ada"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.lenni0451.classtransform:core:1.15.1")
    implementation("net.lenni0451.classtransform:mixinstranslator:1.15.1")
    implementation("net.lenni0451.classtransform:mixinsdummy:1.15.1")
    implementation("net.lenni0451.classtransform:additionalclassprovider:1.15.1")
    implementation("com.google.code.gson:gson:2.13.2")
}

application {
    mainClass.set("net.ada.transformer.Main")
}

tasks.register<JavaExec>("generateExample") {
    group="example"
    dependsOn("build")

    classpath = sourceSets["main"].runtimeClasspath

    mainClass.set("net.ada.mixins.example.ExampleGenerator")
    args(
        "--example",
        rootDir.resolve("example/manifest/mixin.json").absolutePath
    )
}