@echo off
cd ../../
call gradlew build-targets:teavm-js:makeMainOfflineDownload
pause
