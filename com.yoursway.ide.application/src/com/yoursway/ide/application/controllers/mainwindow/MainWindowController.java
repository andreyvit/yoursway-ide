package com.yoursway.ide.application.controllers.mainwindow;

import static com.yoursway.utils.instrusive.IntrusiveMaps.newIntrusiveHashMap;

import com.yoursway.ide.application.controllers.AbstractController;
import com.yoursway.ide.application.controllers.ApplicationViewsDefinition;
import com.yoursway.ide.application.controllers.Context;
import com.yoursway.ide.application.model.Document;
import com.yoursway.ide.application.model.DocumentAdditionReason;
import com.yoursway.ide.application.model.Project;
import com.yoursway.ide.application.model.ProjectListener;
import com.yoursway.ide.application.view.mainwindow.MainWindow;
import com.yoursway.ide.application.view.mainwindow.MainWindowCallback;
import com.yoursway.ide.application.view.mainwindow.MainWindowFactory;
import com.yoursway.utils.instrusive.IntrusiveMap;

public class MainWindowController extends AbstractController implements MainWindowCallback, ProjectListener {
    
    private MainWindow window;
    private MainWindowModelImpl windowModel;
    
    private IntrusiveMap<Document, DocumentController> docsToControllers = newIntrusiveHashMap(DocumentController.GET_DOCUMENT);
    private final Context context;
    
    public MainWindowController(Project project, MainWindowFactory presentation, Context context,
            ApplicationViewsDefinition viewsDefinition) {
        if (project == null)
            throw new NullPointerException("project is null");
        if (presentation == null)
            throw new NullPointerException("presentation is null");
        if (context == null)
            throw new NullPointerException("context is null");
        if (viewsDefinition == null)
            throw new NullPointerException("viewsDefinition is null");
        this.context = new Context(this, context);
        this.windowModel = new MainWindowModelImpl();
        windowModel.projectLocation.setValue(project.getLocation());
        windowModel.projectType.setValue(project.getType());
        
        this.window = presentation.createWindow(windowModel, this);
        
        project.addListener(this);
        
        new ProjectTreeController(window, viewsDefinition.projectTreeViewDefinition, project);
        
        window.open();
    }
    
    public void documentAdded(Document document, DocumentAdditionReason reason) {
        DocumentController controller = new DocumentController(document, window);
        docsToControllers.add(controller);
    }
    
    public void documentAlreadyOpen(Document document) {
        DocumentController controller = docsToControllers.get(document);
        controller.activateEditor();
    }
    
}
