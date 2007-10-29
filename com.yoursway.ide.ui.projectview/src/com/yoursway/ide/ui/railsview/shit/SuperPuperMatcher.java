package com.yoursway.ide.ui.railsview.shit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;

public final class SuperPuperMatcher {
    
    private static boolean startWithIgnoreCase(String string, String pattern) {
        int length = pattern.length();
        if (string.length() < length)
            return false;
        for (int i = 0; i < length; i++) {
            if (Character.toLowerCase(string.charAt(i)) != Character.toLowerCase(pattern.charAt(i)))
                return false;
        }
        return true;
    }
    
    private static boolean chrCmp(char a, char b) {
        return Character.toLowerCase(a) == Character.toLowerCase(b);
    }
    
    public static MatchResult match(String string, String pattern) {
        int patternLength = pattern.length();
        if (string.equalsIgnoreCase(pattern))
            return new MatchResult(MatchResult.FULL_MATCH, Collections.singletonList((IRegion) new Region(0,
                    patternLength)));
        if (startWithIgnoreCase(string, pattern))
            return new MatchResult(MatchResult.PREFIX, Collections.singletonList((IRegion) new Region(0,
                    patternLength)));
        int stringLength = string.length();
        boolean matching[][] = new boolean[stringLength][patternLength];
        for (int i = 0; i < stringLength; i++) {
            matching[i][0] = chrCmp(string.charAt(i), pattern.charAt(0));
        }
        for (int i = 0; i < stringLength; i++) { // not the best solution but I'm too lazy to rewrite it
            for (int j = 1; j < patternLength; j++) {
                if (chrCmp(string.charAt(i), pattern.charAt(j))) {
                    matching[i][j] = false;
                    for (int k = 0; k < i; k++) {
                        if (matching[k][j - 1]) {
                            matching[i][j] = true;
                            break;
                        }
                    }
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
            if (x == prev + length) {
                length++;
            } else {
                regions.add(new Region(prev, length));
                prev = x;
                length = 1;
            }
        }
        regions.add(new Region(prev, length));
        if (regions.size() == 1) {
            return new MatchResult(MatchResult.SUBSTRING + prev, Collections
                    .singletonList((IRegion) new Region(prev, patternLength)));
        }
        return new MatchResult(MatchResult.SUBSEQUENCE, regions);
    }
}
