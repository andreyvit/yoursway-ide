package com.yoursway.rails.model.tests;



import org.junit.Before;

import com.yoursway.rails.model.RailsRepository;
import com.yoursway.rails.model.RailsProject;
import com.yoursway.tests.commons.AbstractObservableSetTestCase;

public abstract class AbstractRailsProjectsTestCase extends AbstractObservableSetTestCase<RailsProject> {
    
    protected RailsRepository repository;

    @Before
    public void createRepository() {
        repository = new RailsRepository(realm);
    }
    
    protected void observe() {
        observable = repository.observeProjects();
    }
    
}
