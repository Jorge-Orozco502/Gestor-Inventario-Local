@echo off
title Gestor de Inventario - Iniciando Servicios...

REM 1. Determinar la ruta actual de ejecucion
set RUTA_APP=%~dp0
cd /d "%RUTA_APP%"

REM 2. Iniciar MySQL Portable en segundo plano si no esta corriendo
tasklist /FI "IMAGENAME eq mysqld.exe" 2>NUL | find /I /N "mysqld.exe">NUL
if "%ERRORLEVEL%"=="0" (
    echo [INFO] El servidor MySQL ya se encuentra activo.
) else (
    echo [INFO] Iniciando servidor MySQL local...
    start "" /b ".\mysql_portable\bin\mysqld.exe" --datadir=".\mysql_portable\data" --port=3306
    timeout /t 3 /nobreak >nul
)

REM 3. Crear la base de datos e importar el script SQL si no existe
echo [INFO] Verificando base de datos...
".\mysql_portable\bin\mysql.exe" -u root -e "CREATE DATABASE IF NOT EXISTS inventario_vidrieria;" 2>nul
".\mysql_portable\bin\mysql.exe" -u root inventario_vidrieria < ".\BaseDatos\inventario_vidrieria Progra II.sql" 2>nul

REM 4. Iniciar la aplicacion Java (.jar)
echo [INFO] Iniciando Aplicacion...
start "" javaw -jar ".\dist\GESTOR-INVENTARIO-LOCAL.jar"

exit