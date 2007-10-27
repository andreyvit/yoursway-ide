package com.yoursway.ide.projects.editor;

import org.eclipse.core.databinding.observable.value.AbstractObservableValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.yoursway.ide.projects.ProjectRenameFailed;
import com.yoursway.ide.projects.YourSwayProjects;
import com.yoursway.ide.ui.Activator;
import com.yoursway.rails.core.projects.RailsProject;

public class ProjectEditor extends FormEditor {
    
    public static final String ID = "com.yoursway.ide.RailsProjectEditor";
    private RailsProject railsProject;
    
    @Override
    protected void addPages() {
        IObservableValue projectNameObservable = new AbstractObservableValue() {
            
            @Override
            protected Object doGetValue() {
                return railsProject.getProject().getName();
            }
            
            @Override
            protected void doSetValue(Object value) {
                String newName = (String) value;
                if (newName == railsProject.getProject().getName())
                    return;
                IWorkspaceRoot root = railsProject.getProject().getWorkspace().getRoot();
                IProject newProject = root.getProject(newName);
                if (newProject.exists())
                    throw new IllegalArgumentException("Project already exists");
                IProject oldProject = railsProject.getProject();
                try {
                    YourSwayProjects.rename(oldProject, newProject, null);
                } catch (ProjectRenameFailed e) {
                    throw new RuntimeException(e);
                }
                //                MessageDialog.openInformation(null, "Change",  "Renamed : " + value);
            }
            
            public Object getValueType() {
                return String.class;
            }
            
        };
        IObservableValue projectLocationObservable = new AbstractObservableValue() {
            
            @Override
            protected Object doGetValue() {
                return railsProject.getProject().getLocation().removeLastSegments(1).toString();
            }
            
            @Override
            protected void doSetValue(Object value) {
                //                super.doSetValue(value);
            }
            
            public Object getValueType() {
                return String.class;
            }
            
        };
        ViewModel viewModel = new ViewModel(projectNameObservable, projectLocationObservable);
        try {
            addPage(new FirstPage(this, viewModel));
        } catch (PartInitException e) {
            Activator.unexpectedError(e);
        }
    }
    
    @Override
    public void doSave(IProgressMonitor monitor) {
    }
    
    @Override
    public void doSaveAs() {
    }
    
    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }
    
    @Override
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        super.init(site, input);
        railsProject = ((ProjectEditorInput) getEditorInput()).getRailsProject();
    }
    
    @Override
    protected FormToolkit createToolkit(Display display) {
        return super.createToolkit(display);
    }
    
}
