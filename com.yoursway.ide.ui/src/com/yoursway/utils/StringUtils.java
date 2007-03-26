package com.yoursway.utils;

public class StringUtils {
    
    public static String join(String[] name, String delimiter) {
        int totalLength = name.length * delimiter.length();
        for (String string : name)
            totalLength += string.length();
        StringBuilder builder = new StringBuilder(totalLength);
        for (String string : name) {
            if (builder.length() > 0)
                builder.append(delimiter);
            builder.append(string);
        }
        return builder.toString();
    }
    
    public static String stripSuffix(String string, String suffix) {
        if (string.endsWith(suffix))
            return string.substring(0, string.length() - suffix.length());
        else
            return string;
    }
    
}
