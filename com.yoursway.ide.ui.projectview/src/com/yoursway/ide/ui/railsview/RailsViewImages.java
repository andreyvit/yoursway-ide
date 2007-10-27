package com.yoursway.ide.ui.railsview;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import com.yoursway.ide.ui.projectview.Activator;


public class RailsViewImages {
    
    private static final String MODEL_ICONS_PATH = "icons/rails_model/";
    
    public static final ImageDescriptor EMPTY_ICON = Activator.getImageDescriptor(MODEL_ICONS_PATH
            + "empty.png");
    
    public static final ImageDescriptor ACTION_ICON = Activator.getImageDescriptor(MODEL_ICONS_PATH
            + "base.png");
    public static final Image ACTION_ICON_IMG = ACTION_ICON.createImage();
    
    public static final ImageDescriptor CONTROLLER_ICON = Activator.getImageDescriptor(MODEL_ICONS_PATH
            + "controller.png");
    public static final Image CONTROLLER_ICON_IMG = CONTROLLER_ICON.createImage();
    
    public static final ImageDescriptor FIELD_ICON = Activator.getImageDescriptor(MODEL_ICONS_PATH
            + "base.png");
    public static final Image FIELD_ICON_IMG = ACTION_ICON.createImage();
    
    public static final ImageDescriptor MODEL_ICON = Activator.getImageDescriptor(MODEL_ICONS_PATH
            + "model.png");
    public static final Image MODEL_ICON_IMG = MODEL_ICON.createImage();
    
    public static final ImageDescriptor PARTIAL_ICON = Activator.getImageDescriptor(MODEL_ICONS_PATH
            + "partial.png");
    public static final Image PARTIAL_ICON_IMG = PARTIAL_ICON.createImage();
    
    public static final ImageDescriptor PROJECT_ICON = Activator.getImageDescriptor(MODEL_ICONS_PATH
            + "project.gif");
    public static final Image PROJECT_ICON_IMG = PROJECT_ICON.createImage();
    
    public static final ImageDescriptor VIEW_ICON = Activator.getImageDescriptor(MODEL_ICONS_PATH
            + "view.png");
    public static final Image VIEW_ICON_IMG = VIEW_ICON.createImage();
    
    private static final Map<ImageDescriptor, Image> images = Collections
            .synchronizedMap(new HashMap<ImageDescriptor, Image>());
    
    public static Image getImage(ImageDescriptor descriptor) {
        Image image = images.get(descriptor);
        if (image == null) {
            image = descriptor.createImage();
            images.put(descriptor, image);
        }
        return image;
    }
}
