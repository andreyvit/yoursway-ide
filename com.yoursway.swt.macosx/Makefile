
#SWT_DEBUG = -g
ARCHS = -arch i386 -arch ppc
CFLAGS = -c -Wall $(ARCHS) $(SWT_DEBUG) -DUSE_ASSEMBLER -DCARBON -I /System/Library/Frameworks/JavaVM.framework/Headers
LFLAGS = -bundle $(ARCHS) -framework JavaVM -framework Carbon 
OUTPUT=libyoursway-macosx.jnilib
OBJECTS=os.o

.c.o:
	cc $(CFLAGS) $*.c

$(OUTPUT): $(OBJECTS)
	cc -o $(OUTPUT) $(LFLAGS) $(OBJECTS)

headers :
	javah -classpath bin com.yoursway.swt.experiments.MacOS
	