#include <Carbon/Carbon.h>
#include "os.h"

JNIEXPORT jint JNICALL Java_com_yoursway_swt_experiments_MacOS_OpenDrawer
  (JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
    return OpenDrawer((WindowRef) arg0, (OptionBits) arg1, (Boolean) arg2);
}

JNIEXPORT jint JNICALL Java_com_yoursway_swt_experiments_MacOS_SetDrawerParent
  (JNIEnv *env, jclass that, jint arg0, jint arg1)
{
    return SetDrawerParent((WindowRef) arg0, (WindowRef) arg1);
}
