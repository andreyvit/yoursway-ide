package com.yoursway.ruby.internal;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.dltk.launching.IInterpreterInstall;

import com.yoursway.common.TypedListenerList;
import com.yoursway.ruby.RubyInstance;
import com.yoursway.ruby.RubyInstanceCollectionChangedListener;
import com.yoursway.ruby.RubyInstancesProvider;
import com.yoursway.ruby.RubySearchService;

public class RubyInstanceCollection implements RubyInstancesProvider {
    
    private final Map<File, RubyInstanceImpl> instances = new HashMap<File, RubyInstanceImpl>();
    
    //    private boolean discoveryInProgress = false;
    //    private DiscoveryProcess driver;
    //    
    //    private RubyInstanceCollection() {
    //        discover(null);
    //    }
    
    private static final RubyInstanceCollection INSTANCE = new RubyInstanceCollection();
    private static final RubySearchServiceImpl SEARCH_INSTANCE = new RubySearchServiceImpl(INSTANCE);
    
    private final TypedListenerList<RubyInstanceCollectionChangedListener> listeners = new TypedListenerList<RubyInstanceCollectionChangedListener>();
    
    public static RubyInstancesProvider instance() {
        return INSTANCE;
    }
    
    public static RubySearchService searchService() {
        return SEARCH_INSTANCE;
    }
    
    //    private class DiscoveryProcess implements IPossibleRubyLocationsRequestor {
    //        
    //        private final Set<File> knownBadLocations = new HashSet<File>();
    //        
    //        private final TypedListenerList<IRubyDiscoveryListener> listeners = new TypedListenerList<IRubyDiscoveryListener>() {
    //            
    //            @Override
    //            protected IRubyDiscoveryListener[] makeArray(int size) {
    //                return new IRubyDiscoveryListener[size];
    //            }
    //            
    //        };
    //        
    //        private Collection<RubyInstanceImpl> unchangedInstances;
    //        
    //        public void run(IProgressMonitor monitor) {
    //            SubMonitor progress = SubMonitor.convert(monitor, 100);
    //            
    //            HashSet<RubyInstanceImpl> unchangedInstances = new HashSet<RubyInstanceImpl>(getAll());
    //            
    //            verifyExistingInstances(progress.newChild(15));
    //            verifyExistingInstalls(progress.newChild(5));
    //            searchForNewInstalls(progress.newChild(80));
    //            
    //            Set<RubyInstanceImpl> finalInstances = new HashSet<RubyInstanceImpl>(getAll());
    //            unchangedInstances.retainAll(finalInstances);
    //            IRubyDiscoveryListener[] listenersArray;
    //            synchronized (this) {
    //                this.unchangedInstances = unchangedInstances;
    //                listenersArray = listeners.getListeners();
    //            }
    //            for (IRubyDiscoveryListener listener : listenersArray)
    //                listener.railsInstancesUnchanged(unchangedInstances);
    //        }
    //        
    //        private void searchForNewInstalls(IProgressMonitor monitor) {
    //            PossibleRubyLocationsIterator iterator = new PossibleRubyLocationsIterator(this);
    //            iterator.build(monitor);
    //        }
    //        
    //        private void verifyExistingInstalls(IProgressMonitor monitor) {
    //            final Collection<? extends RubyInstallWrapper> installs = RubyInstallWrapper.getAll();
    //            SubMonitor progress = SubMonitor.convert(monitor, installs.size());
    //            for (RubyInstallWrapper installWrapper : installs) {
    //                File location = installWrapper.getLocation();
    //                RubyInstanceImpl rubyInstance = get(location);
    //                if (rubyInstance == null) {
    //                    RubyInstanceValidationResult result = validate(location, progress.newChild(1));
    //                    if (result.isValid())
    //                        addRuby(location, result.getVersion(), installWrapper);
    //                    else
    //                        installWrapper.destroy();
    //                } else {
    //                    if (!rubyInstance.getInstallWrapper().getId().equals(installWrapper.getId())) {
    //                        // another install pointing to an already known installation
    //                        installWrapper.destroy();
    //                    }
    //                    progress.worked(1);
    //                }
    //            }
    //        }
    //        
    //        private void verifyExistingInstances(IProgressMonitor monitor) {
    //            Collection<RubyInstanceImpl> instancesToValidate = new ArrayList<RubyInstanceImpl>(getAll());
    //            SubMonitor progress = SubMonitor.convert(monitor, instancesToValidate.size());
    //            for (RubyInstanceImpl rubyInstance : instancesToValidate) {
    //                final File location = rubyInstance.getLocation();
    //                RubyInstanceValidationResult result = validate(location, progress.newChild(1));
    //                if (!result.isValid())
    //                    removeRuby(rubyInstance);
    //                else if (!result.getVersion().equals(rubyInstance.getVersion())) {
    //                    removeRuby(rubyInstance);
    //                    addRuby(location, result.getVersion(), null);
    //                }
    //            }
    //        }
    //        
    //        private RubyInstanceValidationResult validate(File location, IProgressMonitor monitor) {
    //            if (knownBadLocations.contains(location))
    //                return new RubyInstanceValidationResult(false, null);
    //            RubyInstanceValidationResult result = RubyInstanceValidator.validate(location, monitor);
    //            if (!result.isValid())
    //                knownBadLocations.add(location);
    //            return result;
    //        }
    //        
    //        public void accept(File executableLocation) {
    //            RubyInstanceValidationResult result = validate(executableLocation, null);
    //            if (result.isValid())
    //                addRuby(executableLocation, result.getVersion(), null);
    //        }
    //        
    //        public void addListener(IRubyDiscoveryListener discoveryListener) {
    //            Collection<RubyInstanceImpl> unchangedInstances;
    //            synchronized (this) {
    //                if (this.unchangedInstances == null) {
    //                    listeners.add(discoveryListener);
    //                    return;
    //                }
    //                unchangedInstances = this.unchangedInstances;
    //            }
    //            discoveryListener.railsInstancesUnchanged(unchangedInstances);
    //        }
    //    }
    
    //    protected void removeRuby(RubyInstanceImpl instance) {
    //        syncRemoveRuby(instance);
    //        for (IRubyInstancesListener listener : getListeners())
    //            listener.rubyInstanceRemoved(instance);
    //    }
    //    
    //    private synchronized void syncRemoveRuby(RubyInstanceImpl instance) {
    //        instances.remove(instance.getLocation());
    //        instance.getInstallWrapper().destroy();
    //    }
    //    
    //    protected void addRuby(File location, Version version, RubyInstallWrapper installWrapper) {
    //        Assert.isTrue(installWrapper == null || installWrapper.getLocation().equals(location));
    //        RubyInstanceImpl instance = syncAddRuby(location, version, installWrapper);
    //        for (IRubyInstancesListener listener : getListeners())
    //            listener.rubyInstanceAdded(instance);
    //    }
    //    
    //    private synchronized RubyInstanceImpl syncAddRuby(File location, Version version,
    //            RubyInstallWrapper installWrapper) {
    //        String id = "Ruby " + version.asDotDelimitedString();
    //        String id2 = id + RubyInstallWrapper.AMBIG_NAME_NUMBER_DELIMITER;
    //        if (installWrapper != null) {
    //            String iwid = installWrapper.getRawDLTKInterpreterInstall().getId();
    //            if (!iwid.equals(id) && !iwid.startsWith(id2)) {
    //                installWrapper.destroy();
    //                installWrapper = null;
    //            }
    //        }
    //        if (installWrapper == null)
    //            installWrapper = RubyInstallWrapper.create(location, id);
    //        final RubyInstanceImpl instance = new RubyInstanceImpl(location, version, installWrapper);
    //        instances.put(location, instance);
    //        return instance;
    //    }
    
    //public synchronized void discover(final IRubyDiscoveryListener discoveryListener) {
    //        if (discoveryInProgress) {
    //            if (discoveryListener != null)
    //                driver.addListener(discoveryListener);
    //            return;
    //        }
    //        discoveryInProgress = true;
    //        driver = new DiscoveryProcess();
    //        
    //        Job job = new Job("Searching for Ruby") {
    //            
    //            @Override
    //            protected IStatus run(IProgressMonitor monitor) {
    //                if (discoveryListener != null)
    //                    driver.addListener(discoveryListener);
    //                driver.run(monitor);
    //                return Status.OK_STATUS;
    //            }
    //            
    //        };
    //        job.addJobChangeListener(new JobChangeAdapter() {
    //            
    //            @Override
    //            public void done(IJobChangeEvent event) {
    //                discoveryEnded();
    //            }
    //            
    //        });
    //        job.schedule();
    //}
    
    //    protected synchronized void discoveryEnded() {
    //        discoveryInProgress = false;
    //    }
    
    //    public RubyInstanceImpl get(IInterpreterInstall install) {
    //        return get(install.getInstallLocation());
    //    }
    
    /**
     * Returns unmodifiable collection of all registered Ruby instances.
     */
    public synchronized Collection<RubyInstanceImpl> getAll() {
        return Collections.unmodifiableList(new ArrayList<RubyInstanceImpl>(instances.values()));
    }
    
    public void addRubyInstanceCollectionChangedListener(RubyInstanceCollectionChangedListener listener) {
        listeners.add(listener);
    }
    
    public void removeRubyInstanceCollectionChangedListener(RubyInstanceCollectionChangedListener listener) {
        listeners.remove(listener);
    }
    
    public synchronized RubyInstance addRubyInstance(IInterpreterInstall rubyInterpreter) {
        File location = rubyInterpreter.getInstallLocation();
        if (!instances.containsKey(location)) {
            //FIXME
            instances.put(location, new RubyInstanceImpl(location, null, null));
        }
        RubyInstance ruby = instances.get(location);
        
        for (RubyInstanceCollectionChangedListener listener : listeners)
            listener.rubyInstanceAdded(ruby);
        
        return ruby;
    }
    
    public synchronized void removeRubyInstance(RubyInstance ruby) {
        for (RubyInstanceCollectionChangedListener listener : listeners)
            listener.rubyInstanceRemoved(ruby);
        instances.remove(ruby);
    }
}
