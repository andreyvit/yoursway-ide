package com.yoursway.rails.search;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterInstallType;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.dltk.ruby.core.RubyNature;

import com.yoursway.utils.PercentageBroadcastingProgressMonitor;
import com.yoursway.utils.TypedListenerList;

public class RailsSearching implements ISessionTrackerParent {
    
    private final class Impl extends JobChangeAdapter {
        
        @Override
        public void aboutToRun(IJobChangeEvent event) {
            Job job = event.getJob();
            if (job.belongsTo(NEEDS_RAILS_FAMILY)) {
                if (isActive()) {
                    job.sleep();
                    notifyJobBlocked(job);
                }
            }
        }
        
    }
    
    public static final Object NEEDS_RAILS_FAMILY = new Object();
    
    private static final RailsSearching INSTANCE = new RailsSearching();
    
    private final TypedListenerList<IRailsSearchingListener> listeners = new TypedListenerList<IRailsSearchingListener>() {
        
        @Override
        protected IRailsSearchingListener[] makeArray(int size) {
            return new IRailsSearchingListener[size];
        }
        
    };
    
    public RailsSearching() {
        Job.getJobManager().addJobChangeListener(new Impl());
    }
    
    public void addListener(IRailsSearchingListener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(IRailsSearchingListener listener) {
        listeners.remove(listener);
    }
    
    public static RailsSearching getInstance() {
        return INSTANCE;
    }
    
    public static void initialize() {
        // triggers loading of this class, but otherwise does nothing
    }
    
    /**
     * Starts searching Rails instances in all available Ruby instances.
     */
    public static void runSearchRails() {
        IInterpreterInstallType[] rubyInterpreters = ScriptRuntime
                .getInterpreterInstallTypes(RubyNature.NATURE_ID);
        assert rubyInterpreters.length == 1 : "Only one IInterpreterInstallType is expected for Ruby nature";
        
        for (IInterpreterInstall rubyInterpreter : rubyInterpreters[0].getInterpreterInstalls())
            runSearchRails(rubyInterpreter);
    }
    
    /**
     * Starts searching Rails instances in the background.
     * 
     * @param rubyInterpreter
     *            Ruby interpreter where Rails instances will be searched in.
     */
    public static void runSearchRails(IInterpreterInstall rubyInterpreter) {
        RailsSearchJob job = new RailsSearchJob(rubyInterpreter);
        job.schedule();
    }
    
    private final SessionTracker sessionTracker = new SessionTracker(this);
    
    public IProgressMonitor createProgressMonitor(BlockingJobFamily family) {
        FamilyTracker familyTracker = getSessionTracker().getFamilyTracker(family);
        return new PercentageBroadcastingProgressMonitor()
                .withListener(new JobTracker(familyTracker, family));
    }
    
    private SessionTracker getSessionTracker() {
        return sessionTracker;
    }
    
    public synchronized void searchingStateChanged() {
        boolean wokeUp = false;
        if (!isActive()) {
            Job.getJobManager().wakeUp(NEEDS_RAILS_FAMILY);
            wokeUp = true;
        }
        for (IRailsSearchingListener listener : listeners.getListeners())
            listener.searchingStateChanged();
        if (wokeUp)
            for (IRailsSearchingListener listener : listeners.getListeners())
                listener.allJobsUnblocked();
    }
    
    public boolean isActive() {
        return sessionTracker != null && sessionTracker.isActive();
    }
    
    public boolean isStarted() {
        return sessionTracker != null && sessionTracker.isStarted();
    }
    
    public double getPercentage() {
        if (sessionTracker == null)
            throw new IllegalStateException();
        return sessionTracker.getPercentage();
    }
    
    private synchronized void notifyJobBlocked(Job job) {
        for (IRailsSearchingListener listener : listeners.getListeners())
            listener.blockedJobAdded(job);
    }
    
}
