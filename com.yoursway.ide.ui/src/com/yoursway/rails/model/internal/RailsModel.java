package com.yoursway.rails.model.internal;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.dltk.core.IType;

import com.yoursway.rails.model.Caching;
import com.yoursway.rails.model.IRailsModel;
import com.yoursway.rails.model.IRailsProject;
import com.yoursway.utils.PathUtils;
import com.yoursway.utils.RailsNamingConventions;

public class RailsModel implements IRailsModel {
    
    private final RailsModelsCollection parent;
    private final IFile file;
    private final String[] pathComponents;
    private final String[] className;
    private boolean typeKnown = false;
    private IType modelType;
    
    public RailsModel(RailsModelsCollection parent, IFile file) {
        this.parent = parent;
        this.file = file;
        
        pathComponents = PathUtils.determinePathComponents(parent.getModelsFolder(), file);
        className = RailsNamingConventions.camelize(pathComponents);
    }
    
    public IFile getCorrespondingFile() {
        return file;
    }
    
    public IType getCorrespondingType(Caching caching) {
        if (!typeKnown && caching != Caching.CACHED_ONLY) {
            modelType = RailsFileUtils.findModelTypeInFile(file);
            typeKnown = true;
        }
        return modelType;
    }
    
    public String[] getExpectedClassName() {
        return className;
    }
    
    public String[] getPathComponents() {
        return pathComponents;
    }
    
    public void reconcile(RailsDeltaBuilder deltaBuilder, IResourceDelta delta) {
        typeKnown = false;
        modelType = null;
    }
    
    public IRailsProject getRailsProject() {
        return parent.getRailsProject();
    }
    
    public String getName() {
        return RailsNamingConventions.joinNamespaces(className);
    }
    
}
