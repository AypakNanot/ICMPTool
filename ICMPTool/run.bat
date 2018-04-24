@echo off
:: 在for循环中用set循环赋值，必须setlocal ENABLEDELAYEDEXPANSION（延迟加载）
setlocal ENABLEDELAYEDEXPANSION

for /r .\lib\ %%i in (*.jar) do (
    set classp1=!classp1!.\lib\%%~ni.jar;
)
@echo on
java -Xmx1024M -Djna.library.path=. -Dfile.encoding=GBK -cp %classp1%.\res\ com.optel.run.Ping
endlocal