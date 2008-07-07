package com.yoursway.experiments.birdseye.component;

import static com.google.common.collect.Maps.newHashMap;
import static com.yoursway.utils.YsPathUtils.extension;
import static com.yoursway.utils.YsStrings.emptyToNull;

import java.io.File;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import com.yoursway.experiments.birdseye.model.Leaf;
import com.yoursway.experiments.birdseye.model.Node;

public class Factory {
    
    private final Display display;
    
    private Color defaultColor;
    
    private Map<String, Color> extToColor = newHashMap();
    
    public Factory(Display display) {
        this.display = display;
        defaultColor = display.getSystemColor(SWT.COLOR_CYAN);
    }
    
    public void associate(String ext, int color) {
        associate(ext, display.getSystemColor(color));
    }
    
    public void associate(String ext, Color color) {
        if (ext == null)
            throw new NullPointerException("ext is null");
        if (color == null)
            throw new NullPointerException("color is null");
        
        extToColor.put(ext.toLowerCase(), color);
    }
    
    public Node createLeaf(File file, String path) {
        return new Leaf(Math.log(1 + file.length()), colorFor(file), path);
    }
    
    private Color colorFor(File file) {
        String ext = emptyToNull(extension(file));
        Color color = (ext == null ? null : extToColor.get(ext.toLowerCase()));
        if (color == null)
            return defaultColor;
        else
            return color;
    }
    
}