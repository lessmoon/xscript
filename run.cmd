echo OFF
cls
SET BIN_PATH=bin/
SET SRC_PATH=src/
java -classpath %BIN_PATH% -ea main.Main -so -dbgcmp -dbgrt -dbgst -eo -brds 20 testcase/test.xs ok 22 >structs.xs
pause>nul