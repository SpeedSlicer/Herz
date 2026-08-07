plugins {
    java
}

val mixinT = project(":mixins")

val mixinTSourceSets = mixinT.extensions.getByType<SourceSetContainer>()

val mixinsTClasspath = mixinTSourceSets["main"].runtimeClasspath



group = "net.ada"
version = "1.0-SNAPSHOT"

sourceSets {
    main {
        java.srcDir("src/common/java")
    }
}

repositories {
    mavenCentral()
}
dependencies {
    "compileOnly"("net.lenni0451.classtransform:core:1.15.1")
    "compileOnly"(project(":src:test:test-version:test-inner"))
}

tasks.register<JavaExec>("transformClasses") {
    group="mixins"
    dependsOn("build")
    dependsOn(":mixins:classes")

    classpath = mixinsTClasspath

    mainClass.set("net.ada.mixins.transformer.Main")
    args(
        rootDir.resolve("src/test/test-version/test-inner/build/classes/java/main").absolutePath,
        rootDir.resolve("src/test/test-version/build/classes/java/main").absolutePath,
        rootDir.resolve("src/test/test-version/build/out/java/main").absolutePath
    )

}