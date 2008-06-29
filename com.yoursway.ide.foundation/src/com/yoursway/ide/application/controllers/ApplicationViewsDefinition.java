package com.yoursway.ide.application.controllers;

import static com.yoursway.utils.UniqueId.uniqueId;

import com.yoursway.ide.application.view.ViewDefinition;
import com.yoursway.ide.application.view.ViewDefinitionFactory;
import com.yoursway.ide.application.view.mainwindow.MainWindowAreas;

public class ApplicationViewsDefinition {
    
    public final ViewDefinition projectTreeViewDefinition;
    
    public ApplicationViewsDefinition(ViewDefinitionFactory factory, MainWindowAreas mainWindowAreas) {
        projectTreeViewDefinition = factory.defineView(
                uniqueId("3C0A3078-8C42-4F70-AA05-FF1A031624FA Project Tree"),
                mainWindowAreas.projectViewArea);
    }
    
}
