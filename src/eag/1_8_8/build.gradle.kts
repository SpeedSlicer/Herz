plugins {
	id("java")
}

allprojects {
	repositories {
		mavenCentral()
	}

	plugins.withId("java") {
		java {
			toolchain {
				languageVersion = JavaLanguageVersion.of(17)
			}
		}
	}
}

java {
	sourceCompatibility = JavaVersion.VERSION_11
	targetCompatibility = JavaVersion.VERSION_11
}

sourceSets {
	named("main") {
		java.srcDirs(
			"eag-1_8/src/main/java",
			"eag-1_8/src/game/java",
			"eag-1_8/src/protocol-game/java",
			"eag-1_8/src/protocol-relay/java",
			"eag-1_8/src/platform-api/java"
		)
	}
}

dependencies {
	implementation(libs.bundles.common)
	implementation("net.lenni0451.classtransform:mixinsdummy:1.15.1")
}

tasks.withType<Jar> {
	entryCompression = ZipEntryCompression.STORED
	// TeaVM will fail if anything from platform-api is in the JAR
	fileTree("eag-1_8/src/platform-api/java").visit {
		if (!isDirectory) {
			if (path.endsWith(".java")) {
				exclude(path.substring(0, path.length - 5) + ".class")
			}
		}
	}
}