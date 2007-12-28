package com.yoursway.ide.ui.railsview.shit.rails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TreeItem;

import com.yoursway.ide.ui.railsview.shit.ElementsCategory;
import com.yoursway.ide.ui.railsview.shit.IPresentableItem;
import com.yoursway.ide.ui.railsview.shit.IViewInfoProvider;
import com.yoursway.model.rails.IRailsApplicationProject;

public class FakeProjectItem implements IPresentableItem {
    
    private final IRailsApplicationProject project;
    private final IViewInfoProvider infoProvider;
    
    public FakeProjectItem(IRailsApplicationProject project, IViewInfoProvider info) {
        this.project = project;
        this.infoProvider = info;
    }
    
    public String getCaption() {
        throw new UnsupportedOperationException();
    }
    
    private void addCategory(List<IPresentableItem> list, ElementsCategory category) {
        list.add(category);
        if (category.headerOnly()) {
            list.addAll(category.getChildren());
        }
    }
    
    public Collection<IPresentableItem> getChildren() {
        ArrayList<IPresentableItem> list = new ArrayList<IPresentableItem>();
        addCategory(list, new ControllersCategory("Controllers", infoProvider, project));
        addCategory(list, new ModelsCategory("Models", infoProvider, project));
        addCategory(list, new MigrationsCategory("Migrations", infoProvider, project));
        // TODO: add more 
        
        return list;
    }
    
    public Image getImage() {
        throw new UnsupportedOperationException();
    }
    
    public boolean hasChildren() {
        return true;
    }
    
    public int matches(String pattern) {
        throw new UnsupportedOperationException();
    }
    
    public void measureItem(TreeItem item, Event event) {
        throw new UnsupportedOperationException();
    }
    
    public void paintItem(TreeItem item, Event event) {
        throw new UnsupportedOperationException();
    }
    
    public void eraseItem(TreeItem item, Event event) {
        throw new UnsupportedOperationException();
    }

    public void fillContextMenu(IMenuManager menuManager) {
        // TODO Auto-generated method stub
        
    }
}