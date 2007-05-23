package com.yoursway.ide.rhtml.internal.modelhandler;

import org.eclipse.wst.sse.core.internal.ltk.modelhandler.EmbeddedTypeHandler;

/**
 * This class is only for remembering old and new embedded handlers, in the
 * event a re-init is needed.
 */
public class EmbeddedTypeStateData {
    
    EmbeddedTypeHandler oldHandler;
    EmbeddedTypeHandler newHandler;
    
    public EmbeddedTypeStateData(EmbeddedTypeHandler oldHandler, EmbeddedTypeHandler newHandler) {
        this.oldHandler = oldHandler;
        this.newHandler = newHandler;
    }
    
    public EmbeddedTypeHandler getNewHandler() {
        return newHandler;
    }
    
    public EmbeddedTypeHandler getOldHandler() {
        return oldHandler;
    }
    
}
