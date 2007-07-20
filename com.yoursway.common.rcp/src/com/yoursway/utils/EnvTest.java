package com.yoursway.utils;

import org.eclipse.core.runtime.Platform;

public class EnvTest {
    
    private static final boolean IS_MACOSX = Platform.OS_MACOSX.equals(Platform.getOS());
    
    private static final boolean IS_WINDOWS = Platform.OS_WIN32.equals(Platform.getOS());
    
    private static final boolean IS_LINUX = Platform.OS_LINUX.equals(Platform.getOS());
    
    private EnvTest() {
    }
    
    public static boolean isMacOSX() {
        return IS_MACOSX;
    }
    
    public static boolean isWindowsOS() {
        return IS_WINDOWS;
    }
    
    public static boolean isPosix() {
        return !IS_WINDOWS;
    }
    
    /**
     * Warning: before using this function consider if <code>isPosix</code>
     * will work for you instead.
     */
    public static boolean isLinux() {
        return IS_LINUX;
    }
    
}
