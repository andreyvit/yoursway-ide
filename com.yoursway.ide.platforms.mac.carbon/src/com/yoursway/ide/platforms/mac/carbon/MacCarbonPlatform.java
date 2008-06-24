package com.yoursway.ide.platforms.mac.carbon;

import com.yoursway.ide.platforms.api.LastWindowCloseBehavior;
import com.yoursway.ide.platforms.mac.MacPlatform;

public class MacCarbonPlatform extends MacPlatform {

    public LastWindowCloseBehavior conventionalLastWindowCloseBehavior() {
        return LastWindowCloseBehavior.EXIT;
    }
    
}
