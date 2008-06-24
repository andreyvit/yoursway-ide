package com.yoursway.ide.platforms.mac.cocoa;

import com.yoursway.ide.platforms.api.LastWindowCloseBehavior;
import com.yoursway.ide.platforms.mac.MacPlatform;

public class MacCocoaPlatform extends MacPlatform {

    public LastWindowCloseBehavior conventionalLastWindowCloseBehavior() {
        return LastWindowCloseBehavior.CONTINUE_RUNNING;
    }

}
