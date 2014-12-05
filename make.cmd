echo OFF
cls
SET BIN_PATH=bin/
SET SRC_PATH=src/
cd %SRC_PATH%
javac lexer/*.java -d "../%BIN_PATH%"
javac inter/*.java -d "../%BIN_PATH%"
javac parser/*.java -d "../%BIN_PATH%"
javac main/*.java -d "../%BIN_PATH%"
javac runtime/*.java -d "../%BIN_PATH%"
pause