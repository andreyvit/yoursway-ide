package com.yoursway.ide.ui.railsview.shit.rails;

import java.util.ArrayList;
import java.util.Collection;

import com.yoursway.ide.ui.railsview.shit.ElementsCategory;
import com.yoursway.ide.ui.railsview.shit.IPresentableItem;
import com.yoursway.ide.ui.railsview.shit.IViewInfoProvider;
import com.yoursway.model.rails.IRailsApplicationProject;
import com.yoursway.model.rails.IRailsMigration;
import com.yoursway.model.repository.IHandle;
import com.yoursway.model.repository.IResolver;

public class MigrationsCategory extends ElementsCategory {
    
 private final IRailsApplicationProject project;
    
    public MigrationsCategory(String name, IViewInfoProvider infoProvider, IRailsApplicationProject project) {
        super(name, infoProvider);
        this.project = project;
    }
    
    @Override
    public int getPriority() {
        return 2;
    }
    
    @Override
    public boolean headerOnly() {
        return false;
    }
    
    public Collection<IPresentableItem> getChildren() {
        IResolver resolver = infoProvider.getModelResolver();
        if (resolver == null)
            return null;
        
        IHandle<Collection<IRailsMigration>> modelsHandle = project.migrations();
        Collection<IRailsMigration> models = resolver.get(modelsHandle);
        ArrayList<IPresentableItem> list = new ArrayList<IPresentableItem>();
        for (IRailsMigration c : models) {
            list.add(new MigrationElement(this, c, infoProvider));
        }
        
        return list;
    }
    
    public boolean hasChildren() {
        return true;
    }
    
}
