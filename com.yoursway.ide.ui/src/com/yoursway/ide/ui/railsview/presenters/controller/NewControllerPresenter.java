/**
 * 
 */
package com.yoursway.ide.ui.railsview.presenters.controller;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;

import com.yoursway.ide.ui.railsview.RailsViewImages;
import com.yoursway.ide.ui.railsview.presentation.AbstractPresenter;
import com.yoursway.ide.ui.railsview.presentation.IContextMenuContext;
import com.yoursway.ide.ui.railsview.presentation.IPresenterOwner;
import com.yoursway.ide.ui.railsview.presentation.IProvidesTreeItem;
import com.yoursway.ide.ui.railsview.presenters.IRenameContext;
import com.yoursway.ide.ui.railsview.presenters.RenameContextAdapter;
import com.yoursway.rails.model.IRailsProject;
import com.yoursway.utils.RailsNamingConventions;
import com.yoursway.utils.StringUtils;

public class NewControllerPresenter extends AbstractPresenter {
    
    private final IRailsProject railsProject;
    
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
            if (!className.endsWith("Controller"))
                className += "Controller";
            
            final String fileName = StringUtils.join(RailsNamingConventions.underscore(RailsNamingConventions
                    .splitNamespaces(className)), "/");
            
            final String body = "\n" + "class " + className + " < ApplicationController\n" + "end\n";
            
            final IFolder folder = railsProject.getControllersCollection().getRootFolder()
                    .getCorrespondingFolder();
            
            new Job("Creating controller") {
                
                @Override
                protected IStatus run(IProgressMonitor monitor) {
                    final IFile file = createFile(body, folder, fileName + ".rb",
                            "Controller creation failed");
                    if (file != null) {
                        Display.getDefault().asyncExec(new Runnable() {
                            public void run() {
                                openEditor(file);
                            }
                        });
                    }
                    return Status.OK_STATUS;
                }
                
            }.schedule();
        }
    }
    
    public final static class NewAction extends Action {
        private final IRenameContext renameContext;
        private ControllerCreationMode renameMode;
        
        private NewAction(IRenameContext renameContext) {
            super("New Controller");
            this.renameContext = renameContext;
        }
        
        @Override
        public void runWithEvent(Event event) {
            run();
        }
        
        @Override
        public void run() {
            renameMode = new ControllerCreationMode(renameContext);
            renameMode.enterMode();
        }
    }
    
    public NewControllerPresenter(IPresenterOwner owner, IRailsProject railsProject) {
        super(owner);
        this.railsProject = railsProject;
    }
    
    public boolean canEditInPlace() {
        return true;
    }
    
    public String getCaption() {
        return "New Controller (double-click to create)";
    }
    
    public Object[] getChildren() {
        Collection<Object> children = new ArrayList<Object>();
        return children.toArray();
    }
    
    public ImageDescriptor getImage() {
        return RailsViewImages.ACTION_ICON;
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
