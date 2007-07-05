package com.yoursway.rails.core.fixtures;

public interface IRailsFixturesListener {
    
    void fixtureAdded(RailsFixture railsFixture);
    
    void fixtureRemoved(RailsFixture railsFixture);
    
    void reconcile(RailsFixture railsFixture);
    
}
