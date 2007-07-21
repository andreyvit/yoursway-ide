package com.yoursway.rails.core.migrations;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;

import com.yoursway.common.resources.ResourceUtils;
import com.yoursway.ide.ui.Activator;
import com.yoursway.rails.core.internal.support.AbstractModel;
import com.yoursway.rails.core.migrations.internal.RailsMigrationsBroadcastingChangeVisitor;
import com.yoursway.rails.core.migrations.internal.RailsMigrationsIterator;
import com.yoursway.rails.core.migrations.internal.RailsMigrationsRequestor;
import com.yoursway.rails.core.projects.RailsProject;
import com.yoursway.utils.RailsNamingConventions;

public class PerProjectRailsMigrationsCollection extends AbstractModel<IRailsMigrationsListener> {
    
    private final RailsProject railsProject;
    private Map<IFile, RailsMigration> items = new HashMap<IFile, RailsMigration>();
    
    public PerProjectRailsMigrationsCollection(RailsProject railsProject) {
        Assert.isNotNull(railsProject);
        this.railsProject = railsProject;
        rebuild();
    }
    
    public RailsProject getRailsProject() {
        return railsProject;
    }
    
    public Collection<RailsMigration> getAll() {
        return items.values();
    }
    
    public RailsMigration get(IFile file) {
        return items.get(file);
    }
    
    private void rebuild() {
        RailsMigrationsRequestor requestor = new RailsMigrationsRequestor(railsProject, items);
        new RailsMigrationsIterator(railsProject, requestor).build();
        items = requestor.getNewItems();
        requestor.visitChanges(new RailsMigrationsBroadcastingChangeVisitor(getListeners()));
    }
    
    @Override
    protected IRailsMigrationsListener[] makeListenersArray(int size) {
        return new IRailsMigrationsListener[size];
    }
    
    public void reconcile(IResourceDelta projectDelta) {
        IResourceDelta migDelta = projectDelta.findMember(RailsNamingConventions.DB_MIGRATIONS_PATH);
        if (migDelta == null)
            return;
        rebuild();
        try {
            migDelta.accept(new IResourceDeltaVisitor() {
                
                public boolean visit(IResourceDelta delta) throws CoreException {
                    IResource resource = delta.getResource();
                    if (resource.getType() != IResource.FILE)
                        return true;
                    RailsMigration railsMigration = items.get(resource);
                    if (railsMigration != null)
                        for (IRailsMigrationsListener listener : getListeners())
                            listener.migrationContentChanged(railsMigration);
                    return false;
                }
                
            });
        } catch (CoreException e) {
            if (!ResourceUtils.isNotFoundOrOutOfSync((e)))
                Activator.unexpectedError(e);
        }
    }
    
    public int getNextUnusedOrdinal() {
        int maxOrdinal = 0;
        for (RailsMigration migration : getAll())
            if (migration.getOrdinal() > maxOrdinal)
                maxOrdinal = migration.getOrdinal();
        return maxOrdinal + 1;
    }
    
}
