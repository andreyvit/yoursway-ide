package com.yoursway.ide.application.controllers;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.databinding.observable.Realm;

import com.yoursway.ide.application.controllers.mainwindow.MainWindowController;
import com.yoursway.ide.application.model.Project;
import com.yoursway.ide.application.model.application.ApplicationModel;
import com.yoursway.ide.application.model.application.ApplicationModelListener;
import com.yoursway.ide.application.model.application.ProjectAdditionReason;
import com.yoursway.ide.application.view.application.ApplicationPresentation;
import com.yoursway.ide.application.view.application.ApplicationPresentationCallback;
import com.yoursway.ide.application.view.application.ApplicationPresentationFactory;
import com.yoursway.ide.application.view.impl.ApplicationCommands;
import com.yoursway.ide.application.view.impl.commands.AbstractHandler;
import com.yoursway.ide.application.view.impl.commands.Command;
import com.yoursway.ide.application.view.impl.commands.Handler;
import com.yoursway.ide.platforms.api.PlatformSupport;
import com.yoursway.utils.bugs.Bugs;
import com.yoursway.utils.bugs.Severity;

public class ApplicationController extends AbstractController implements ApplicationPresentationCallback,
        ApplicationModelListener {
    
    private final ApplicationModel model;
    private final ApplicationPresentation presentation;
    private final ApplicationViewsDefinition viewsDefinition;
    private final PlatformSupport platformSupport;
    
    private final Context context;
    private final EditorRegistry editorRegistry;
    
    public ApplicationController(PlatformSupport platformSupport, ApplicationModel model,
            ApplicationPresentationFactory presentationFactory, EditorRegistry editorRegistry) {
        if (platformSupport == null)
            throw new NullPointerException("platformSupport is null");
        if (model == null)
            throw new NullPointerException("model is null");
        if (presentationFactory == null)
            throw new NullPointerException("presentationFactory is null");
        if (editorRegistry == null)
            throw new NullPointerException("editorRegistry is null");
        this.platformSupport = platformSupport;
        this.model = model;
        this.context = new Context(this);
        this.presentation = presentationFactory.createPresentation(this);
        this.editorRegistry = editorRegistry;
        this.viewsDefinition = new ApplicationViewsDefinition(presentation.viewDefinitions(), presentation
                .mainWindowAreas());
        
        model.addListener(this);
        
//        registry
        
        hook();
    }

    private void hook() {
        // XXX hack - should not create another instance here 
        ApplicationCommands commands = new ApplicationCommands();
        context.addHandler(commands.openProject, new Handler() {

            public boolean run(Command command) {
                File file = presentation.chooseProjectToOpen();
                if (file != null) {
                    try {
                        model.openProject(file);
                    } catch (Throwable e) {
                        Bugs.unknownErrorRecovery(Severity.USER_COMMAND_IGNORED, e);
                        presentation.displayFailedToOpenProjectError(file);
                    }
                }
                return true;
            }
            
        });
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
        new MainWindowController(project, presentation, context, viewsDefinition, editorRegistry);
    }
    
    public void execute(Command command) {
        context.execute(command);
    }
    
    public void openProject() {
    }
    
}
