package com.yoursway.ide.platforms.mac.cocoa;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;

import com.yoursway.ide.platforms.api.GlobalMenuSupport;
import com.yoursway.ide.platforms.api.LastWindowCloseBehavior;
import com.yoursway.ide.platforms.mac.MacPlatform;

public class MacCocoaPlatform extends MacPlatform implements GlobalMenuSupport {

    public LastWindowCloseBehavior conventionalLastWindowCloseBehavior() {
        return LastWindowCloseBehavior.CONTINUE_RUNNING;
    }

    public GlobalMenuSupport globalMenuSupport() {
        return this;
    }

    public void setGlobalApplicationMenu(Display display, Menu menu) {
        display.setApplicationMenuBar(menu);
    }

}
