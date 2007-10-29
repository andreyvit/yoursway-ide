package com.yoursway.ide.ui.railsview.shit;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

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
        if (item instanceof ElementsCategory) {
            ElementsCategory elementsCategory = (ElementsCategory) item;
            if (elementsCategory.headerOnly())
                return new Object[0];
        }
        String pattern = searchProvider.getPattern();
        if (pattern == null || pattern.length() == 0) {
            Collection<IPresentableItem> children = item.getChildren();
            if (children == null)
                return null;
            return children.toArray(new IPresentableItem[children.size()]);
        } else {
            Collection<IPresentableItem> children = item.getChildren(); //BIGTODO
            if (children == null)
                return null;
            for (Iterator<IPresentableItem> iterator = children.iterator(); iterator.hasNext();) {
                IPresentableItem i = (IPresentableItem) iterator.next();
                if (hasMatchingChild(i, pattern))
                    continue;
                int matches = i.matches(pattern);
                if (matches < 0) {
                    iterator.remove();
                }
            }
            return children.toArray(new IPresentableItem[children.size()]);
        }
    }
    
    private boolean hasMatchingChild(IPresentableItem item, String pattern) { //TODO : performance optimizations
        Collection<IPresentableItem> children = item.getChildren();
        if (children == null)
            return false;
        Queue<IPresentableItem> q = new LinkedList<IPresentableItem>();
        q.addAll(children);
        while (!q.isEmpty()) {
            IPresentableItem next = q.poll();
            if (next.matches(pattern) >= 0)
                return true;
            Collection<IPresentableItem> ch2 = next.getChildren();
            if (ch2 != null)
                q.addAll(ch2);
        }
        return false;
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
        return getChildren(inputElement);
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
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
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
