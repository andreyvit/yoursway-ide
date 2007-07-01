package com.yoursway.ide.preferences;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.dltk.ruby.internal.ui.RubyPreferenceConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.osgi.framework.Bundle;

import com.yoursway.ide.ui.Activator;

public class ColorSchemeManager {
    
    private static class Section {
        private final Class<?> constantsClass;
        private final IPreferenceStore preferenceStore;
        private final String name;
        
        public Section(String name, Class<?> constantsClass, IPreferenceStore preferenceStore) {
            this.name = name;
            this.constantsClass = constantsClass;
            this.preferenceStore = preferenceStore;
        }
        
        public String getName() {
            return name;
        }
        
        public void applyPreference(String key, String value) {
            preferenceStore.setValue(key, value);
        }
        
        public Map<String, String> retrievePreferences() {
            Map<String, String> props = new HashMap<String, String>();
            Field[] fields = constantsClass.getFields();
            Class<String> stringClass = String.class;
            final int correctModifiers = Modifier.STATIC | Modifier.FINAL;
            for (Field field : fields) {
                int modifiers = field.getModifiers();
                if ((modifiers & correctModifiers) == correctModifiers) {
                    if (field.getType().equals(stringClass)) {
                        try {
                            String key = (String) field.get(null);
                            String value = preferenceStore.getString(key);
                            if (value != null)
                                if (COLOR_PATTERN.matcher(value).find()) {
                                    props.put(key, value);
                                }
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return props;
        }
        
    }
    
    private static Section[] getSections() {
        
        return new Section[] {
                new Section("rubyui", RubyPreferenceConstants.class,
                        getPluginPreferenceStore("org.eclipse.dltk.ruby.ui")),
                new Section("texteditor", AbstractTextEditor.class,
                        getPluginPreferenceStore("org.eclipse.ui.editors")),
                new Section("decoratedtexteditor", AbstractDecoratedTextEditorPreferenceConstants.class,
                        getPluginPreferenceStore("org.eclipse.ui.editors")), };
    }
    
    private static Map<String, Section> getSectionsMap() {
        Map<String, Section> result = new HashMap<String, Section>();
        for (Section section : getSections())
            result.put(section.getName(), section);
        return result;
    }
    
    private static ScopedPreferenceStore getPluginPreferenceStore(String pluginId) {
        return new ScopedPreferenceStore(new InstanceScope(), pluginId);
    }
    
    public static ColorScheme getCurrentScheme() {
        Map<String, String> props = new HashMap<String, String>();
        for (Section section : getSections()) {
            Map<String, String> localPrefs = section.retrievePreferences();
            for (Map.Entry<String, String> entry : localPrefs.entrySet())
                props.put(section.getName() + "/" + entry.getKey(), entry.getValue());
        }
        return new ColorScheme("(Current)", props);
    }
    
    public static void apply(ColorScheme colorScheme) {
        Map<String, Section> sectionsMap = getSectionsMap();
        for (Map.Entry<String, String> entry : colorScheme.getSettings().entrySet()) {
            String[] sectionAndKey = entry.getKey().split(Pattern.quote("/"), 2);
            String sectionName = sectionAndKey[0];
            Section section = sectionsMap.get(sectionName);
            if (section == null)
                System.err.println("No more section named " + sectionName);
            else
                section.applyPreference(sectionAndKey[1], entry.getValue());
        }
    }
    
    private static final Pattern COLOR_PATTERN = Pattern.compile("^\\d+,\\d+,\\d+$");
    
    @SuppressWarnings("unchecked")
    public static List<ColorScheme> getColorSchemes() {
        List<ColorScheme> colorSchemes = new ArrayList<ColorScheme>();
        Bundle bundle = Activator.getDefault().getBundle();
        Enumeration<String> items = bundle.getEntryPaths("resources/color_schemes/");
        while (items.hasMoreElements()) {
            String entryName = items.nextElement();
            URL entry = bundle.getEntry(entryName);
            ColorScheme cs = null;
            try {
                cs = loadColorScheme(entry.openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (cs != null)
                colorSchemes.add(cs);
        }
        return colorSchemes;
    }
    
    public static ColorScheme loadColorScheme(InputStream inputStream) {
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
            String name = (String) properties.remove("name");
            ColorScheme colorScheme = new ColorScheme(name, convertToStringMap(properties));
            return colorScheme;
        } catch (IOException e) {
            Activator.unexpectedError(e);
            return null;
        }
    }
    
    private static Map<String, String> convertToStringMap(Properties properties) {
        Map<String, String> props = new HashMap<String, String>();
        for (Map.Entry<?, ?> item : properties.entrySet())
            props.put((String) item.getKey(), (String) item.getValue());
        return props;
    }
    
}
