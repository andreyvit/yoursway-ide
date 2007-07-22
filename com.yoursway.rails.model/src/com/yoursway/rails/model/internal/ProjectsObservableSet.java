package com.yoursway.rails.model.internal;

import java.util.AbstractSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.set.ISetChangeListener;
import org.eclipse.core.databinding.observable.set.ObservableSet;
import org.eclipse.core.databinding.observable.set.SetChangeEvent;
import org.eclipse.core.databinding.observable.set.SetDiff;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;

import com.yoursway.databinding.resources.IContainerObservableSet;
import com.yoursway.databinding.resources.ResourceObservables;
import com.yoursway.rails.model.RailsProject;

public class ProjectsObservableSet extends ObservableSet {
    
    private IContainerObservableSet projectSet;
    
    private Map<IProject, RailsProject> projects;
    
    private final class Contents extends AbstractSet<RailsProject> {
        
        @Override
        public int size() {
            initialize();
            return projects.size();
        }
        
        @Override
        public Iterator<RailsProject> iterator() {
            initialize();
            return new HashSet<RailsProject>(projects.values()).iterator();
        }
        
    }
    
    private ISetChangeListener setChangeListener = new ISetChangeListener() {

        public void handleSetChange(SetChangeEvent event) {
            doHandleSetChange(event);
        }
        
    };
    
    public ProjectsObservableSet(Realm realm) {
        super(realm, null, RailsProject.class);
        setWrappedSet(new Contents());
        projectSet = ResourceObservables.observeChildren(realm, ResourcesPlugin.getWorkspace().getRoot());
        realm.asyncExec(new Runnable() {

            public void run() {
                initialize();
            }
            
        });
    }
    
    public RailsProject lookup(IProject project) {
        getterCalled();
        initialize();
        return projects.get(project);
    }
    
    @Override
    protected void firstListenerAdded() {
        projectSet.addSetChangeListener(setChangeListener);
    }
    
    @Override
    protected void lastListenerRemoved() {
        projectSet.removeSetChangeListener(setChangeListener);
    }
    
    private void initialize() {
        if (projects == null) {
            projects = new HashMap<IProject, RailsProject>();
            for (Object project : projectSet) {
                addRailsProject((IProject) project);
            }
        }
    }
    
    /**
     * <code>initialize()</code> scheduled in the contructor is known to
     * have been executed, because this method will be called from inside
     * another <code>asyncExec</code>.
     */
    @SuppressWarnings("unchecked")
    void doHandleSetChange(SetChangeEvent event) {
        Assert.isNotNull(projects);
        Set<IProject> addedProjects = event.diff.getAdditions();
        Set<IProject> removedProjects = event.diff.getRemovals();
        final Set<RailsProject> added = new HashSet<RailsProject>(addedProjects.size());
        final Set<RailsProject> removed = new HashSet<RailsProject>(removedProjects.size());
        for (IProject project : addedProjects) {
            RailsProject newRailsProject = addRailsProject(project);
            if (newRailsProject != null)
                added.add(newRailsProject);
        }
        for (IProject project : removedProjects) {
            RailsProject railsProject = projects.remove(project);
            if (railsProject != null)
                removed.add(railsProject);
        }
        if (added.isEmpty() && removed.isEmpty())
            return;
        fireSetChange(new SetDiff() {
            
            @Override
            public Set getAdditions() {
                return added;
            }
            
            @Override
            public Set getRemovals() {
                return removed;
            }
            
        });
    }
    
    private RailsProject addRailsProject(IProject project) {
        if (!isRailsProject(project))
            return null;
        RailsProject newProject = new RailsProject(project);
        projects.put(project, newProject);
        return newProject;
    }
    
    private boolean isRailsProject(IProject project) {
        return true;
    }
    
}
