val repoRoot = rootDir.resolve("..").resolve("..").resolve("..").canonicalFile

plugins {
	id("java-library")
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
	api("net.lenni0451.classtransform:mixinsdummy:1.15.1")
	api("net.lenni0451.classtransform:core:1.15.1")
}


val unprocessedClasses = configurations.create("unprocessedClasses") {
	isCanBeConsumed = true
	isCanBeResolved = false
	extendsFrom(configurations.api.get())
}

artifacts {
	add(unprocessedClasses.name, layout.buildDirectory.dir("classes/java/main")) {
		builtBy(tasks.named("compileJava"))
	}
}

val javaLauncher = javaToolchains.launcherFor {
	languageVersion.set(JavaLanguageVersion.of(17))
}

val syncHerzAssets by tasks.registering(Sync::class) {
	from(file("resources/assets/herz"))
	into(file("eag-1_8/desktopRuntime/resources/assets/herz"))
}

val applyCommonMixins = tasks.register<Exec>("applyCommonMixins") {
	dependsOn(":build-targets:common:classes")

	val javaExecutable = javaLauncher.get().executablePath.asFile

	commandLine(
		javaExecutable,
		"-jar",
		repoRoot.resolve("build-tools/mixins-1.0-SNAPSHOT-all.jar"),

		repoRoot.resolve("src/eag/1_8_8/build/classes/java/main"),
		repoRoot.resolve("src/eag/1_8_8/build-targets/common/build/classes/java/main"),
		repoRoot.resolve("src/eag/1_8_8/build-targets/common/build/resources/main/mixin-targets.json"),
		repoRoot.resolve("src/eag/1_8_8/build/classes/java/main")
	)
}


tasks.withType<Jar> {
	dependsOn(syncHerzAssets)
	dependsOn(applyCommonMixins)

	entryCompression = ZipEntryCompression.STORED

	// TeaVM will fail if anything from platform-api is in the JAR
	fileTree("eag-1_8/src/platform-api/java").visit {
		if (!isDirectory && path.endsWith(".java")) {
			exclude(path.substring(0, path.length - 5) + ".class")
		}
	}
}
