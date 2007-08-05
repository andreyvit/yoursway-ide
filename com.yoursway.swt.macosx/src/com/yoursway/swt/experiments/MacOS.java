package com.yoursway.swt.experiments;

public class MacOS {
    
    static {
        System.loadLibrary("yoursway-macosx");
    }
    
    public static native int SetDrawerParent(int drawerHandle, int parentHandle);
    
    public static native int OpenDrawer(int windowHandle, int options, boolean async);
    
}
