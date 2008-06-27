package com.yoursway.ide.application.model.application;

import static com.google.common.collect.Lists.newArrayList;
import static com.yoursway.utils.Listeners.newListenersByIdentity;

import java.io.File;
import java.util.Collection;
import java.util.Collections;

import com.yoursway.ide.application.model.Project;
import com.yoursway.ide.application.model.ProjectOwner;
import com.yoursway.ide.application.model.projects.types.ProjectType;
import com.yoursway.ide.application.problems.Bugs;
import com.yoursway.utils.Listeners;

public class ApplicationModel implements ProjectOwner {
    
    private final File defaultProjectFolder;
    
    private final Collection<ProjectType> projectTypes = newArrayList();
    
    private final Collection<Project> projects = newArrayList();
    
    private transient Listeners<ApplicationModelListener> listeners = newListenersByIdentity();

    public ApplicationModel(File defaultProjectFolder) {
        if (defaultProjectFolder == null)
            throw new NullPointerException("defaultProjectFolder is null");
        this.defaultProjectFolder = defaultProjectFolder;
    }
    
    public synchronized void addListener(ApplicationModelListener listener) {
        listeners.add(listener);
    }
    
    public synchronized void removeListener(ApplicationModelListener listener) {
        listeners.remove(listener);
    }
    
    public void registerProjectType(ProjectType projectType) {
        if (projectType == null)
            throw new NullPointerException("projectType is null");
        projectTypes.add(projectType);
    }
    
    public Collection<ProjectType> getRegisteredProjectTypes() {
        return Collections.unmodifiableCollection(projectTypes);
    }
    
    public void openProject(File location) {
        ProjectType type = recognizeType(location);
        if (type == null)
            type = projectTypes.iterator().next(); // TODO: use a “generic” type
        addProject(new Project(this, type, location), ProjectAdditionReason.OPENED);
    }
    
    private ProjectType recognizeType(File location) {
        for (ProjectType type : projectTypes)
            if (type.recognize(location))
                return type;
        return null;
    }
    
    public void createProject(ProjectType type) {
        String projectName = type.generateNewProjectName(defaultProjectFolder);
        File location = new File(defaultProjectFolder, projectName);
        addProject(new Project(this, type, location), ProjectAdditionReason.CREATED);
    }

    private void addProject(Project project, ProjectAdditionReason reason) {
        projects.add(project);
        for(ApplicationModelListener listener : listeners)
            try {
                listener.projectAdded(project, reason);
            } catch (Throwable e) {
                Bugs.listenerFailed(e, listener, "Adding project " + project + " (reason " + reason + ")");
            }
    }

    public void closeProject(Project project) {
        projects.remove(project);
    }
    
}
