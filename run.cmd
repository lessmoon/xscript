echo OFF
cls
SET BIN_PATH=bin/
SET SRC_PATH=src/
cd %BIN_PATH%
java -ea main.Main ../testcase/test.xs  -eo -so  -pc> log.txt
pause
