package com.yoursway.experiments.birdseye.component;

import static com.yoursway.utils.UniqueId.uniqueId;

import com.yoursway.ide.application.controllers.ViewComponentFactory;
import com.yoursway.ide.application.controllers.ViewComponentFactoryFactory;
import com.yoursway.ide.application.view.ViewDefinition;
import com.yoursway.ide.application.view.ViewDefinitionFactory;
import com.yoursway.ide.application.view.mainwindow.MainWindowAreas;

public class BirdsEyeViewComponentFactoryFactory implements ViewComponentFactoryFactory {
    
    public ViewComponentFactory create(ViewDefinitionFactory definitionFactory) {
        ViewDefinition viewDefinition = definitionFactory.defineView(
                uniqueId("27D0263A-EFCA-4E8B-A4AC-130FCB57D269 Bird's Eye View"),
                MainWindowAreas.birdsEyeViewArea);
        return new BirdsEyeViewComponentFactory(viewDefinition);
    }
    
}
