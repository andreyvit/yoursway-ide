package com.yoursway.ide.application.controllers;

import com.yoursway.ide.application.model.Project;
import com.yoursway.ide.application.view.ViewFactory;

public interface ViewComponentFactory {
    
    ViewComponent createBoundComponent(ViewFactory viewFactory, Project project);
    
}
