package com.yoursway.genericmodel.ui;

import java.util.Collection;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

import com.yoursway.model.repository.BackgroundConsumer;
import com.yoursway.model.repository.IHandle;
import com.yoursway.model.repository.IRepository;
import com.yoursway.model.repository.IResolver;

public class ViewModelContentProvider implements ITreeContentProvider {
    
    private BackgroundConsumer consumer;

    private final IHandle<Collection<INodeElement>> rootElementsH;

    private TreeViewer viewer;

    public ViewModelContentProvider(IRepository repository, IHandle<Collection<INodeElement>> rootElements) {
        this.rootElementsH = rootElements;
        consumer = new BackgroundConsumer(repository) {

            @Override
            protected void somethingChanged(IResolver resolver) {
                viewer.refresh();
            }
            
        };
    }
    
    protected IResolver resolver() {
        return consumer.resolver();
    }

    public Object[] getChildren(Object element) {
        return childrenOf(element).toArray();
    }

    public Object getParent(Object element) {
        INodeElement parentNode = (INodeElement) element;
        return parentNode.getParent();
    }

    public boolean hasChildren(Object element) {
        return !childrenOf(element).isEmpty();
    }

    private Collection<INodeElement> childrenOf(Object element) {
        final IResolver resolver = resolver();
        INodeElement parentNode = (INodeElement) element;
        Collection<INodeElement> children = resolver.get(parentNode.children());
        return children;
    }

    public Object[] getElements(Object inputElement) {
        IResolver resolver = resolver();
        Collection<INodeElement> rootElements = resolver.get(rootElementsH);
        return rootElements.toArray();
    }

    public void dispose() {
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        this.viewer = (TreeViewer) viewer;
    }

    
}
