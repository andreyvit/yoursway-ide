package com.yoursway.rails.core.migrations;

public interface IRailsMigrationsListener {
    
    void migrationAdded(RailsMigration railsMigration);
    
    void migrationRemoved(RailsMigration railsMigration);
    
    void migrationContentChanged(RailsMigration railsMigration);
    
}
