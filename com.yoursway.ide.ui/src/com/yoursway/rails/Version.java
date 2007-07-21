package com.yoursway.rails;

import java.util.regex.Pattern;

import com.yoursway.common.Strings;

/**
 * Holds a version number. Currently is a stub implementation that uses "natural
 * string order" for comparisons.
 * 
 * @author Andrey Tarantsov
 */
public class Version implements Comparable<Version> {
    
    private final String versionString;
    
    private transient String[] components;
    
    private Version(String dotDelimitedString) {
        this.versionString = dotDelimitedString;
    }
    
    public static Version fromDotDelimitedString(String dotDelimitedString) {
        return new Version(dotDelimitedString);
    }
    
    public String asDotDelimitedString() {
        return versionString;
    }
    
    public String[] getComponents() {
        if (components == null)
            components = versionString.split(Pattern.quote("."));
        return components;
    }
    
    public String getComponent(int index, String defaultValue) {
        String[] components = getComponents();
        if (index >= components.length)
            return defaultValue;
        else
            return components[index];
    }
    
    public int compareTo(Version o) {
        return Strings.compareNatural(versionString, o.versionString);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((versionString == null) ? 0 : versionString.hashCode());
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
        final Version other = (Version) obj;
        if (versionString == null) {
            if (other.versionString != null)
                return false;
        } else if (!versionString.equals(other.versionString))
            return false;
        return true;
    }
    
}
