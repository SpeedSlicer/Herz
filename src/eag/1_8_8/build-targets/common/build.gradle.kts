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

