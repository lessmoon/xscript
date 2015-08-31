SET BIN_PATH=bin/
SET SRC_PATH=src/
javac tool/GenTable.java -d "%BIN_PATH%"
javac @filelist -d "%BIN_PATH%" -Xlint:unchecked
pause