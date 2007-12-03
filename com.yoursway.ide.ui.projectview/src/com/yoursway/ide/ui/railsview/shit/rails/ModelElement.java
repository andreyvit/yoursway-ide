package com.yoursway.ide.ui.railsview.shit.rails;

import java.util.Collection;

import org.eclipse.swt.graphics.Image;

import com.yoursway.ide.ui.railsview.RailsViewImages;
import com.yoursway.ide.ui.railsview.shit.IInPlaceRenameable;
import com.yoursway.ide.ui.railsview.shit.IPresentableItem;
import com.yoursway.ide.ui.railsview.shit.IViewInfoProvider;
import com.yoursway.ide.ui.railsview.shit.SimpleProjectElement;
import com.yoursway.model.rails.IRailsModel;

public class ModelElement extends SimpleProjectElement implements IInPlaceRenameable {
    
    private IRailsModel model;
    
    public ModelElement(IPresentableItem parent, IRailsModel model, IViewInfoProvider infoProvider) {
        super(parent, ClassNameUtil.buildName(infoProvider.getModelResolver().get(model.name())),
                infoProvider);
        this.model = model;
    }
    
    public Collection<IPresentableItem> getChildren() {
        return null;
    }
    
    public Image getImage() {
        return RailsViewImages.MODEL_ICON_IMG;
    }
    
    public boolean hasChildren() {
        return false;
    }

    public String getInitialName() {
        return "ya modelko";
    }

    public void setNewName(String name) {
        // TODO Auto-generated method stub
        
    }
    
}
