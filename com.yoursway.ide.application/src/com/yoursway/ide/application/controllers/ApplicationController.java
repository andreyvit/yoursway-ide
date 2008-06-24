package com.yoursway.ide.application.controllers;

import com.yoursway.databinding.Realm;
import com.yoursway.ide.application.controllers.mainwindow.MainWindowController;
import com.yoursway.ide.application.model.Project;
import com.yoursway.ide.application.model.application.ApplicationModel;
import com.yoursway.ide.application.model.application.ApplicationModelListener;
import com.yoursway.ide.application.model.application.ProjectAdditionReason;
import com.yoursway.ide.application.view.application.ApplicationPresentation;
import com.yoursway.ide.application.view.application.ApplicationPresentationCallback;
import com.yoursway.ide.application.view.application.ApplicationPresentationFactory;

public class ApplicationController implements ApplicationPresentationCallback, ApplicationModelListener {
    
    private final ApplicationModel model;
    private ApplicationPresentation presentation;
    
    public ApplicationController(ApplicationModel model, ApplicationPresentationFactory presentationFactory) {
        if (model == null)
            throw new NullPointerException("model is null");
        if (presentationFactory == null)
            throw new NullPointerException("presentationFactory is null");
        this.model = model;
        this.presentation = presentationFactory.createPresentation(this);
        
        model.addListener(this);
    }
    
    public void run() {
        Realm.runWithDefault(presentation.getDefaultBindingRealm(), new Runnable() {

            public void run() {
                model.createProject(model.getRegisteredProjectTypes().iterator().next());
                presentation.runEventLoop();
            }
            
        });
    }
    
    public void projectAdded(Project project, ProjectAdditionReason reason) {
        new MainWindowController(project, presentation);
    }
    
}
