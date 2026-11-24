@echo off
setlocal
title API Forged
set "JAVA_HOME=C:\Program Files\Android\Android Studio\jbr"
cd /d "%~dp0"

echo ============================================
echo  Iniciando API Forged
echo  Base de datos: forged_db (MySQL)
echo  Direccion:     http://localhost:8080
echo ============================================
echo.
echo No cierres esta ventana mientras uses la app.
echo Para detener la API presiona Ctrl + C.
echo.

call gradlew.bat bootRun

echo.
echo La API se detuvo.
pause
