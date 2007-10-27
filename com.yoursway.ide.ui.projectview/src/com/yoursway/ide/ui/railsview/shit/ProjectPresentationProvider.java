package com.yoursway.ide.ui.railsview.shit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TreeItem;

import com.yoursway.ide.ui.railsview.shit.rails.ControllersCategory;

public class ProjectPresentationProvider implements ITreeContentProvider, ILabelProvider, Listener {
    
    private final ISearchPatternProvider searchProvider;
    private boolean showAllResults;
    
    public ProjectPresentationProvider(ISearchPatternProvider searchProvider) {
        this.searchProvider = searchProvider;
        this.showAllResults = false;
    }
    
    public boolean isShowAllResults() {
        return showAllResults;
    }
    
    public void setShowAllResults(boolean showAllResults) {
        this.showAllResults = showAllResults;
    }
    
    public Object[] getChildren(Object parentElement) {
        if (!(parentElement instanceof IPresentableItem)) {
            return new Object[0];
        }
        IPresentableItem item = (IPresentableItem) parentElement;
        String pattern = searchProvider.getPattern();
        if (pattern == null || pattern.length() == 0) {
            return item.getChildren();
        } else {
            
        }
        return new Object[0];
    }
    
    public Object getParent(Object element) {
        if (element instanceof ProjectElement) {
            ProjectElement projectElement = (ProjectElement) element;
            return projectElement.getParent();
        }
        return null;
    }
    
    public boolean hasChildren(Object element) {
        Object[] children = getChildren(element);
        return children != null && children.length > 0;
    }
    
    public Object[] getElements(Object inputElement) {
        // TODO: handle actual categories here
        ArrayList<ElementsCategory> list = new ArrayList<ElementsCategory>();
        list.add(new ControllersCategory("Controllers", searchProvider));
        Collections.sort(list, new Comparator<ElementsCategory>() {
            
            public int compare(ElementsCategory o1, ElementsCategory o2) {
                return o1.getPriority() - o2.getPriority();
            }
            
        });
        return list.toArray(new ElementsCategory[list.size()]);
    }
    
    public void dispose() {
    }
    
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }
    
    public Image getImage(Object element) {
        if (element instanceof IPresentableItem) {
            IPresentableItem presentableItem = (IPresentableItem) element;
            return presentableItem.getImage();
        }
        return null;
    }
    
    public String getText(Object element) {
        if (element instanceof IPresentableItem) {
            IPresentableItem presentableItem = (IPresentableItem) element;
            return presentableItem.getCaption();
        }
        return null;
    }
    
    public void addListener(ILabelProviderListener listener) {
    }
    
    public boolean isLabelProperty(Object element, String property) {
        return false;
    }
    
    public void removeListener(ILabelProviderListener listener) {
    }
    
    public void handleEvent(Event event) {
        TreeItem item = (TreeItem) event.item;
        if (item.getData() instanceof IPresentableItem) {
            IPresentableItem presentable = (IPresentableItem) item.getData();
            switch (event.type) {
            case SWT.PaintItem:
                presentable.paintItem(item, event);
                break;
            case SWT.MeasureItem:
                presentable.measureItem(item, event);
                break;
            case SWT.EraseItem:
                presentable.eraseItem(item, event);
                break;
            }
        }
    }
    
    public void install(TreeViewer viewer) {
        viewer.setContentProvider(this);
        viewer.setLabelProvider(this);
        viewer.getTree().addListener(SWT.PaintItem, this);
        viewer.getTree().addListener(SWT.MeasureItem, this);
        viewer.getTree().addListener(SWT.EraseItem, this);
    }
    
}
