package com.yoursway.ide.ui.railsview.shit.rails;

import java.util.Collection;

import org.eclipse.swt.graphics.Image;

import com.yoursway.ide.ui.railsview.shit.IPresentableItem;
import com.yoursway.ide.ui.railsview.shit.IViewInfoProvider;
import com.yoursway.ide.ui.railsview.shit.SimpleProjectElement;
import com.yoursway.model.rails.IRailsMigration;

public class MigrationElement extends SimpleProjectElement {
    private IRailsMigration migration;
    
    public MigrationElement(IPresentableItem parent, IRailsMigration mig, IViewInfoProvider infoProvider) {
        super(parent, ClassNameUtil.buildName(infoProvider.getModelResolver().get(mig.name())),
                infoProvider);
        this.migration = mig;
    }
    
    public Collection<IPresentableItem> getChildren() {
        return null;
    }
    
    public Image getImage() {
        return null;
    }
    
    public boolean hasChildren() {
        return false;
    }
}
