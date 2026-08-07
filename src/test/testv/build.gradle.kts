plugins {
    java
}

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
    "compileOnly"(project(":src:test:testv:test-inner"))
}