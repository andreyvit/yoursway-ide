package com.yoursway.rails.core.tests;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

import com.yoursway.ide.ui.Activator;
import com.yoursway.rails.core.internal.support.AbstractModel;
import com.yoursway.rails.core.projects.RailsProject;
import com.yoursway.rails.core.tests.internal.BroadcastingRailsTestsChangeVisitor;
import com.yoursway.rails.core.tests.internal.RailsTestsIterator;
import com.yoursway.rails.core.tests.internal.Requestor;
import com.yoursway.utils.RailsNamingConventions;

public class PerProjectRailsTestsCollection extends AbstractModel<IRailsTestsListener> {
    
    private Map<IFile, RailsTest> items = new HashMap<IFile, RailsTest>();
    private final RailsProject railsProject;
    
    public PerProjectRailsTestsCollection(RailsProject railsProject) {
        this.railsProject = railsProject;
        rebuild();
    }
    
    public RailsProject getRailsProject() {
        return railsProject;
    }
    
    public Collection<RailsTest> getAll() {
        return items.values();
    }
    
    public RailsTest get(IFile project) {
        return items.get(project);
    }
    
    public void rebuild() {
        Requestor updater = new Requestor(railsProject, items);
        new RailsTestsIterator(railsProject, updater).build();
        items = updater.getNewItems();
        updater.visitChanges(new BroadcastingRailsTestsChangeVisitor(getListeners()));
    }
    
    @Override
    protected IRailsTestsListener[] makeListenersArray(int size) {
        return new IRailsTestsListener[size];
    }
    
    private void reconcileFor(IResourceDelta projectRD, IPath path) {
        IResourceDelta folderDelta = projectRD.findMember(path);
        if (folderDelta == null)
            return;
        try {
            folderDelta.accept(new IResourceDeltaVisitor() {
                
                public boolean visit(IResourceDelta delta) throws CoreException {
                    if (delta.getResource().getType() == IResource.FILE) {
                        IFile file = (IFile) delta.getResource();
                        RailsTest railsTest = items.get(file);
                        if (railsTest != null)
                            for (IRailsTestsListener listener : getListeners())
                                listener.reconcile(railsTest);
                    }
                    return true;
                }
                
            });
        } catch (CoreException e) {
            Activator.unexpectedError(e);
        }
    }
    
    public void reconcile(IResourceDelta projectRD) {
        rebuild();
        reconcileFor(projectRD, RailsNamingConventions.TEST_UNIT_PATH);
        reconcileFor(projectRD, RailsNamingConventions.TEST_FUNCTIONAL_PATH);
    }
}
