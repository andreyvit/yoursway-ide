package com.yoursway.rails.core.fixtures;

import org.eclipse.core.resources.IFile;

import com.yoursway.rails.core.models.RailsModel;
import com.yoursway.rails.core.models.RailsModelsCollection;
import com.yoursway.rails.core.projects.RailsProject;
import com.yoursway.utils.RailsFixtureUtils;

public class RailsFixture {
    
    private final RailsProject railsProject;
    private final IFile file;
    
    public RailsFixture(RailsProject railsProject, IFile file) {
        if (railsProject == null)
            throw new IllegalArgumentException();
        if (file == null)
            throw new IllegalArgumentException();
        
        this.railsProject = railsProject;
        this.file = file;
        
    }
    
    public RailsProject getRailsProject() {
        return railsProject;
    }
    
    public IFile getFile() {
        return file;
    }
    
    public RailsModel getModel() {
        IFile modelFile = RailsFixtureUtils.getModelFileByFixture(file);
        return RailsModelsCollection.instance().get(railsProject).get(modelFile);
    }
    
}
