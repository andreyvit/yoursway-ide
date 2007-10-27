package com.yoursway.ide.ui.railsview.shit.rails;

import java.util.Collection;

import org.eclipse.swt.graphics.Image;

import com.yoursway.ide.ui.railsview.RailsViewImages;
import com.yoursway.ide.ui.railsview.shit.IPresentableItem;
import com.yoursway.ide.ui.railsview.shit.IViewInfoProvider;
import com.yoursway.ide.ui.railsview.shit.SimpleProjectElement;
import com.yoursway.model.rails.IRailsControllerAction;

public class ActionElement extends SimpleProjectElement {
    
    private final IRailsControllerAction action;
    
    public ActionElement(IPresentableItem parent, IRailsControllerAction action,
            IViewInfoProvider infoProvider) {
        super(parent, infoProvider.getModelResolver().get(action.getName()), infoProvider);
        this.action = action;
    }
    
    public Collection<IPresentableItem> getChildren() {
        return null;
    }
    
    public Image getImage() {
        return RailsViewImages.ACTION_ICON_IMG;
    }
    
    public boolean hasChildren() {
        return false;
    }
    
}
