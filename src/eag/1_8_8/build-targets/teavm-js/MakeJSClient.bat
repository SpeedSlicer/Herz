@echo off
cd ../../
call gradlew build-targets:teavm-js:generateJavaScript
pause
