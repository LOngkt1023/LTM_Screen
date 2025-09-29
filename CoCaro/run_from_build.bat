@echo off
echo ====================================
echo     GAME CARO ONLINE (từ build)
echo ====================================
echo.
echo Chon lua chon:
echo 1. Chay Server
echo 2. Chay Client
echo 3. Chay Caro Offline
echo 4. Bien dich lai
echo 5. Thoat
echo.
set /p choice="Nhap lua chon (1-5): "

if "%choice%"=="1" goto server
if "%choice%"=="2" goto client
if "%choice%"=="3" goto offline
if "%choice%"=="4" goto compile
if "%choice%"=="5" goto exit

:compile
echo Dang bien dich vao thu muc build...
javac -encoding UTF-8 -d build *.java
if %errorlevel%==0 (
    echo Bien dich thanh cong!
    echo.
    goto menu
) else (
    echo Co loi khi bien dich!
    pause
    goto exit
)

:server
echo Khoi dong Server tu build...
echo Server se chay tren port 12345
echo.
java -cp build CaroServer
pause
goto exit

:client
echo Khoi dong Client tu build...
echo Dang ket noi den localhost:12345
echo.
java -cp build CaroClient
pause
goto exit

:offline
echo Khoi dong Caro Offline tu build...
echo.
java -cp build CaroOffline
pause
goto exit

:menu
goto :eof

:exit
echo Tam biet!
pause
