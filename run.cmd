SET BIN_PATH=bin/
SET SRC_PATH=src/
cd %BIN_PATH%
type test.txt | java -ea parser.Parser
pause
