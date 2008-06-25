package com.yoursway.ide.application.controllers.mainwindow;

import com.yoursway.ide.application.model.Project;
import com.yoursway.ide.application.view.View;
import com.yoursway.ide.application.view.ViewCallback;
import com.yoursway.ide.application.view.ViewDefinition;
import com.yoursway.ide.application.view.ViewFactory;
import com.yoursway.ide.application.view.ViewPresentation;

public class ProjectTreeController implements ViewCallback {
    
    private final Project project;
    private final View view;

    public ProjectTreeController(ViewFactory viewFactory, ViewDefinition viewDefinition, Project project) {
        if (project == null)
            throw new NullPointerException("project is null");
        this.project = project;
        this.view = viewFactory.bindView(viewDefinition, this);
    }

    public void bindPresentation(final ViewPresentation presentation) {
        new ProjectTreeInstanceController(project, new ProjectTreeViewFactory() {

            public ProjectTreeView bind(ProjectTreeViewCallback callback) {
                return new ProjectTreeViewImpl(presentation, callback);
            }
            
        });
    }
    
}
