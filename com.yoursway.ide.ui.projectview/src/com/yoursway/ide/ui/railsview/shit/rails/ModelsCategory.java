package com.yoursway.ide.ui.railsview.shit.rails;

import java.util.ArrayList;
import java.util.Collection;

import com.yoursway.ide.ui.railsview.shit.ElementsCategory;
import com.yoursway.ide.ui.railsview.shit.IPresentableItem;
import com.yoursway.ide.ui.railsview.shit.IViewInfoProvider;
import com.yoursway.model.rails.IRailsApplicationProject;
import com.yoursway.model.rails.IRailsModel;
import com.yoursway.model.repository.IHandle;
import com.yoursway.model.repository.IResolver;

public class ModelsCategory extends ElementsCategory {
    
    private final IRailsApplicationProject project;
    
    public ModelsCategory(String name, IViewInfoProvider infoProvider, IRailsApplicationProject project) {
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
        if (resolver == null)
            return null;
        
        IHandle<Collection<IRailsModel>> modelsHandle = project.models();
        Collection<IRailsModel> models = resolver.get(modelsHandle);
        ArrayList<IPresentableItem> list = new ArrayList<IPresentableItem>();
        for (IRailsModel c : models) {
            list.add(new ModelElement(this, c, infoProvider));
        }
        
        return list;
    }
    
    public boolean hasChildren() {
        return true;
    }
    
}
