echo OFF
cls
SET BIN_PATH=bin/
SET SRC_PATH=src/
java -classpath %BIN_PATH% -ea main.Main -so -eo -brds 20 testcase/test.xs ok 22 > code2
pause>nul