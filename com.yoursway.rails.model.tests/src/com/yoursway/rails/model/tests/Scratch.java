package com.yoursway.rails.model.tests;

import org.junit.Before;
import org.junit.Test;

import com.yoursway.rails.model.RailsRepository;
import com.yoursway.rails.model.commands.CommandHistory;

public class Scratch {
    
    private CommandHistory history;
    private RailsRepository repository;
    
    @Before
    public void setUp() {
        history = new CommandHistory();
        repository = new RailsRepository();
        
    }
    
    @Test
    public void dick1() {
        ModelFamily family = new ModelFamily();
        FooModelInstance instance = new FooModelInstance();
        FooSnapshotBuilder builder = instance.createSnapshotBuilder(new MockResourceSnapshot());
        
    }
    
}