SET BIN_PATH=bin/
SET SRC_PATH=src/
javac @filelist -d "%BIN_PATH%" -Xlint:unchecked
pause