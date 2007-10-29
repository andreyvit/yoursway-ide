/**
 * 
 */
package com.yoursway.ide.ui.railsview.shit;

import java.util.Collection;

import org.eclipse.jface.text.IRegion;

public class MatchResult {
    
    public static final int FULL_MATCH = 0;
    public static final int PREFIX = 1;
    public static final int SUBSTRING = 2;
    public static final int SUBSEQUENCE = 100;
    
    public int kind;
    public Collection<IRegion> regions;
    
    public MatchResult(int kind, Collection<IRegion> regions) {
        this.kind = kind;
        this.regions = regions;
    }
    
    public MatchResult(int kind) {
        this.kind = kind;
        this.regions = null;
    }
}