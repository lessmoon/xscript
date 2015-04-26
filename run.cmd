echo OFF
cls
SET BIN_PATH=bin/
SET SRC_PATH=src/
java -classpath %BIN_PATH% -ea main.Main -so -eo -pc testcase/test.xs ok 22 > log.txt
pause