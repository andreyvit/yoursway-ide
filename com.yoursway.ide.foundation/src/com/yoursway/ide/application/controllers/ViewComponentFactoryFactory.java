package com.yoursway.ide.application.controllers;

import com.yoursway.ide.application.view.ViewDefinitionFactory;

public interface ViewComponentFactoryFactory {
    
    ViewComponentFactory create(ViewDefinitionFactory definitionFactory);
    
}
