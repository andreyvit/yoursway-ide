package com.yoursway.rails.core.fixtures;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;

import com.yoursway.ide.ui.Activator;
import com.yoursway.rails.commons.RailsNamingConventions;
import com.yoursway.rails.core.fixtures.internal.BroadcastingRailsFixturesChangeVisitor;
import com.yoursway.rails.core.fixtures.internal.RailsFixturesIterator;
import com.yoursway.rails.core.fixtures.internal.Requestor;
import com.yoursway.rails.core.internal.support.AbstractModel;
import com.yoursway.rails.core.projects.RailsProject;

public class PerProjectRailsFixturesCollection extends AbstractModel<IRailsFixturesListener> {
    
    private Map<IFile, RailsFixture> items = new HashMap<IFile, RailsFixture>();
    private final RailsProject railsProject;
    
    public PerProjectRailsFixturesCollection(RailsProject railsProject) {
        this.railsProject = railsProject;
        rebuild();
    }
    
    public RailsProject getRailsProject() {
        return railsProject;
    }
    
    public Collection<RailsFixture> getAll() {
        return items.values();
    }
    
    public RailsFixture get(IFile project) {
        return items.get(project);
    }
    
    public void rebuild() {
        Requestor updater = new Requestor(railsProject, items);
        new RailsFixturesIterator(railsProject, updater).build();
        items = updater.getNewItems();
        updater.visitChanges(new BroadcastingRailsFixturesChangeVisitor(getListeners()));
    }
    
    @Override
    protected IRailsFixturesListener[] makeListenersArray(int size) {
        return new IRailsFixturesListener[size];
    }
    
    public void reconcile(IResourceDelta projectRD) {
        rebuild();
        IResourceDelta folderDelta = projectRD.findMember(RailsNamingConventions.TEST_FIXTURES_PATH);
        if (folderDelta == null)
            return;
        try {
            folderDelta.accept(new IResourceDeltaVisitor() {
                
                public boolean visit(IResourceDelta delta) throws CoreException {
                    if (delta.getResource().getType() == IResource.FILE) {
                        IFile file = (IFile) delta.getResource();
                        RailsFixture railsFixture = items.get(file);
                        if (railsFixture != null)
                            for (IRailsFixturesListener listener : getListeners())
                                listener.reconcile(railsFixture);
                    }
                    return true;
                }
                
            });
        } catch (CoreException e) {
            Activator.unexpectedError(e);
        }
    }
}
