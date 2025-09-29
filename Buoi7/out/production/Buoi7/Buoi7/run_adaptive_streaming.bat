@echo off
echo ========================================
echo    ADAPTIVE SCREEN SHARING SYSTEM
echo ========================================
echo.
echo Chon lua chon:
echo 1. Chay Server + Monitor (Recommended)
echo 2. Chay Server only
echo 3. Chay Client
echo 4. Chay Monitor only
echo 5. Bien dich tat ca
echo 6. Test bandwidth giua 2 may
echo 7. Thoat
echo.
set /p choice="Nhap lua chon (1-7): "

if "%choice%"=="1" goto server_monitor
if "%choice%"=="2" goto server_only
if "%choice%"=="3" goto client
if "%choice%"=="4" goto monitor_only
if "%choice%"=="5" goto compile
if "%choice%"=="6" goto bandwidth_test
if "%choice%"=="7" goto exit

:compile
echo Dang bien dich tat ca cac file...
cd ..
javac -encoding UTF-8 Buoi7/*.java
if %errorlevel%==0 (
    echo Bien dich thanh cong!
    echo.
    cd Buoi7
    pause
    goto menu
) else (
    echo Co loi khi bien dich!
    cd Buoi7
    pause
    goto exit
)

:server_monitor
echo Khoi dong Server voi Monitor Dashboard...
echo Server se chay tren port 2345
echo Monitor Dashboard se hien thi thong tin tat ca clients
echo.
cd ..
java Buoi7.AdaptiveStreamingMonitor
pause
goto exit

:server_only
echo Khoi dong Server only (khong co giao dien monitor)...
echo Server se chay tren port 2345
echo.
cd ..
java Buoi7.ScreenServerAdaptive
pause
goto exit

:client
echo Khoi dong Adaptive Client...
echo Dang ket noi den localhost:2345
echo Client se tu dong dieu chinh chat luong dua tren bandwidth
echo.
cd ..
java Buoi7.ScreenClientAdaptive
pause
goto exit

:monitor_only
echo Khoi dong Monitor Dashboard only...
echo Luu y: Server phai da chay truoc khi dung monitor
echo.
cd ..
java Buoi7.AdaptiveStreamingMonitor
pause
goto exit

:bandwidth_test
echo ========================================
echo         BANDWIDTH TEST MODE
echo ========================================
echo.
echo Huong dan test:
echo 1. Chay Server tren may thu nhat
echo 2. Sua SERVER_ADDRESS trong ScreenClientAdaptive.java thanh IP may server
echo 3. Chay Client tren may thu hai
echo 4. Xem ket qua do bandwidth trong giao dien Client va Monitor
echo.
echo Cac quality levels:
echo - VERY_LOW: ^< 50 KB/s, ping ^> 200ms
echo - LOW: 50-200 KB/s
echo - MEDIUM: 200-500 KB/s  
echo - HIGH: ^> 500 KB/s, ping ^< 200ms
echo.
pause
goto menu

:menu
goto :eof

:exit
echo.
echo ========================================
echo     CAM ON BAN DA SU DUNG!
echo ========================================
echo.
echo Tinh nang da duoc thuc hien:
echo ✓ Do toc do duong truyen cua moi client
echo ✓ Dieu chinh chat luong hinh anh tu dong
echo ✓ Monitor bandwidth real-time
echo ✓ Adaptive frame rate va compression
echo ✓ Ping measurement va quality adjustment
echo ✓ Dashboard quan ly nhieu client
echo.
pause