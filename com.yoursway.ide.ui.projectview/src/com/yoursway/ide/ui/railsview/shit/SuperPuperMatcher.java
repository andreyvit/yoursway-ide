package com.yoursway.ide.ui.railsview.shit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;

public final class SuperPuperMatcher {
    
    public static final int FULL_MATCH = 0;
    public static final int PREFIX = 1;
    public static final int SUBSTRING = 2;
    public static final int SUBSEQUENCE = 100;
    
    public static class MatchResult {
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
    
    public static MatchResult match(String string, String pattern) {
        int patternLength = pattern.length();
        if (string.equals(pattern))
            return new MatchResult(FULL_MATCH, Collections.singletonList((IRegion) new Region(0,
                    patternLength)));
        if (string.startsWith(pattern))
            return new MatchResult(PREFIX, Collections.singletonList((IRegion) new Region(0, patternLength)));
        int pos = string.indexOf(pattern);
        if (pos >= 0)
            return new MatchResult(SUBSTRING + pos, Collections.singletonList((IRegion) new Region(pos,
                    patternLength)));
        int stringLength = string.length();
        boolean matching[][] = new boolean[stringLength][patternLength];
        for (int i = 0; i < stringLength; i++) { // not the best solution but I'm too lazy to rewrite it
            for (int j = 0; j < patternLength; j++) {
                if (string.charAt(i) == pattern.charAt(j)) {
                    boolean prev = (i == 0);
                    for (int k = 0; k < i; k++) {
                        prev |= matching[k][j - 1];
                    }
                    matching[i][j] = prev;
                } else
                    matching[i][j] = false;
            }
        }
        int last = -1;
        for (int i = 0; i < stringLength; i++) {
            if (matching[i][patternLength - 1]) {
                last = i;
                break;
            }
        }
        if (last == -1)
            return null;
        List<Integer> positions = new ArrayList<Integer>();
        positions.add(last);
        for (int k = patternLength - 2; k >= 0; k--) {
            last--;
            while (last >= 0 && !matching[last][k])
                last--;
            if (last >= 0)
                positions.add(last);
            else
                throw new RuntimeException("Absolute shit");
        }
        Collections.sort(positions);
        List<IRegion> regions = new ArrayList<IRegion>();
        int prev = -1;
        int length = 0;
        for (int x : positions) {
            if (prev == -1) {
                prev = x;
                length = 1;
                continue;
            }
            if (x == prev + 1) {
                length++;
            } else {
                regions.add(new Region(prev, length));
                prev = x;
                length = 1;
            }
        }
        regions.add(new Region(prev, length));
        return new MatchResult(SUBSEQUENCE, regions);
    }
}
