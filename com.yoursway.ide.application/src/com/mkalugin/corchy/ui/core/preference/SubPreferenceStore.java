package com.mkalugin.corchy.ui.core.preference;

import static com.yoursway.utils.Listeners.newListenersByIdentity;

import com.yoursway.utils.Listeners;

public class SubPreferenceStore implements IPreferenceStore, IPropertyChangeListener {
    
    private final IPreferenceStore parent;
    
    private final String section;
    
    private transient Listeners<IPropertyChangeListener> listeners = newListenersByIdentity();
    
    public SubPreferenceStore(IPreferenceStore parent, String section) {
        if (parent == null)
            throw new NullPointerException("parent is null");
        if (section == null)
            throw new NullPointerException("section is null");
        this.parent = parent;
        this.section = section;
        parent.addPropertyChangeListener(this);
    }
    
    private String wrap(String name) {
        return section + "." + name;
    }
    
    public void addPropertyChangeListener(IPropertyChangeListener listener) {
        listeners.add(listener);
    }
    
    public boolean contains(String name) {
        return parent.contains(wrap(name));
    }
    
    public void firePropertyChangeEvent(String name, Object oldValue, Object newValue) {
        parent.firePropertyChangeEvent(wrap(name), oldValue, newValue);
    }
    
    public boolean getBoolean(String name) {
        return parent.getBoolean(wrap(name));
    }
    
    public boolean getDefaultBoolean(String name) {
        return parent.getDefaultBoolean(wrap(name));
    }
    
    public double getDefaultDouble(String name) {
        return parent.getDefaultDouble(wrap(name));
    }
    
    public float getDefaultFloat(String name) {
        return parent.getDefaultFloat(wrap(name));
    }
    
    public int getDefaultInt(String name) {
        return parent.getDefaultInt(wrap(name));
    }
    
    public long getDefaultLong(String name) {
        return parent.getDefaultLong(wrap(name));
    }
    
    public String getDefaultString(String name) {
        return parent.getDefaultString(wrap(name));
    }
    
    public double getDouble(String name) {
        return parent.getDouble(wrap(name));
    }
    
    public float getFloat(String name) {
        return parent.getFloat(wrap(name));
    }
    
    public int getInt(String name) {
        return parent.getInt(wrap(name));
    }
    
    public long getLong(String name) {
        return parent.getLong(wrap(name));
    }
    
    public String getString(String name) {
        return parent.getString(wrap(name));
    }
    
    public boolean isDefault(String name) {
        return parent.isDefault(wrap(name));
    }
    
    public boolean needsSaving() {
        return parent.needsSaving();
    }
    
    public void putValue(String name, String value) {
        parent.putValue(wrap(name), value);
    }
    
    public void removePropertyChangeListener(IPropertyChangeListener listener) {
        listeners.remove(listener);
    }
    
    public void setDefault(String name, boolean value) {
        parent.setDefault(wrap(name), value);
    }
    
    public void setDefault(String name, double value) {
        parent.setDefault(wrap(name), value);
    }
    
    public void setDefault(String name, float value) {
        parent.setDefault(wrap(name), value);
    }
    
    public void setDefault(String name, int value) {
        parent.setDefault(wrap(name), value);
    }
    
    public void setDefault(String name, long value) {
        parent.setDefault(wrap(name), value);
    }
    
    public void setDefault(String name, String defaultObject) {
        parent.setDefault(wrap(name), defaultObject);
    }
    
    public void setToDefault(String name) {
        parent.setToDefault(wrap(name));
    }
    
    public void setValue(String name, boolean value) {
        parent.setValue(wrap(name), value);
    }
    
    public void setValue(String name, double value) {
        parent.setValue(wrap(name), value);
    }
    
    public void setValue(String name, float value) {
        parent.setValue(wrap(name), value);
    }
    
    public void setValue(String name, int value) {
        parent.setValue(wrap(name), value);
    }
    
    public void setValue(String name, long value) {
        parent.setValue(wrap(name), value);
    }
    
    public void setValue(String name, String value) {
        parent.setValue(wrap(name), value);
    }
    
    public void propertyChange(PropertyChangeEvent event) {
        String property = event.getProperty();
        if (!property.startsWith(section + "."))
            return;
        PropertyChangeEvent newEvent = new PropertyChangeEvent(event.getSource(), property.substring(section
                .length() + 1), event.getOldValue(), event.getNewValue());
        for (IPropertyChangeListener listener : listeners)
            listener.propertyChange(newEvent);
        
    }
    
}
