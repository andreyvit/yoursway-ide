package com.yoursway.rails.model.internal.infos;

public interface IProjectDeltaConsumer {
    
    void projectAdded(ProjectInfo projectInfo);
    
    void projectRemoved(ProjectInfo projectInfo);
    
}
