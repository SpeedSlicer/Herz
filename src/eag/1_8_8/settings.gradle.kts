import java.io.File

pluginManagement {
	repositories {
		gradlePluginPortal()
		maven {
			name = "eagler-teavm"
			url = uri("https://eaglercraft-teavm-fork.github.io/maven/")
		}
		maven {
			name = "eagler-local"
			url = uri(File(rootDir, "gradle/local-libs"))
		}
		mavenCentral()
	}
}

rootProject.name = "eaglercraft-1_8-mixins"
include("build-targets:common")
include("build-targets:lwjgl")
include("build-targets:teavm-js")
include("build-targets:teavm-wasm")

includeBuild("eag-1_8")
