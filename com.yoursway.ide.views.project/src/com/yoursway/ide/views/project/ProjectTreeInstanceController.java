package com.yoursway.ide.views.project;

import java.io.File;

import com.yoursway.ide.application.model.Project;

public class ProjectTreeInstanceController implements ProjectTreeViewCallback {

    private ProjectTreeView view;
    private final Project project;

    public ProjectTreeInstanceController(Project project, ProjectTreeViewFactory viewFactory) {
        if (project == null)
            throw new NullPointerException("project is null");
        this.project = project;
        this.view = viewFactory.bind(this);
        view.show(project);
    }

    public void openFile(File file) {
        project.openDocument(file);
    }
    
}
