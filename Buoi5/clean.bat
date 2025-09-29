@echo off
echo Cleaning compiled files...
if exist bin (
    rmdir /s /q bin
    echo Deleted bin/ directory and all .class files
) else (
    echo No bin/ directory found
)
if exist *.class (
    del *.class
    echo Deleted any .class files in current directory
)
echo Clean completed!
pause