@echo off
:: ��forѭ������setѭ����ֵ������setlocal ENABLEDELAYEDEXPANSION���ӳټ��أ�
setlocal ENABLEDELAYEDEXPANSION

for /r .\lib\ %%i in (*.jar) do (
    set classp1=!classp1!.\lib\%%~ni.jar;
)
@echo on
java -Xmx1024M -Djna.library.path=. -Dfile.encoding=GBK -cp %classp1%.\res\ com.optel.run.Ping
endlocal