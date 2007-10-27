package com.yoursway.ide.ui.railsview.shit.rails;

import org.eclipse.swt.graphics.Image;

import com.yoursway.ide.ui.railsview.shit.IPresentableItem;
import com.yoursway.ide.ui.railsview.shit.ISearchPatternProvider;
import com.yoursway.ide.ui.railsview.shit.SimpleProjectElement;

public class ControllerElement extends SimpleProjectElement {
    
    public ControllerElement(ControllersCategory parent, String name, ISearchPatternProvider searchProvider) {
        super(parent, name, searchProvider);
    }
    
    public IPresentableItem[] getChildren() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public Image getImage() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public boolean hasChildren() {
        // TODO Auto-generated method stub
        return false;
    }
    
}
