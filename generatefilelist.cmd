if exist filelist del filelist 
for /F %%i in ('dir src /A /B') do for /F %%v in ('dir src\%%i /A /B *.java') do echo src\%%i\%%v >> filelist