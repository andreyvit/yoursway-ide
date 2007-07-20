package com.yoursway.common;

import java.util.Set;

public class StringUtils {
    
    public static String join(String[] name, String delimiter) {
        return join(name, delimiter, 0, name.length);
        
    }
    
    public static String join(String[] name, String delimiter, int startIndex, int endIndex) {
        int totalLength = name.length * delimiter.length();
        for (int i = startIndex; i < endIndex; i++)
            totalLength += name[i].length();
        StringBuilder builder = new StringBuilder(totalLength);
        for (int i = startIndex; i < endIndex; i++) {
            if (builder.length() > 0)
                builder.append(delimiter);
            builder.append(name[i]);
        }
        return builder.toString();
    }
    
    public static String stripSuffix(String string, String suffix) {
        if (string.endsWith(suffix))
            return string.substring(0, string.length() - suffix.length());
        else
            return string;
    }
    
    public static String chooseUniqueString(String namePrefix, String nameSuffix, String numberPrefix,
            Set<String> usedStrings) {
        String name;
        int suffix = 1;
        do {
            String stringSuffix = (suffix == 1 ? "" : numberPrefix + suffix);
            name = namePrefix + stringSuffix + nameSuffix;
            suffix++;
        } while (usedStrings.contains(name));
        return name;
    }
    
}
