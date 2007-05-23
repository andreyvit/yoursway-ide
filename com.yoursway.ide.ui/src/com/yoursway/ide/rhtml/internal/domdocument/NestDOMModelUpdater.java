package com.yoursway.ide.rhtml.internal.domdocument;

import org.eclipse.wst.xml.core.internal.document.DOMModelImpl;
import org.eclipse.wst.xml.core.internal.document.XMLModelUpdater;

import com.yoursway.ide.rhtml.internal.regions.DOMJSPRegionContexts;

public class NestDOMModelUpdater extends XMLModelUpdater {
    
    /**
     * @param model
     */
    public NestDOMModelUpdater(DOMModelImpl model) {
        super(model);
    }
    
    @Override
    protected boolean isNestedTagClose(String regionType) {
        boolean result = regionType == DOMJSPRegionContexts.JSP_CLOSE
                || regionType == DOMJSPRegionContexts.JSP_DIRECTIVE_CLOSE;
        return result;
    }
    
}
