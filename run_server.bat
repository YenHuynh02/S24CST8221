@echo off
REM Compile the Host.java
echo Compiling Host.java...
javac Host.java HostFrame.java Network.java BattleshipView.java BattleshipModel.java BattleshipController.java main.java

REM Check if compilation was successful
if %errorlevel% neq 0 (
    echo Compilation failed.
    pause
    exit /b %errorlevel%
)

REM Run the server
echo Starting the server...
java main

REM Pause the command prompt to keep it open
pause
