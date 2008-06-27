package com.yoursway.ide.application.controllers;

import java.io.File;

import com.yoursway.databinding.Realm;
import com.yoursway.ide.application.controllers.mainwindow.MainWindowController;
import com.yoursway.ide.application.model.Project;
import com.yoursway.ide.application.model.application.ApplicationModel;
import com.yoursway.ide.application.model.application.ApplicationModelListener;
import com.yoursway.ide.application.model.application.ProjectAdditionReason;
import com.yoursway.ide.application.view.application.ApplicationPresentation;
import com.yoursway.ide.application.view.application.ApplicationPresentationCallback;
import com.yoursway.ide.application.view.application.ApplicationPresentationFactory;
import com.yoursway.ide.application.view.impl.commands.Command;
import com.yoursway.ide.application.view.impl.commands.Handler;
import com.yoursway.ide.application.view.impl.commands.HandlerOfOpenProject;
import com.yoursway.ide.platforms.api.PlatformSupport;

public class ApplicationController extends AbstractController implements ApplicationPresentationCallback,
        ApplicationModelListener, Handler, HandlerOfOpenProject {
    
    private final ApplicationModel model;
    private final ApplicationPresentation presentation;
    private final ApplicationViewsDefinition viewsDefinition;
    private final PlatformSupport platformSupport;
    
    private final Context context;
    
    private final CommandDispatcher dispatcher;
    
    public ApplicationController(PlatformSupport platformSupport, ApplicationModel model,
            ApplicationPresentationFactory presentationFactory) {
        if (platformSupport == null)
            throw new NullPointerException("platformSupport is null");
        if (model == null)
            throw new NullPointerException("model is null");
        if (presentationFactory == null)
            throw new NullPointerException("presentationFactory is null");
        this.platformSupport = platformSupport;
        this.model = model;
        this.context = new Context(this);
        this.dispatcher = new CommandDispatcher(this);
        this.presentation = presentationFactory.createPresentation(this);
        this.viewsDefinition = new ApplicationViewsDefinition(presentation.viewDefinitions(), presentation
                .mainWindowAreas());
        
        model.addListener(this);
    }
    
    public void run() {
        Realm.runWithDefault(presentation.getDefaultBindingRealm(), new Runnable() {
            
            public void run() {
                doRun();
            }
            
        });
    }
    
    void doRun() {
        //        model.createProject(model.getRegisteredProjectTypes().iterator().next());
        model.openProject(new File(platformSupport.defaultProjectsLocation(), "ujudge"));
        presentation.runEventLoop();
    }
    
    public void projectAdded(Project project, ProjectAdditionReason reason) {
        new MainWindowController(project, presentation, context, viewsDefinition);
    }
    
    public void execute(Command command) {
        dispatcher.execute(command);
    }
    
    public void openProject() {
    }
    
}
