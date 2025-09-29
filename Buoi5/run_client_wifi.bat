@echo off
echo ========================================
echo    FILE TRANSFER CLIENT (WIFI MODE)
echo ========================================
echo.
echo Huong dan ket noi qua WiFi:
echo 1. Dam bao ca 2 may cung ket noi WiFi
echo 2. Tren may server, chay run_server.bat
echo 3. Xem IP cua may server bang cach chay: ipconfig
echo 4. Nhap IP cua may server khi duoc hoi
echo.
if not exist bin mkdir bin
javac -d bin FileClient.java
java -cp bin FileClient
pause