package com.yoursway.ide.views.project;

import static com.yoursway.utils.UniqueId.uniqueId;

import com.yoursway.ide.application.controllers.ViewComponentFactory;
import com.yoursway.ide.application.controllers.ViewComponentFactoryFactory;
import com.yoursway.ide.application.view.ViewDefinition;
import com.yoursway.ide.application.view.ViewDefinitionFactory;
import com.yoursway.ide.application.view.mainwindow.MainWindowAreas;

public class ProjectTreeComponentFactoryFactory implements ViewComponentFactoryFactory {
    
    public ViewComponentFactory create(ViewDefinitionFactory definitionFactory) {
        ViewDefinition viewDefinition = definitionFactory.defineView(
                uniqueId("3C0A3078-8C42-4F70-AA05-FF1A031624FA Project Tree"),
                MainWindowAreas.projectViewArea);
        return new ProjectTreeComponentFactory(viewDefinition);
    }
    
}
