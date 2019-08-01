SET BIN_PATH=bin/
SET SRC_PATH=src/
javac -J-Duser.language=en tool/GenTable.java -d "%BIN_PATH%"
javac -J-Duser.language=en @filelist -d "%BIN_PATH%" -Xlint:all
pause