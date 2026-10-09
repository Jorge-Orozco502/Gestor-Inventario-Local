@echo off
title Gestor de Inventario - Entorno de Desarrollo

REM 1. Configurar ruta base del proyecto
set RUTA_APP=%~dp0
cd /d "%RUTA_APP%"

REM 2. Verificar si la carpeta de MySQL existe
if not exist ".\mysql_portable\bin\mysqld.exe" (
    echo [ERROR] No se encontro la carpeta 'mysql_portable'.
    echo Por favor, descarga MySQL portable en zip, descomprimelo en la raiz del proyecto y renombra la carpeta a 'mysql_portable'.
    pause
    exit /b
)

REM 3. Si la carpeta 'data' no existe, inicializar MySQL automaticamente (sin contrasenia)
if not exist ".\mysql_portable\data" (
    echo [INFO] Primera ejecucion detectada. Inicializando base de datos local...
    ".\mysql_portable\bin\mysqld.exe" --initialize-insecure --datadir=".\mysql_portable\data"
    timeout /t 3 /nobreak >nul
)

REM 4. Iniciar el servidor MySQL si no esta corriendo
tasklist /FI "IMAGENAME eq mysqld.exe" 2>NUL | find /I /N "mysqld.exe">NUL
if "%ERRORLEVEL%"=="0" (
    echo [INFO] El servidor MySQL ya esta activo.
) else (
    echo [INFO] Iniciando servidor MySQL local...
    start "" /b ".\mysql_portable\bin\mysqld.exe" --datadir=".\mysql_portable\data" --port=3306
    timeout /t 5 /nobreak >nul
)

REM 5. Crear la BD e importar las tablas si no existen
echo [INFO] Verificando esquema e importando tablas SQL...
".\mysql_portable\bin\mysql.exe" -u root -e "CREATE DATABASE IF NOT EXISTS inventario_vidrieria;" 2>nul
".\mysql_portable\bin\mysql.exe" -u root inventario_vidrieria < ".\BaseDatos\inventario_vidrieria Progra II.sql" 2>nul

echo [OK] Base de datos configurada y lista en el puerto 3306.
echo.

REM 6. Iniciar la app Java si el .jar existe (Omitir error si aun estan programando)
if exist ".\dist\GESTOR-INVENTARIO-LOCAL.jar" (
    echo [INFO] Lanzando aplicacion Java...
    start "" javaw -jar ".\dist\GESTOR-INVENTARIO-LOCAL.jar"
) else (
    echo [INFO] Modo Desarrollo: El ejecutable .jar no se ha generado todavia.
    echo Puedes ejecutar tu clase principal o 'conexionBD.java' directamente desde tu IDE.
)

pause