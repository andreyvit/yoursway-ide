package com.yoursway.ide.preferences;

import java.util.Map;

public class ColorScheme {
    
    private final String name;
    private final Map<String, String> settings;
    
    public ColorScheme(String name, Map<String, String> settings) {
        this.name = name;
        this.settings = settings;
    }
    
    public String getName() {
        return name;
    }
    
    public Map<String, String> getSettings() {
        return settings;
    }
    
}
