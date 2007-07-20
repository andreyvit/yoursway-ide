package com.yoursway.common;

import java.util.Arrays;

/**
 * Immutable.
 * 
 * @author Andrey Tarantsov
 */
public class SegmentedName {
    
    public static final SegmentedName EMPTY_SEGMENTED_NAME = new SegmentedName(new String[0]);
    
    private final String[] segments;
    
    public SegmentedName(String[] segments) {
        this.segments = segments;
    }
    
    public String[] getSegments() {
        return segments;
    }
    
    public String getTrailingSegment() {
        if (segments.length == 0)
            throw new IndexOutOfBoundsException("Segmented name is empty");
        return segments[segments.length - 1];
    }
    
    public SegmentedName removeTrailingSegment() {
        if (segments.length == 0)
            throw new IndexOutOfBoundsException("Segmented name is empty");
        if (segments.length == 1)
            return EMPTY_SEGMENTED_NAME;
        String[] result = new String[segments.length - 1];
        System.arraycopy(segments, 0, result, 0, result.length);
        return new SegmentedName(result);
    };
    
    public SegmentedName replaceTrailingSegment(String newTrailingSegment) {
        if (segments.length == 0)
            throw new IndexOutOfBoundsException("Segmented name is empty");
        String[] result = new String[segments.length];
        System.arraycopy(segments, 0, result, 0, result.length - 1);
        result[result.length - 1] = newTrailingSegment;
        return new SegmentedName(result);
    }
    
    public int getSegmentCount() {
        return segments.length;
    }
    
    public boolean equalsSingleSegment(String singleSegment) {
        return segments.length == 1 && segments[0].equals(singleSegment);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(segments);
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final SegmentedName other = (SegmentedName) obj;
        if (!Arrays.equals(segments, other.segments))
            return false;
        return true;
    }
    
}
