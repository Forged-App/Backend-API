@echo off
setlocal
title Pruebas API Forged
cd /d "%~dp0"

echo ============================================
echo  Probando la API Forged
echo  Asegurate de que la API este corriendo.
echo ============================================
echo.

powershell -NoProfile -ExecutionPolicy Bypass -File ".\scripts\probar-api.ps1"

echo.
pause
