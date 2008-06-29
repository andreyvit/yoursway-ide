package com.yoursway.ide.application.controllers;

import static com.google.common.collect.Lists.newArrayList;

import java.util.Collection;

import com.yoursway.ide.application.view.ViewDefinitionFactory;

public class ViewRegistry {
    
    private Collection<ViewComponentFactoryFactory> factories = newArrayList();
    
    public void add(ViewComponentFactoryFactory factory) {
        if (factory == null)
            throw new NullPointerException("factory is null");
        factories.add(factory);
    }
    
    public ViewRegistry2 instantiate(ViewDefinitionFactory definitionFactory) {
        Collection<ViewComponentFactory> result = newArrayList();
        for (ViewComponentFactoryFactory factory : factories)
            result.add(factory.create(definitionFactory));
        return new ViewRegistry2(result);
    }
    
}
