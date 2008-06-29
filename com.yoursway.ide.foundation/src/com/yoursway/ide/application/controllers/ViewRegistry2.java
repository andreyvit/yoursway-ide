package com.yoursway.ide.application.controllers;

import static com.google.common.collect.Lists.newArrayList;

import java.util.Collection;

import com.yoursway.ide.application.model.Project;
import com.yoursway.ide.application.view.ViewFactory;

public class ViewRegistry2 {
    
    private Collection<ViewComponentFactory> factories;
    
    public ViewRegistry2(Collection<ViewComponentFactory> factories) {
        if (factories == null)
            throw new NullPointerException("factories is null");
        this.factories = newArrayList(factories);
    }

    public void implement(Project project, ViewFactory viewFactory) {
        for(ViewComponentFactory factory : factories)
            factory.createBoundComponent(viewFactory, project);
    }
    
}
