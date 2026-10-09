@echo off
cd /d "%~dp0backend"
echo Starting NetGuard AI backend...
mvn spring-boot:run
pause
