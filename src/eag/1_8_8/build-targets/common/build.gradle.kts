plugins {
    id("java")
}

group = "dev.speedslicer"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val sharedMainJava = file("../../../common/src/main/java")
val sharedPlatformApi = file("../../../common/src/platform-api/java")

sourceSets {
    main {
        java.srcDirs(
            "src/main/java",
            sharedMainJava,
            sharedPlatformApi,
        )
    }
}

dependencies {
    implementation(project(path = ":", configuration = "unprocessedClasses"))
}

val platformApiClassPatterns = fileTree(sharedPlatformApi)
    .matching { include("**/*.java") }
    .files
    .map { sourceFile ->
        val relativePath = sourceFile.relativeTo(sharedPlatformApi)
            .invariantSeparatorsPath
            .removeSuffix(".java")
        "$relativePath*.class"
    }

tasks.named<JavaCompile>("compileJava") {
    outputs.upToDateWhen { false }

    doLast {
        delete(fileTree(destinationDirectory) {
            platformApiClassPatterns.forEach(::include)
        })
    }
}

tasks.withType<Jar> {
    entryCompression = ZipEntryCompression.STORED
    // TeaVM will fail if anything from platform-api is in the JAR

    fileTree(sharedPlatformApi).visit {
        if (!isDirectory) {
            if (path.endsWith(".java")) {
                exclude(path.substring(0, path.length - 5) + ".class")
            }
        }
    }
}
