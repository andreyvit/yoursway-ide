package com.yoursway.rails.model.internal.infos;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IProject;

public class ProjectInfoCache {
    
    private Collection<ProjectInfo> cache, snapshot;
    
    public synchronized Collection<ProjectInfo> obtainActualInfos() {
        if (cache == null)
            cache = rebuild();
        return cache;
    }
    
    public synchronized Collection<ProjectInfo> rebuild() {
        return new ProjectInfoBuilder().build();
    }
    
    public synchronized ProjectInfo obtainActualInfo(IProject project) {
        for (ProjectInfo projectInfo : obtainActualInfos())
            if (projectInfo.getProject().equals(project))
                return projectInfo;
        return null;
    }
    
    public synchronized void checkpoint(IProjectDeltaConsumer consumer) {
        // TODO: compare cache with snapshot and report changes
        snapshot = new ArrayList<ProjectInfo>(cache);
    }
}
