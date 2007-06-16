package com.yoursway.ide.rhtml.internal.contentassist;

public class TemplateContextTypeIdsJSP {
    
    public static final String ALL = getAll();
    
    public static final String ATTRIBUTE = getAttribute();
    
    public static final String ATTRIBUTE_VALUE = getAttributeValue();
    
    public static final String NEW = getNew();
    
    public static final String TAG = getTag();
    
    private static String getAll() {
        return getPrefix() + "_all"; //$NON-NLS-1$
    }
    
    private static String getAttribute() {
        return getPrefix() + "_attribute"; //$NON-NLS-1$
    }
    
    private static String getAttributeValue() {
        return getPrefix() + "_attribute_value"; //$NON-NLS-1$
    }
    
    private static String getNew() {
        return getPrefix() + "_new"; //$NON-NLS-1$
    }
    
    private static String getPrefix() {
        return "jsp"; //$NON-NLS-1$
    }
    
    private static String getTag() {
        return getPrefix() + "_tag"; //$NON-NLS-1$
    }
}
