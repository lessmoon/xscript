type keyword | sort > sk
SET BIN_PATH=../bin/ 
type sk | java -classpath "%BIN_PATH%" tool.GenTable 5 > skt
pause