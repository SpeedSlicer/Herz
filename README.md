# Herz
A Client base for Eaglercraft Clients. Currently supporting Mixins, with features on the way. 

Currently Support:
- Mixins

## Gradle layout

- `src/eag/common` contains platform-independent Herz code and compile-time platform stubs.
- `src/eag/1_8_8` is the version build.
- `src/eag/1_8_8/build-targets` contains the common mixin, LWJGL, TeaVM JavaScript, and TeaVM WASM targets.
- `src/eag/1_8_8/eag-1_8` is included by the version build; it should not be included separately by the repository build.

Run `./gradlew build` from the repository root to build the root projects and every 1.8.8 platform jar. Run `./gradlew buildAllPlatforms` from `src/eag/1_8_8` when working only on the game client.
