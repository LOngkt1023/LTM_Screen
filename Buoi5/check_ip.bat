@echo off
echo ========================================
echo       KIEM TRA DIA CHI IP
echo ========================================
echo.
echo Dia chi IP cua may nay:
ipconfig | findstr "IPv4"
echo.
echo Luu y: Su dung dia chi IPv4 (khong phai 127.0.0.1)
echo Vi du: 192.168.1.100, 192.168.0.50, etc.
echo.
pause