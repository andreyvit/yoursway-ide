package com.yoursway.rails.model.internal.infos;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;

public class ControllerInfoCache {
    
    private Map<ProjectInfo, Collection<ControllerInfo>> snapshot = new HashMap<ProjectInfo, Collection<ControllerInfo>>();
    
    private final Map<ProjectInfo, Collection<ControllerInfo>> cache = new HashMap<ProjectInfo, Collection<ControllerInfo>>();
    
    public synchronized Collection<ControllerInfo> obtainActualInfos(ProjectInfo projectInfo) {
        Collection<ControllerInfo> cachedResult = cache.get(projectInfo);
        if (cachedResult != null)
            return cachedResult;
        return rebuild(projectInfo);
    }
    
    public synchronized Collection<ControllerInfo> rebuild(ProjectInfo projectInfo) {
        ControllerInfoBuilder builder = new ControllerInfoBuilder(projectInfo);
        final Collection<ControllerInfo> result = builder.build();
        cache.put(projectInfo, result);
        return result;
    }
    
    public synchronized void checkpoint(IControllerDeltaConsumer consumer) {
        for (ProjectInfo projectInfo : cache.keySet()) {
            obtainActualInfos(projectInfo);
            // TODO: compare snapshot with cache, report diff to IControllerDeltaConsumer
        }
        snapshot = new HashMap<ProjectInfo, Collection<ControllerInfo>>(cache);
    }
    
    public synchronized ControllerInfo obtainActualInfo(ProjectInfo projectInfo, IFile file) {
        for (ControllerInfo controllerInfo : obtainActualInfos(projectInfo))
            if (controllerInfo.getFile().equals(file))
                return controllerInfo;
        return null;
    }
    
}
