@echo off
title NetGuard AI - Launcher
color 0A

echo ==========================================
echo        NETGUARD AI - STARTING
echo ==========================================
echo.

REM Start MySQL Server
echo [1/3] Starting MySQL Server...
net start MySQL80 >nul 2>&1
if %errorlevel%==0 (
    echo MySQL Server started.
) else (
    echo MySQL Server is already running, or the service name is different.
)
echo.

REM Start Spring Boot backend
echo [2/3] Starting Spring Boot backend...
start "NetGuard AI - Backend" cmd /k "cd /d C:\Users\Naveen\Downloads\netguard-v2\backend && set SPRING_DATASOURCE_USERNAME=root && set SPRING_DATASOURCE_PASSWORD=root && mvn spring-boot:run"
echo Backend window opened.
echo.

REM Start React frontend on port 5172
echo [3/3] Starting React frontend on port 5172...
start "NetGuard AI - Frontend" cmd /k "cd /d C:\Users\Naveen\Downloads\netguard-v2\frontend && npm run dev -- --port 5172"
echo Frontend window opened.
echo.

REM Open browser after a short delay
timeout /t 5 /nobreak >nul
start "" "http://localhost:5172"

echo.
echo ==========================================
echo NetGuard AI is starting...
echo Browser: http://localhost:5172
echo ==========================================
echo.
pause
