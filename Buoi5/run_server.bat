@echo off
echo Starting File Server...
echo Server will run on port 6000
echo Files directory: files/
echo.
if not exist bin mkdir bin
javac -d bin FileServer.java
java -cp bin FileServer
pause