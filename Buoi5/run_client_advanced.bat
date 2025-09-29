@echo off
echo Starting Advanced File Client...
echo Connecting to server at specified IP
echo Features: Download and Upload files
echo.
if not exist bin mkdir bin
javac -d bin FileClientAdvanced.java
java -cp bin FileClientAdvanced
pause