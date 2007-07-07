package com.yoursway.rails.core.migrations.internal;

import java.util.Map;

import org.eclipse.core.resources.IFile;

import com.yoursway.rails.core.internal.support.ComparingUpdater;
import com.yoursway.rails.core.migrations.RailsMigration;
import com.yoursway.rails.core.projects.RailsProject;

public class RailsMigrationsRequestor extends ComparingUpdater<RailsMigrationInfo, IFile, RailsMigration>
        implements IRailsMigrationsRequestor {
    
    private final RailsProject railsProject;
    
    public RailsMigrationsRequestor(RailsProject railsProject, Map<IFile, RailsMigration> oldItems) {
        super(oldItems);
        this.railsProject = railsProject;
    }
    
    @Override
    protected RailsMigration create(RailsMigrationInfo data) {
        return new RailsMigration(railsProject, data);
    }
    
    @Override
    protected IFile getKey(RailsMigrationInfo data) {
        return data.getFile();
    }
    
    @Override
    protected void update(RailsMigration value, RailsMigrationInfo data) {
        // all additional data is derived from identity and thus cannot change
    }
    
}
