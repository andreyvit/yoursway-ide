package com.yoursway.ide.platforms.windows.win32;

import com.yoursway.ide.platforms.api.LastWindowCloseBehavior;
import com.yoursway.ide.platforms.win.WinPlatform;

public class Win32Platform extends WinPlatform {

    public LastWindowCloseBehavior conventionalLastWindowCloseBehavior() {
        return LastWindowCloseBehavior.EXIT;
    }
    
}
