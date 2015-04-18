echo OFF
cls
SET BIN_PATH=bin/
SET SRC_PATH=src/
java -classpath %BIN_PATH% -ea main.Main testcase/test.xs   -eo -so
::> log.txt
pause
