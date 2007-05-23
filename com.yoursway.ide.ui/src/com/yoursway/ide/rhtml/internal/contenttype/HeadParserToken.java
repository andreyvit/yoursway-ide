package com.yoursway.ide.rhtml.internal.contenttype;

public class HeadParserToken {
    private int fStart;
    
    private String fText;
    private String fType;
    
    protected HeadParserToken() {
        super();
    }
    
    public HeadParserToken(String type, int start, String text) {
        this();
        fType = type;
        fStart = start;
        fText = text;
        
    }
    
    public String getText() {
        return fText;
    }
    
    public String getType() {
        return fType;
    }
    
    @Override
    public String toString() {
        return ("text: " + fText + " offset: " + fStart + " type: " + fType); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
}
