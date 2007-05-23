package com.yoursway.rails.models.core.internal.infos;

import com.yoursway.rails.models.project.RailsProject;

public interface IProjectDeltaConsumer {
    
    void projectAdded(RailsProject railsProject);
    
    void projectRemoved(RailsProject railsProject);
    
}
