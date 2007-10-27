package com.yoursway.ide.ui.railsview.shit.rails;

import com.yoursway.ide.ui.railsview.shit.ElementsCategory;
import com.yoursway.ide.ui.railsview.shit.IPresentableItem;
import com.yoursway.ide.ui.railsview.shit.ISearchPatternProvider;

public class ControllersCategory extends ElementsCategory {
    
    public ControllersCategory(String name, ISearchPatternProvider searchProvider) {
        super(name, searchProvider);
    }
    
    @Override
    public int getPriority() {
        return 1;
    }
    
    public IPresentableItem[] getChildren() {
        return new IPresentableItem[] { new ControllerElement(this, "shit", searchProvider),
                new ControllerElement(this, "shit2", searchProvider) };
    }
    
    public boolean hasChildren() {
        return true;
    }
    
}
