plugins {
    java
}

group = "net.ada"
version = "1.0-SNAPSHOT"

sourceSets {
    main {
        java.srcDir("src/common/java")
        java.srcDir("src/mixins/java")
    }
}

repositories {
    mavenCentral()
}