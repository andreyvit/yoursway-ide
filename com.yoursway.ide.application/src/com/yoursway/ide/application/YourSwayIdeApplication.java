package com.yoursway.ide.application;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

import com.yoursway.ide.application.controllers.ApplicationController;
import com.yoursway.ide.application.model.application.ApplicationModel;
import com.yoursway.ide.application.view.impl.ApplicationPresentationFactoryImpl;
import com.yoursway.ide.platforms.api.PlatformSupport;
import com.yoursway.ide.platforms.api.PlatformUtilities;
import com.yoursway.ide.rails.projects.RailsProjectType;

public class YourSwayIdeApplication implements IApplication {
    
    public Object start(IApplicationContext context) throws Exception {
        PlatformSupport platformSupport = PlatformUtilities.createPlatformImpl();
        
        ApplicationModel model = new ApplicationModel(platformSupport.defaultProjectsLocation());
        model.registerProjectType(new RailsProjectType());
        
        ApplicationController controller = new ApplicationController(model,
                new ApplicationPresentationFactoryImpl());
        controller.run();
        
        return IApplication.EXIT_OK;
    }
    
    public void stop() {
    }
    
}
