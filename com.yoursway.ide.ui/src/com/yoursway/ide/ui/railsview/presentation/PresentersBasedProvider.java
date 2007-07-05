package com.yoursway.ide.ui.railsview.presentation;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TreeItem;

import com.yoursway.ide.ui.railsview.RailsViewImages;

public class PresentersBasedProvider implements ILabelProvider, IFontProvider, IColorProvider,
        ITreeContentProvider {
    
    private final IPresenterFactory presenterFactory;
    
    public PresentersBasedProvider(IPresenterFactory presenterFactory) {
        this.presenterFactory = presenterFactory;
    }
    
    public Image getImage(Object element) {
        ImageDescriptor descr = presenterFactory.createPresenter(element).getImage();
        return RailsViewImages.getImage(descr);
    }
    
    public String getText(Object element) {
        return presenterFactory.createPresenter(element).getCaption();
    }
    
    public void addListener(ILabelProviderListener listener) {
    }
    
    public void dispose() {
    }
    
    public boolean isLabelProperty(Object element, String property) {
        return false;
    }
    
    public void removeListener(ILabelProviderListener listener) {
    }
    
    public Object[] getChildren(Object parentElement) {
        return presenterFactory.createPresenter(parentElement).getChildren();
    }
    
    public Object getParent(Object element) {
        return presenterFactory.createPresenter(element).getParent();
    }
    
    public boolean hasChildren(Object element) {
        return presenterFactory.createPresenter(element).hasChildren();
    }
    
    public Object[] getElements(Object element) {
        return presenterFactory.createPresenter(element).getChildren();
    }
    
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }
    
    public Font getFont(Object element) {
        return null;
    }
    
    public Color getBackground(Object element) {
        return null;
    }
    
    public Color getForeground(Object element) {
        return null;
    }
    
    public void measureItem(TreeItem item, Object element, Event event) {
        presenterFactory.createPresenter(element).measureItem(item, element, event);
    }
    
    public void eraseItem(TreeItem item, Object element, Event event) {
        presenterFactory.createPresenter(element).eraseItem(item, element, event);
    }
    
    public void paintItem(TreeItem item, Object element, Event event) {
        presenterFactory.createPresenter(element).paintItem(item, element, event);
    }
    
}
