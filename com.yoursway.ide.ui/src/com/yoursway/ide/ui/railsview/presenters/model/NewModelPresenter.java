/**
 * 
 */
package com.yoursway.ide.ui.railsview.presenters.model;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Event;

import com.yoursway.ide.ui.railsview.RailsViewImages;
import com.yoursway.ide.ui.railsview.presentation.AbstractPresenter;
import com.yoursway.ide.ui.railsview.presentation.IContextMenuContext;
import com.yoursway.ide.ui.railsview.presentation.IPresenterOwner;
import com.yoursway.ide.ui.railsview.presentation.IProvidesTreeItem;
import com.yoursway.ide.ui.railsview.presenters.IRenameContext;
import com.yoursway.ide.ui.railsview.presenters.RenameContextAdapter;
import com.yoursway.rails.models.project.RailsProject;
import com.yoursway.utils.RailsNamingConventions;
import com.yoursway.utils.StringUtils;

@Deprecated
public class NewModelPresenter extends AbstractPresenter {
    
    private final RailsProject railsProject;
    
    private final class Context extends RenameContextAdapter {
        
        public Context(IPresenterOwner presenterOwner, IProvidesTreeItem contextMenuContext) {
            super(presenterOwner, contextMenuContext);
        }
        
        public String getInitialValue() {
            return "";
        }
        
        public boolean isValidValue(String value) {
            return true;
        }
        
        public void setValue(String value) {
            String className = RailsNamingConventions.joinNamespaces(RailsNamingConventions
                    .camelize(RailsNamingConventions.splitPath(value)));
            
            final String fileName = StringUtils.join(RailsNamingConventions.underscore(RailsNamingConventions
                    .splitNamespaces(className)), "/");
            
            final String body = "\n" + "class " + className + " < ActiveRecord::Base\n" + "end\n";
            
            //            final IFolder folder = railsProject.getModelsCollection().getModelsFolder();
            //            
            //            new Job("Creating model") {
            //                
            //                @Override
            //                protected IStatus run(IProgressMonitor monitor) {
            //                    final IFile file = createFile(body, folder, fileName + ".rb", "Model creation failed");
            //                    if (file != null) {
            //                        Display.getDefault().asyncExec(new Runnable() {
            //                            public void run() {
            //                                openEditor(file);
            //                            }
            //                        });
            //                    }
            //                    return Status.OK_STATUS;
            //                }
            //                
            //            }.schedule();
        }
    }
    
    public final static class NewAction extends Action {
        private final IRenameContext renameContext;
        private ModelCreationMode renameMode;
        
        private NewAction(IRenameContext renameContext) {
            super("New Model");
            this.renameContext = renameContext;
        }
        
        @Override
        public void runWithEvent(Event event) {
            run();
        }
        
        @Override
        public void run() {
            renameMode = new ModelCreationMode(renameContext);
            renameMode.enterMode();
        }
    }
    
    public NewModelPresenter(IPresenterOwner owner, RailsProject railsProject) {
        super(owner);
        this.railsProject = railsProject;
    }
    
    public boolean canEditInPlace() {
        return true;
    }
    
    public String getCaption() {
        return "New Model (double-click to create)";
    }
    
    public Object[] getChildren() {
        Collection<Object> children = new ArrayList<Object>();
        return children.toArray();
    }
    
    public ImageDescriptor getImage() {
        return RailsViewImages.EMPTY_ICON;
    }
    
    public Object getParent() {
        return railsProject;
    }
    
    public boolean hasChildren() {
        return false;
    }
    
    public void handleDoubleClick(IProvidesTreeItem context) {
        NewAction action = new NewAction(new Context(getOwner(), context));
        action.run();
    }
    
    public void fillContextMenu(final IContextMenuContext context) {
        context.getMenuManager().add(new NewAction(new Context(getOwner(), context)));
    }
}
