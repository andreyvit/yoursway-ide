package com.yoursway.rails.search.ui;

import org.eclipse.core.runtime.jobs.Job;

import com.yoursway.rails.search.IRailsSearchingListener;
import com.yoursway.rails.search.RailsSearching;
import com.yoursway.rails.search.ui.RailsSearchingView.Callback;

public class RailsSearchingUIController implements Callback, IRailsSearchingListener {
    
    private final InvisibleState INVISIBLE_STATE = new InvisibleState();
    
    private final ProgressState PROGRESS_STATE = new ProgressState();
    
    private final ResultsState RESULTS_STATE = new ResultsState();
    
    private abstract class State {
        
        public void allJobsUnblocked() {
        }
        
        public void blockedJobAdded(Job job) {
        }
        
        public void searchingStateChanged() {
            if (getRailsSearching().isActive())
                ;
        }
        
        protected final RailsSearching getRailsSearching() {
            return RailsSearching.getInstance();
        }
        
    }
    
    /**
     * No searching UI is visible.
     */
    private class InvisibleState extends State {
        
        @Override
        public void allJobsUnblocked() {
            // huh, how come there were any?
        }
        
        @Override
        public void blockedJobAdded(Job job) {
            view.showSearchProgress();
            setState(PROGRESS_STATE);
        }
        
        @Override
        public void searchingStateChanged() {
            // don't care
        }
        
    }
    
    /**
     * Searching progress UI is visible.
     */
    private class ProgressState extends State {
        
        @Override
        public void allJobsUnblocked() {
            // huh, how come there were any?
        }
        
        @Override
        public void blockedJobAdded(Job job) {
        }
        
        @Override
        public void searchingStateChanged() {
            if (getRailsSearching().isStarted() && !getRailsSearching().isActive()) {
                // done searching, show results to the user
                view.showManualSelection();
                setState(RESULTS_STATE);
            }
        }
        
    }
    
    /**
     * Searching progress UI is visible.
     */
    private class ResultsState extends State {
        
        @Override
        public void allJobsUnblocked() {
        }
        
        @Override
        public void blockedJobAdded(Job job) {
        }
        
        @Override
        public void searchingStateChanged() {
            if (getRailsSearching().isStarted() && !getRailsSearching().isActive()) {
                // done searching, show results to the user
                view.showManualSelection();
            }
        }
        
    }
    
    private final RailsSearchingView view = new RailsSearchingView(this);
    private State currentState = INVISIBLE_STATE;
    
    public void allJobsUnblocked() {
        currentState.allJobsUnblocked();
    }
    
    public void blockedJobAdded(Job job) {
        currentState.blockedJobAdded(job);
    }
    
    public void searchingStateChanged() {
        currentState.searchingStateChanged();
    }
    
    protected void setState(State state) {
        currentState = state;
    }
    
}
