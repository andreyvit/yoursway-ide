package com.yoursway.experiments.birdseye.component;

import com.yoursway.ide.application.model.Project;

public class BirdsEyeViewController implements BirdsEyeViewCallback {

    private BirdsEyeView view;

    public BirdsEyeViewController(Project project, BirdsEyeViewFactory viewFactory) {
        view = viewFactory.bind(this);
        view.show(project);
    }
    
}
