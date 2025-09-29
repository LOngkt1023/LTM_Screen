@echo off
echo Compiling all Java files...
echo Compiled files will be saved to bin/ directory
echo.
if not exist bin mkdir bin
javac -d bin *.java
if %errorlevel% == 0 (
    echo Compilation successful!
    echo Compiled .class files are in bin/ directory
    echo.
    echo To run:
    echo - Double click run_server.bat to start server
    echo - Double click run_client.bat to start client
    echo.
    echo Available test files in files/ directory:
    dir files\
) else (
    echo Compilation failed!
)
pause