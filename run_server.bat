@echo off
REM Compile the Host.java
echo Compiling Host.java...
javac Host.java Network.java

REM Check if compilation was successful
if %errorlevel% neq 0 (
    echo Compilation failed.
    pause
    exit /b %errorlevel%
)

REM Run the server
echo Starting the server...
java Host

REM Pause the command prompt to keep it open
pause
