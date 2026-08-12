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
			"buildSource/src/main/java",
			"buildSource/src/game/java",
			"buildSource/src/protocol-game/java",
			"buildSource/src/protocol-relay/java",
			"buildSource/src/platform-api/java"
		)
	}
}

dependencies {
	implementation(libs.bundles.common)
}

tasks.withType<Jar> {
	entryCompression = ZipEntryCompression.STORED
	// TeaVM will fail if anything from platform-api is in the JAR
	fileTree("buildSource/src/platform-api/java").visit {
		if (!isDirectory) {
			if (path.endsWith(".java")) {
				exclude(path.substring(0, path.length - 5) + ".class")
			}
		}
	}
}