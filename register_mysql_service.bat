@echo off
:: Check for Administrator permissions
net session >nul 2>&1
if %errorLevel% == 0 (
    echo Installing MySQL 8.4 as a Windows Service...
    "C:\Program Files\MySQL\MySQL Server 8.4\bin\mysqld.exe" --install MySQL84 --defaults-file="C:\ProgramData\MySQL\MySQL Server 8.4\my.ini"
    echo Starting MySQL84 service...
    net start MySQL84
    echo Done! MySQL is now registered and running as a Windows Service.
    pause
) else (
    echo Requesting Administrator privileges...
    powershell -Command "Start-Process '%~f0' -Verb RunAs"
)
