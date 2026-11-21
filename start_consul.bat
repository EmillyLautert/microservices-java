@echo off
setlocal
cd /d "%~dp0"
docker compose -f docker\docker-compose.yml up --build
if errorlevel 1 pause
