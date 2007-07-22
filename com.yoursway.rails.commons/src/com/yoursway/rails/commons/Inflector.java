package com.yoursway.rails.commons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yoursway.rails.commons.internal.RubyRegexpUtils;

public class Inflector {
    
    private static class Pair {
        
        private final Pattern pattern;
        private final String replacement;
        
        public Pair(Pattern pattern, String replacement) {
            this.pattern = pattern;
            this.replacement = replacement;
        }
        
        public Pattern getPattern() {
            return pattern;
        }
        
        public String getReplacement() {
            return replacement;
        }
        
    }
    
    private final List<Pair> singulars = new ArrayList<Pair>();
    
    private final List<Pair> plurals = new ArrayList<Pair>();
    
    private final Collection<String> uncountables = new HashSet<String>();
    
    public void plural(String rule, String replacement) {
        final Pattern regexp = RubyRegexpUtils.compileRubyRegexp(rule);
        final String javaReplacement = RubyRegexpUtils.convertRubyReplacement(replacement);
        plurals.add(0, new Pair(regexp, javaReplacement));
    }
    
    public void singular(String rule, String replacement) {
        final Pattern regexp = RubyRegexpUtils.compileRubyRegexp(rule);
        final String javaReplacement = RubyRegexpUtils.convertRubyReplacement(replacement);
        singulars.add(0, new Pair(regexp, javaReplacement));
    }
    
    public void irregular(String singular, String plural) {
        plural("(" + singular.charAt(0) + ")" + singular.substring(1), "\\1" + plural.substring(1));
        singular("(" + plural.charAt(0) + ")" + plural.substring(1), "\\1" + singular.substring(1));
    }
    
    public void uncountable(String word) {
        uncountables.add(word);
    }
    
    public String pluralize(String word) {
        if (uncountables.contains(word))
            return word;
        else {
            for (Pair pair : plurals) {
                Matcher matcher = pair.getPattern().matcher(word);
                if (matcher.find())
                    return matcher.replaceAll(pair.getReplacement());
            }
            return word;
        }
    }
    
    public String singularize(String word) {
        if (uncountables.contains(word))
            return word;
        else {
            for (Pair pair : plurals) {
                Matcher matcher = pair.getPattern().matcher(word);
                if (matcher.find())
                    return matcher.replaceAll(pair.getReplacement());
            }
            return word;
        }
    }
    
}
