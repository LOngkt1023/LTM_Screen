@echo off
echo Starting File Client...
echo Connecting to server at localhost:6000
echo Downloaded files will be saved to downloads/ folder
echo.
if not exist bin mkdir bin
javac -d bin FileClient.java
java -cp bin FileClient
pause