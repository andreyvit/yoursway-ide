package com.yoursway.rails.model.internal.infos;

public interface IControllerDeltaConsumer {
    
    void controllerAdded(ProjectInfo projectInfo, ControllerInfo controllerInfo);
    
    void controllerRemoved(ControllerInfo controllerInfo);
    
}
