package com.yoursway.rails.core.migrations.internal;

public interface IRailsMigrationsRequestor {
    
    void accept(RailsMigrationInfo info);
    
}
