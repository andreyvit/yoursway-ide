package com.yoursway.ide.application.model.application;

import com.yoursway.ide.application.model.Project;

public interface ApplicationModelListener {

    void projectAdded(Project project, ProjectAdditionReason reason);
    
}
