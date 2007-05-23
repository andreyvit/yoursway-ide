package com.yoursway.rails.models.core.internal.infos;

import com.yoursway.rails.models.controller.RailsController;
import com.yoursway.rails.models.project.RailsProject;

public interface IControllerDeltaConsumer {
    
    void controllerAdded(RailsProject railsProject, RailsController railsController);
    
    void controllerRemoved(RailsController railsController);
    
    public static final IControllerDeltaConsumer NULL = new IControllerDeltaConsumer() {
        
        public void controllerAdded(RailsProject railsProject, RailsController railsController) {
        }
        
        public void controllerRemoved(RailsController railsController) {
        }
        
    };
    
}
