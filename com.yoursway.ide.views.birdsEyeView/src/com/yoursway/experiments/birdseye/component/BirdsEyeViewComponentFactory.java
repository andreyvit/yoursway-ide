package com.yoursway.experiments.birdseye.component;

import com.yoursway.ide.application.controllers.ViewComponent;
import com.yoursway.ide.application.controllers.ViewComponentFactory;
import com.yoursway.ide.application.model.Project;
import com.yoursway.ide.application.view.ViewDefinition;
import com.yoursway.ide.application.view.ViewFactory;

public class BirdsEyeViewComponentFactory implements ViewComponentFactory {
    
    private final ViewDefinition viewDefinition;
    
    public BirdsEyeViewComponentFactory(ViewDefinition viewDefinition) {
        if (viewDefinition == null)
            throw new NullPointerException("viewDefinition is null");
        this.viewDefinition = viewDefinition;
    }
    
    public ViewComponent createBoundComponent(ViewFactory viewFactory, Project project) {
        return new BirdsEyeViewComponent(viewFactory, viewDefinition, project);
    }
    
}
