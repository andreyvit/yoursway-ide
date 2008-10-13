package com.yoursway.ide.application.controllers.mainwindow;

import static com.yoursway.utils.instrusive.IntrusiveMaps.newIntrusiveHashMap;

import java.awt.FileDialog;

import org.eclipse.jface.dialogs.MessageDialog;

import com.yoursway.ide.application.controllers.AbstractController;
import com.yoursway.ide.application.controllers.Context;
import com.yoursway.ide.application.controllers.EditorRegistry;
import com.yoursway.ide.application.controllers.ViewRegistry2;
import com.yoursway.ide.application.model.Document;
import com.yoursway.ide.application.model.DocumentAdditionReason;
import com.yoursway.ide.application.model.Project;
import com.yoursway.ide.application.model.ProjectListener;
import com.yoursway.ide.application.view.impl.ApplicationCommands;
import com.yoursway.ide.application.view.impl.commands.Command;
import com.yoursway.ide.application.view.impl.commands.Handler;
import com.yoursway.ide.application.view.mainwindow.MainWindow;
import com.yoursway.ide.application.view.mainwindow.MainWindowCallback;
import com.yoursway.ide.application.view.mainwindow.MainWindowFactory;
import com.yoursway.utils.instrusive.IntrusiveMap;

public class MainWindowController extends AbstractController implements MainWindowCallback, ProjectListener {
    
    private MainWindow window;
    private MainWindowModelImpl windowModel;
    
    private IntrusiveMap<Document, DocumentController> docsToControllers = newIntrusiveHashMap(DocumentController.GET_DOCUMENT);
    private final Context context;
    private final Project project;
    private final EditorRegistry editorRegistry;
    
    public MainWindowController(Project project, MainWindowFactory presentation, Context context,
            ViewRegistry2 viewRegistry, EditorRegistry editorRegistry) {
        if (project == null)
            throw new NullPointerException("project is null");
        if (presentation == null)
            throw new NullPointerException("presentation is null");
        if (context == null)
            throw new NullPointerException("context is null");
        if (viewRegistry == null)
            throw new NullPointerException("viewRegistry is null");
        if (editorRegistry == null)
            throw new NullPointerException("editorRegistry is null");
        this.project = project;
        this.context = new Context(this, context);
        this.windowModel = new MainWindowModelImpl();
        this.editorRegistry = editorRegistry;
        windowModel.projectLocation.setValue(project.getLocation());
        windowModel.projectType.setValue(project.getType());
        
        this.window = presentation.createWindow(windowModel, this);
        
        project.addListener(this);
        
        viewRegistry.implement(project, window);
        
        hook();
        
        window.open();
    }
    
    private void hook() {
        context.addHandler(new ApplicationCommands.NewDocumentCommand(), new Handler() {

            public boolean run(Command command) {
//            	MessageDialog.openError(null, "", "Creating document not implemented yet");
            	project.createDocument();
                return true;
            }
            
        });

        context.addHandler(new ApplicationCommands.CloseProjectCommand(), new Handler() {
        	
        	public boolean run(Command command) {
        		project.close();
        		return true;
        	}
        	
        });
    }

    public void documentAdded(Document document, DocumentAdditionReason reason) {
        DocumentController controller = new DocumentController(document, window, editorRegistry);
        docsToControllers.add(controller);
    }
    
    public void documentAlreadyOpen(Document document) {
        DocumentController controller = docsToControllers.get(document);
        controller.activateEditor();
    }

    public void activated() {
        context.setActive(true);
    }

    public void deactivated() {
        context.setActive(false);
    }

    public void closed() {
        window.forceClose();
    }

    public void windowDisposed() {
        dispose();
    }

    public void execute(Command command) {
        context.execute(command);
    }
    
}
