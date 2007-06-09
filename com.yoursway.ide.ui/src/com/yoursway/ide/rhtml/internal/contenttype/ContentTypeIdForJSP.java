package com.yoursway.ide.rhtml.internal.contenttype;

/**
 * This class, with its one field, is a convience to provide compile-time safety
 * when refering to a contentType ID. The value of the contenttype id field must
 * match what is specified in plugin.xml file.
 */

public class ContentTypeIdForJSP {
    /**
     * The value of the contenttype id field must match what is specified in
     * plugin.xml file. Note: this value is intentially set with default
     * protected method so it will not be inlined.
     */
    public final static String ContentTypeID_JSP = getConstantString();
    
    /**
     * Don't allow instantiation.
     */
    private ContentTypeIdForJSP() {
        super();
    }
    
    static String getConstantString() {
        return "com.yoursway.erbhtml"; //$NON-NLS-1$
    }
    
}
