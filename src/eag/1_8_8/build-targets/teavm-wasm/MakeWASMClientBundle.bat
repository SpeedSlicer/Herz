@echo off
cd ../../
call gradlew build-targets:teavm-wasm:makeMainWasmClientBundle
pause
