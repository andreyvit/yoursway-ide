package com.yoursway.rails.core.migrations;

public interface IRailsMigrationsListener {
    
    void migrationAdded(RailsMigration railsMigration);
    
    void migrationRemove(RailsMigration railsMigration);
    
    void migrationContentChanged(RailsMigration railsMigration);
    
}
