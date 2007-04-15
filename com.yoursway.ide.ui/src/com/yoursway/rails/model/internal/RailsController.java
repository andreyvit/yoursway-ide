package com.yoursway.rails.model.internal;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.dltk.core.IType;

import com.yoursway.rails.model.Caching;
import com.yoursway.rails.model.IRailsController;
import com.yoursway.rails.model.IRailsControllerActionsCollection;
import com.yoursway.rails.model.IRailsControllerViewsCollection;
import com.yoursway.rails.model.IRailsControllersCollection;
import com.yoursway.rails.model.IRailsProject;
import com.yoursway.utils.PathUtils;
import com.yoursway.utils.RailsNamingConventions;

public class RailsController implements IRailsController {
    
    private final IRailsControllersCollection parent;
    private final IFile file;
    private IType controllerType;
    private final RailsControllerActionsCollection actionsCollection;
    private final RailsControllerViewsCollection viewsCollection;
    private final String[] pathComponents;
    private final String[] className;
    private boolean typeKnown = false;
    
    public RailsController(IRailsControllersCollection parent, IFile file) {
        this.parent = parent;
        this.file = file;
        
        pathComponents = PathUtils.determinePathComponents(parent.getControllersFolder(), file);
        className = RailsNamingConventions.camelize(pathComponents);
        if (className.length == 1 && className[0].equals("Application"))
            className[0] = "ApplicationController";
        
        actionsCollection = new RailsControllerActionsCollection(this);
        viewsCollection = new RailsControllerViewsCollection(this);
    }
    
    public IFile getFile() {
        return file;
    }
    
    public IType getCorrespondingType(Caching caching) {
        if (!typeKnown && caching != Caching.CACHED_ONLY) {
            controllerType = RailsFileUtils.findControllerTypeInFile(file);
            typeKnown = true;
        }
        return controllerType;
    }
    
    public String[] getExpectedClassName() {
        return className;
    }
    
    private void resetType() {
        typeKnown = false;
        controllerType = null;
    }
    
    public void reconcile(RailsDeltaBuilder deltaBuilder, IResourceDelta delta) {
        resetType();
        actionsCollection.reconcile(deltaBuilder);
        viewsCollection.reconcile(deltaBuilder, delta);
        deltaBuilder.somethingChanged();
    }
    
    public IRailsControllerActionsCollection getActionsCollection() {
        return actionsCollection;
    }
    
    public IRailsControllerViewsCollection getViewsCollection() {
        return viewsCollection;
    }
    
    public String[] getPathComponents() {
        return pathComponents;
    }
    
    public IRailsProject getRailsProject() {
        return parent.getRailsProject();
    }
    
}
