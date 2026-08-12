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
tasks.register<JavaExec>("applyCommonMixins") {
    group = "mixins"
    classpath = files(rootDir.resolve("../../../../mixins/dist/mixins-1.0-SNAPSHOT-all.jar"))

    mainClass.set("-jar")

    args(
        file("${rootDir}/../../../mixins/dist/mixins-1.0-SNAPSHOT-all.jar"),
        "arg2",
        "arg3",
        "arg4")
}
