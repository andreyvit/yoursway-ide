if "%JAVA_HOME%"=="" (
	set JAVA_HOME=C:\Program Files\Java\jdk1.5.0_07
)
"%JAVA_HOME%\bin\javah" -classpath ..\bin -o sysutils.h com.yoursway.utils.SystemUtilitiesImpl 
cl -nologo -c -DUNICODE -D_UNICODE "-I%JAVA_HOME%\include" "-I%JAVA_HOME%\include\win32" sysutils.c
link -nologo "-libpath:%JAVA_HOME%\lib" -dll sysutils.obj shell32.lib 
del *.exp
del *.lib
del *.obj
set LIBNAME=sysutils.dll
set TARGET=..\%LIBNAME%
if exist %TARGET% del %TARGET%
move %LIBNAME% %TARGET%
