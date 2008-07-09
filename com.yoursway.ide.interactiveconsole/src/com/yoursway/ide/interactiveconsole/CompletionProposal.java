package com.yoursway.ide.interactiveconsole;

public class CompletionProposal implements Comparable<CompletionProposal> {
    
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
    
    public int compareTo(CompletionProposal o) {
        int r = text.compareTo(o.text);
        if (r != 0)
            return r;
        if (replaceStart < o.replaceStart)
            return -1;
        if (replaceStart > o.replaceStart)
            return 1;
        if (replaceLength < o.replaceLength)
            return -1;
        if (replaceLength > o.replaceLength)
            return 1;
        return 0;
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
