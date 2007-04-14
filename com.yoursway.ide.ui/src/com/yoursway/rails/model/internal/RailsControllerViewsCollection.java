package com.yoursway.rails.model.internal;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;

import com.yoursway.ide.ui.Activator;
import com.yoursway.rails.model.IRailsBaseView;
import com.yoursway.rails.model.IRailsController;
import com.yoursway.rails.model.IRailsControllerViewsCollection;
import com.yoursway.utils.PathUtils;
import com.yoursway.utils.StringUtils;

public class RailsControllerViewsCollection implements IRailsControllerViewsCollection {
    
    private Collection<RailsBaseView> items;
    
    private boolean itemsKnown = false;
    
    private final IRailsController controller;
    
    private final IFolder viewsFolder;
    
    public RailsControllerViewsCollection(IRailsController controller) {
        this.controller = controller;
        viewsFolder = calculateViewsFolder();
    }
    
    private void refresh() {
        items = new ArrayList<RailsBaseView>();
        itemsKnown = true;
        if (viewsFolder == null)
            return;
        IResource[] members;
        try {
            members = viewsFolder.members();
        } catch (CoreException e) {
            Activator.log(e);
            return;
        }
        for (IResource member : members)
            if (member.getType() == IResource.FILE && !PathUtils.isIgnoredResourceOrNoExtension(member))
                addView((IFile) member, null);
    }
    
    public Collection<? extends IRailsBaseView> getItems() {
        if (!itemsKnown)
            refresh();
        return items;
    }
    
    public boolean hasItems() {
        return true;
    }
    
    public void reconcile(RailsDeltaBuilder deltaBuilder, IResourceDelta delta) {
        IResourceDelta viewsDelta = delta.findMember(new Path("app/views"));
        if (viewsDelta != null)
            // TODO: interpret delta
            refresh();
    }
    
    private IFolder calculateViewsFolder() {
        IProject project = controller.getRailsProject().getProject();
        String controllerPath = StringUtils.join(controller.getPathComponents(), "/");
        if (controllerPath.endsWith("_controller"))
            controllerPath = controllerPath.substring(0, controllerPath.length() - "_controller".length());
        return PathUtils.getFolder(project, "app/views/" + controllerPath);
    }
    
    private void addView(IFile file, RailsDeltaBuilder deltaBuilder) {
        String baseName = PathUtils.getBaseNameWithoutExtension(file);
        final RailsBaseView view;
        if (!baseName.startsWith("_"))
            view = new RailsView(controller, file, baseName);
        else {
            baseName = baseName.substring(1);
            view = new RailsPartial(controller, file, baseName);
        }
        items.add(view);
    }
    
    public IFolder getViewsFolder() {
        return viewsFolder;
    }
}
