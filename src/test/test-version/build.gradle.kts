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
    "compileOnly"(project(":src:test:test-version:test-inner"))
    "compileOnly"(("net.lenni0451.classtransform:mixinstranslator:1.15.1"))
    "compileOnly"(("net.lenni0451.classtransform:mixinsdummy:1.15.1"))
    "compileOnly"(("net.lenni0451.classtransform:additionalclassprovider:1.15.1"))
}
