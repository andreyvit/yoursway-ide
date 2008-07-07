package com.yoursway.experiments.birdseye.component;

import com.yoursway.ide.application.controllers.ViewComponent;
import com.yoursway.ide.application.model.Project;
import com.yoursway.ide.application.view.ViewCallback;
import com.yoursway.ide.application.view.ViewDefinition;
import com.yoursway.ide.application.view.ViewFactory;
import com.yoursway.ide.application.view.ViewPresentation;

public class BirdsEyeViewComponent implements ViewCallback, ViewComponent {

    private final Project project;

    public BirdsEyeViewComponent(ViewFactory viewFactory, ViewDefinition viewDefinition, Project project) {
        if (project == null)
            throw new NullPointerException("project is null");
        this.project = project;
        viewFactory.bindView(viewDefinition, this);
    }

    public void bindPresentation(final ViewPresentation presentation) {
        new BirdsEyeViewController(project, new BirdsEyeViewFactory() {

            public BirdsEyeView bind(BirdsEyeViewCallback callback) {
                return new BirdsEyeViewImpl(presentation, callback);
            }
            
        });
    }
    
}
