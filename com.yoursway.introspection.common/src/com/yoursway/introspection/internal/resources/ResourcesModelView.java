package com.yoursway.introspection.internal.resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import com.yoursway.introspection.common.AbstractModelMonitoringView;
import com.yoursway.introspection.common.internal.Activator;

public class ResourcesModelView extends AbstractModelMonitoringView {
    public class Change extends AbstractChange {
        
        private final IResourceChangeEvent event;
        
        public Change(IResourceChangeEvent event) {
            this.event = event;
        }
        
        public IResourceChangeEvent getEvent() {
            return event;
        }
        
    }
    
    class ElementChangedListener implements IResourceChangeListener {
        
        public void install() {
            ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
        }
        
        public void resourceChanged(final IResourceChangeEvent event) {
            addElementChangedEvent(new Change(event));
        }
        
        public void uninstall() {
            ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
        }
        
    }
    
    class ViewContentProvider implements IStructuredContentProvider, ITreeContentProvider {
        
        private Object input;
        
        public void dispose() {
        }
        
        public Object[] getChildren(Object parent) {
            if (parent == input) {
                Collection<Object> result = new ArrayList<Object>();
                addRecentChangesTo(result);
                result.add(theModel);
                return result.toArray();
            } else if (parent instanceof IContainer) {
                IContainer p = (IContainer) parent;
                try {
                    IResource[] children = p.members();
                    return children;
                } catch (CoreException e) {
                    Activator.log(e);
                }
            } else if (parent instanceof Change) {
                IResourceChangeEvent event = ((Change) parent).getEvent();
                IResource resource = event.getResource();
                IResourceDelta delta = event.getDelta();
                if (resource != null && delta == null)
                    return new Object[] { resource };
                if (delta != null)
                    return new Object[] { delta };
            } else if (parent instanceof IResourceDelta) {
                IResourceDelta delta = (IResourceDelta) parent;
                Collection<IResourceDelta> result = new ArrayList<IResourceDelta>();
                result.addAll(Arrays.asList(delta.getAffectedChildren()));
                return result.toArray();
            }
            
            return NO_CHILDREN;
        }
        
        public Object[] getElements(Object parent) {
            return getChildren(parent);
        }
        
        public Object getParent(Object child) {
            if (child instanceof IResource) {
                IResource modelElement = (IResource) child;
                return modelElement.getParent();
            } else if (child instanceof Change) {
                return theInput;
            }
            return null;
        }
        
        public boolean hasChildren(Object parent) {
            if (parent instanceof IContainer) {
                IContainer p = (IContainer) parent;
                try {
                    return p.members().length > 0;
                } catch (CoreException e) {
                    Activator.log(e);
                }
            } else if (parent instanceof Change || parent instanceof IResource
                    || parent instanceof IResourceDelta) {
                return getChildren(parent).length > 0;
            }
            return false;
        }
        
        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
            this.input = newInput;
        }
    }
    
    class ViewLabelProvider extends BaseLabelProvider {
        
        @Override
        public Image getImage(Object element) {
            return workbenchProvider.getImage(element);
        }
        
        @Override
        public String getText(Object element) {
            String className = element.getClass().getSimpleName();
            if (element instanceof IResource) {
                return className + ": " + workbenchProvider.getText(element);
            } else if (element instanceof Change) {
                Change change = (Change) element;
                IResourceChangeEvent event = change.getEvent();
                String typeName = ResourcesModelPresentationUtils.changeEventTypeNameToString(event);
                return change.getOrdinal() + ") " + className + ": " + typeName + " at "
                        + change.getTimeMillis();
            } else if (element instanceof IResourceDelta) {
                IResourceDelta delta = (IResourceDelta) element;
                IResource modelElement = delta.getResource();
                StringBuilder result = new StringBuilder();
                result.append(ResourcesModelPresentationUtils.deltaKindToString(delta)).append(' ').append(
                        className).append(' ').append(modelElement.getClass().getSimpleName()).append(' ')
                        .append(modelElement.getName());
                ResourcesModelPresentationUtils.appendDeltaFlags(result, delta);
                return result.toString();
            }
            return element.getClass().getSimpleName() + " - " + element.toString();
        }
        
    }
    
    private final ILabelProvider workbenchProvider = WorkbenchLabelProvider
            .getDecoratingWorkbenchLabelProvider();
    
    private final ElementChangedListener elementChangedListener = new ElementChangedListener();
    
    protected IWorkspaceRoot theModel;
    
    /**
     * The constructor.
     */
    public ResourcesModelView() {
    }
    
    @Override
    public void dispose() {
        super.dispose();
        elementChangedListener.uninstall();
    }
    
    private void visit(IResourceDelta delta) {
        IResource resource = delta.getResource();
        reportChangedElement(resource);
        visit(delta.getAffectedChildren());
    }
    
    private void visit(IResourceDelta[] addedChildren) {
        for (int i = 0; i < addedChildren.length; i++) {
            visit(addedChildren[i]);
        }
    }
    
    @Override
    protected void connectToModel() {
        theModel = ResourcesPlugin.getWorkspace().getRoot();
        elementChangedListener.install();
    }
    
    @Override
    protected void findChangedItems(AbstractChange change) {
        visit(((Change) change).getEvent().getDelta());
    }
    
    @Override
    protected Object getModelRoot() {
        return theModel;
    }
    
    @Override
    protected void handleDoubleClick(Object firstElement) {
        if (firstElement instanceof IFile) {
            IFile file = (IFile) firstElement;
            try {
                IDE.openEditor(getSite().getPage(), file);
            } catch (PartInitException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    protected void refreshModel() {
    }
    
    @Override
    protected void setupTreeViewer(TreeViewer viewer) {
        viewer.setContentProvider(new ViewContentProvider());
        viewer.setLabelProvider(new ViewLabelProvider());
    }
}