package com.yoursway.rails.models.core.internal.infos;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.eclipse.core.resources.IFile;

import com.yoursway.rails.models.controller.RailsController;
import com.yoursway.rails.models.project.RailsProject;

public class ControllerInfoCache {
    
    private Map<RailsProject, Map<IFile, RailsController>> snapshot = new HashMap<RailsProject, Map<IFile, RailsController>>();
    
    private final Map<RailsProject, Map<IFile, RailsController>> cache = new HashMap<RailsProject, Map<IFile, RailsController>>();
    
    public synchronized Collection<RailsController> obtainActualInfos(RailsProject railsProject) {
        Map<IFile, RailsController> cachedResult = cache.get(railsProject);
        if (cachedResult != null)
            return cachedResult.values();
        return rebuild(railsProject, IControllerDeltaConsumer.NULL);
    }
    
    public synchronized Collection<RailsController> rebuild(RailsProject railsProject,
            IControllerDeltaConsumer consumer) {
        ComparingControllerInfoRequestor infoRequestor = new ComparingControllerInfoRequestor(new Holder(
                railsProject));
        ControllerInfoBuilder builder = new ControllerInfoBuilder(railsProject, infoRequestor);
        builder.build();
        infoRequestor.removeStale();
        return cache.get(railsProject).values();
    }
    
    public synchronized void checkpoint(IControllerDeltaConsumer consumer) {
        for (RailsProject railsProject : cache.keySet()) {
            obtainActualInfos(railsProject);
            // TODO: compare snapshot with cache, report diff to IControllerDeltaConsumer
        }
        snapshot = new HashMap<RailsProject, Map<IFile, RailsController>>(cache);
    }
    
    public synchronized RailsController obtainActualInfo(RailsProject railsProject, IFile file) {
        for (RailsController railsController : obtainActualInfos(railsProject))
            if (railsController.getFile().equals(file))
                return railsController;
        return null;
    }
    
    private class Holder implements IControllerInfoHolder {
        
        private final RailsProject railsProject;
        
        public Holder(RailsProject railsProject) {
            this.railsProject = railsProject;
        }
        
        private Map<IFile, RailsController> getReadOnlyMap() {
            Map<IFile, RailsController> result = cache.get(railsProject);
            if (result == null)
                result = Collections.emptyMap();
            return result;
        }
        
        private Map<IFile, RailsController> getWritableMap() {
            Map<IFile, RailsController> result = cache.get(railsProject);
            if (result == null) {
                result = new HashMap<IFile, RailsController>();
                cache.put(railsProject, result);
            }
            return result;
        }
        
        public RailsController get(Object key) {
            return getReadOnlyMap().get(key);
        }
        
        public Collection<RailsController> values() {
            return getReadOnlyMap().values();
        }
        
        public RailsController remove(IFile file) {
            RailsController result = getWritableMap().remove(file);
            return result;
        }
        
        public void add(RailsController info) {
            getWritableMap().put(info.getFile(), info);
        }
        
        public void removeAll(HashSet<RailsController> items) {
            for (RailsController railsController : items) {
                remove(railsController.getFile());
            }
        }
    }
    
}
