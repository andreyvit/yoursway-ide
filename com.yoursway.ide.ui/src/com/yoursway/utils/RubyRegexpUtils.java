package com.yoursway.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RubyRegexpUtils {
    
    private static final Pattern RUBY_REPLACEMENT_BACKREF = Pattern.compile("\\\\(\\d+)");
    
    public static Pattern compileRubyRegexp(String regexp) {
        return Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
    }
    
    public static String convertRubyReplacement(String replacement) {
        Matcher matcher = RUBY_REPLACEMENT_BACKREF.matcher(replacement);
        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(result, "\\$$1");
        }
        matcher.appendTail(result);
        return result.toString();
    }
    
}
