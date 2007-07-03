package com.yoursway.ruby;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.dltk.launching.IInterpreterInstall;

import com.yoursway.rails.Version;
import com.yoursway.rails.core.internal.support.AbstractModel;
import com.yoursway.ruby.internal.IPossibleRubyLocationsRequestor;
import com.yoursway.ruby.internal.PossibleRubyLocationsIterator;
import com.yoursway.ruby.internal.RubyInstanceValidationResult;
import com.yoursway.ruby.internal.RubyInstanceValidator;
import com.yoursway.utils.TypedListenerList;

public class RubyInstanceCollection extends AbstractModel<IRubyInstancesListener> {
    
    private final Map<File, RubyInstance> instances = new HashMap<File, RubyInstance>();
    
    private boolean discoveryInProgress = false;
    
    private DiscoveryProcess driver;
    
    private RubyInstanceCollection() {
        discover(null);
    }
    
    private static final RubyInstanceCollection INSTANCE = new RubyInstanceCollection();
    
    public static RubyInstanceCollection instance() {
        return INSTANCE;
    }
    
    private class DiscoveryProcess implements IPossibleRubyLocationsRequestor {
        
        private final Set<File> knownBadLocations = new HashSet<File>();
        
        private final TypedListenerList<IRubyDiscoveryListener> listeners = new TypedListenerList<IRubyDiscoveryListener>() {
            
            @Override
            protected IRubyDiscoveryListener[] makeArray(int size) {
                return new IRubyDiscoveryListener[size];
            }
            
        };
        
        private Collection<RubyInstance> unchangedInstances;
        
        public void run(IProgressMonitor monitor) {
            SubMonitor progress = SubMonitor.convert(monitor, 100);
            
            HashSet<RubyInstance> unchangedInstances = new HashSet<RubyInstance>(getAll());
            
            verifyExistingInstances(progress.newChild(15));
            verifyExistingInstalls(progress.newChild(5));
            searchForNewInstalls(progress.newChild(80));
            
            Set<RubyInstance> finalInstances = new HashSet<RubyInstance>(getAll());
            unchangedInstances.retainAll(finalInstances);
            IRubyDiscoveryListener[] listenersArray;
            synchronized (this) {
                this.unchangedInstances = unchangedInstances;
                listenersArray = listeners.getListeners();
            }
            for (IRubyDiscoveryListener listener : listenersArray)
                listener.railsInstancesUnchanged(unchangedInstances);
        }
        
        private void searchForNewInstalls(IProgressMonitor monitor) {
            PossibleRubyLocationsIterator iterator = new PossibleRubyLocationsIterator(this);
            iterator.build(monitor);
        }
        
        private void verifyExistingInstalls(IProgressMonitor monitor) {
            final Collection<? extends RubyInstallWrapper> installs = RubyInstallWrapper.getAll();
            SubMonitor progress = SubMonitor.convert(monitor, installs.size());
            for (RubyInstallWrapper installWrapper : installs) {
                File location = installWrapper.getLocation();
                RubyInstance rubyInstance = get(location);
                if (rubyInstance == null) {
                    RubyInstanceValidationResult result = validate(location, progress.newChild(1));
                    if (result.isValid())
                        addRuby(location, result.getVersion(), installWrapper);
                    else
                        installWrapper.destroy();
                } else {
                    if (!rubyInstance.getInstallWrapper().getId().equals(installWrapper.getId())) {
                        // another install pointing to an already known installation
                        installWrapper.destroy();
                    }
                    progress.worked(1);
                }
            }
        }
        
        private void verifyExistingInstances(IProgressMonitor monitor) {
            Collection<RubyInstance> instancesToValidate = new ArrayList<RubyInstance>(getAll());
            SubMonitor progress = SubMonitor.convert(monitor, instancesToValidate.size());
            for (RubyInstance rubyInstance : instancesToValidate) {
                final File location = rubyInstance.getLocation();
                RubyInstanceValidationResult result = validate(location, progress.newChild(1));
                if (!result.isValid())
                    removeRuby(rubyInstance);
                else if (!result.getVersion().equals(rubyInstance.getVersion())) {
                    removeRuby(rubyInstance);
                    addRuby(location, result.getVersion(), null);
                }
            }
        }
        
        private RubyInstanceValidationResult validate(File location, IProgressMonitor monitor) {
            if (knownBadLocations.contains(location))
                return new RubyInstanceValidationResult(false, null);
            RubyInstanceValidationResult result = RubyInstanceValidator.validate(location, monitor);
            if (!result.isValid())
                knownBadLocations.add(location);
            return result;
        }
        
        public void accept(File executableLocation) {
            RubyInstanceValidationResult result = validate(executableLocation, null);
            if (result.isValid())
                addRuby(executableLocation, result.getVersion(), null);
        }
        
        public void addListener(IRubyDiscoveryListener discoveryListener) {
            Collection<RubyInstance> unchangedInstances;
            synchronized (this) {
                if (this.unchangedInstances == null) {
                    listeners.add(discoveryListener);
                    return;
                }
                unchangedInstances = this.unchangedInstances;
            }
            discoveryListener.railsInstancesUnchanged(unchangedInstances);
        }
    }
    
    protected void removeRuby(RubyInstance instance) {
        syncRemoveRuby(instance);
        for (IRubyInstancesListener listener : getListeners())
            listener.rubyInstanceRemoved(instance);
    }
    
    private synchronized void syncRemoveRuby(RubyInstance instance) {
        instances.remove(instance.getLocation());
        instance.getInstallWrapper().destroy();
    }
    
    protected void addRuby(File location, Version version, RubyInstallWrapper installWrapper) {
        Assert.isTrue(installWrapper == null || installWrapper.getLocation().equals(location));
        RubyInstance instance = syncAddRuby(location, version, installWrapper);
        for (IRubyInstancesListener listener : getListeners())
            listener.rubyInstanceAdded(instance);
    }
    
    private synchronized RubyInstance syncAddRuby(File location, Version version,
            RubyInstallWrapper installWrapper) {
        String id = "Ruby " + version.asDotDelimitedString();
        String id2 = id + RubyInstallWrapper.AMBIG_NAME_NUMBER_DELIMITER;
        if (installWrapper != null) {
            String iwid = installWrapper.getRawDLTKInterpreterInstall().getId();
            if (!iwid.equals(id) && !iwid.startsWith(id2)) {
                installWrapper.destroy();
                installWrapper = null;
            }
        }
        if (installWrapper == null)
            installWrapper = RubyInstallWrapper.create(location, id);
        final RubyInstance instance = new RubyInstance(location, version, installWrapper);
        instances.put(location, instance);
        return instance;
    }
    
    public synchronized void discover(final IRubyDiscoveryListener discoveryListener) {
        if (discoveryInProgress) {
            if (discoveryListener != null)
                driver.addListener(discoveryListener);
            return;
        }
        discoveryInProgress = true;
        driver = new DiscoveryProcess();
        
        Job job = new Job("Searching for Ruby") {
            
            @Override
            protected IStatus run(IProgressMonitor monitor) {
                if (discoveryListener != null)
                    driver.addListener(discoveryListener);
                driver.run(monitor);
                return Status.OK_STATUS;
            }
            
        };
        job.addJobChangeListener(new JobChangeAdapter() {
            
            @Override
            public void done(IJobChangeEvent event) {
                discoveryEnded();
            }
            
        });
        job.schedule();
    }
    
    protected synchronized void discoveryEnded() {
        discoveryInProgress = false;
    }
    
    public synchronized RubyInstance get(File location) {
        return instances.get(location);
    }
    
    public RubyInstance get(IInterpreterInstall install) {
        return get(install.getInstallLocation());
    }
    
    public synchronized Collection<RubyInstance> getAll() {
        return new ArrayList<RubyInstance>(instances.values());
    }
    
    @Override
    protected IRubyInstancesListener[] makeListenersArray(int size) {
        return new IRubyInstancesListener[size];
    }
    
}
