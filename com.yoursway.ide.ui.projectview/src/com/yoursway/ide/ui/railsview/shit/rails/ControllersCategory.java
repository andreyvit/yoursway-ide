package com.yoursway.ide.ui.railsview.shit.rails;

import java.util.ArrayList;
import java.util.Collection;

import com.yoursway.ide.ui.railsview.shit.ElementsCategory;
import com.yoursway.ide.ui.railsview.shit.IPresentableItem;
import com.yoursway.ide.ui.railsview.shit.IViewInfoProvider;
import com.yoursway.model.rails.IRailsApplicationProject;
import com.yoursway.model.rails.IRailsController;
import com.yoursway.model.repository.IHandle;
import com.yoursway.model.repository.IResolver;

public class ControllersCategory extends ElementsCategory {
    
    private final IRailsApplicationProject project;
    
    public ControllersCategory(String name, IViewInfoProvider infoProvider, IRailsApplicationProject project) {
        super(name, infoProvider);
        this.project = project;
    }
    
    @Override
    public int getPriority() {
        return 1;
    }
    
    @Override
    public boolean headerOnly() {
        return true;
    }
    
    public Collection<IPresentableItem> getChildren() {
        IResolver resolver = infoProvider.getModelResolver();
        IHandle<Collection<IRailsController>> controllersHandle = project.controllers();
        Collection<IRailsController> controllers = resolver.get(controllersHandle);
        ArrayList<IPresentableItem> list = new ArrayList<IPresentableItem>();
        for (IRailsController c : controllers) {
            list.add(new ControllerElement(this, c, infoProvider));
        }
        
        return list;
    }
    
    public boolean hasChildren() {
        return true;
    }
    
}
