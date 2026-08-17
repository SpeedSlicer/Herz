plugins {
    id("java")
}

group = "dev.speedslicer"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

sourceSets {
    main {
        java.srcDirs(
            "src/main/java",
            "src/platform-api/java",
        )
    }
}

dependencies {
    implementation(project(path = ":", configuration = "unprocessedClasses"))
}

val platformApiClassPatterns = fileTree("src/platform-api/java")
    .matching { include("**/*.java") }
    .files
    .map { sourceFile ->
        val relativePath = sourceFile.relativeTo(file("src/platform-api/java"))
            .invariantSeparatorsPath
            .removeSuffix(".java")
        "$relativePath*.class"
    }

tasks.named<JavaCompile>("compileJava") {
    doLast {
        delete(fileTree(destinationDirectory) {
            platformApiClassPatterns.forEach(::include)
        })
    }
}

tasks.withType<Jar> {
    entryCompression = ZipEntryCompression.STORED
    // TeaVM will fail if anything from platform-api is in the JAR

    fileTree("src/platform-api/java").visit {
        if (!isDirectory) {
            if (path.endsWith(".java")) {
                exclude(path.substring(0, path.length - 5) + ".class")
            }
        }
    }
}
