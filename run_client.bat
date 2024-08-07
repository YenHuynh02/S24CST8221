@echo off
REM Compile the client and game files
echo Compiling client and game files...
javac Client.java Network.java BattleshipView.java BattleshipModel.java BattleshipController.java main.java

REM Check if compilation was successful
if %errorlevel% neq 0 (
    echo Compilation failed.
    pause
    exit /b %errorlevel%
)

REM Run the client
echo Starting the client...
java main

REM Pause the command prompt to keep it open
pause
