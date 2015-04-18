SET BIN_PATH=bin/
javac tool/ListFile.java -d "%BIN_PATH%"
java -classpath "%BIN_PATH%" tool.ListFile .java src > filelist
pause