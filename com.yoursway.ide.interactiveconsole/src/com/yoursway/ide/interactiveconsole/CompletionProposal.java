package com.yoursway.ide.interactiveconsole;

public class CompletionProposal {
    
    private final int replaceStart;
    private final int replaceLength;
    private final String text;
    
    public CompletionProposal(int replaceStart, int replaceLength, String text) {
        this.replaceStart = replaceStart;
        this.replaceLength = replaceLength;
        this.text = text;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof CompletionProposal) {
            CompletionProposal p = (CompletionProposal) o;
            return replaceStart == p.replaceStart && replaceLength == p.replaceLength && text.equals(p.text);
        }
        return false;
    }
    
    public String text() {
        return text;
    }
    
    public int replaceStart() {
        return replaceStart;
    }
    
    public int replaceLength() {
        return replaceLength;
    }
    
}
