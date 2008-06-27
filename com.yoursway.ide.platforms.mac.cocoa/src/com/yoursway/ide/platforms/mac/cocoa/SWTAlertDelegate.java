package com.yoursway.ide.platforms.mac.cocoa;

import org.eclipse.swt.internal.cocoa.NSObject;
import org.eclipse.swt.internal.cocoa.OS;

public class SWTAlertDelegate extends NSObject {

    public SWTAlertDelegate() {
        super(0);
    }
        
    public SWTAlertDelegate(int id) {
        super(id);
    }

    public int tag() {
        int[] tag = new int[1];
        OS.object_getInstanceVariable(id, "tag", tag);    
        return tag[0];
//        return OS.objc_msgSend(id, OS.sel_tag);
    }

    public void setTag(int tag) {
        OS.object_setInstanceVariable(id, "tag", tag);
    }

   
}
