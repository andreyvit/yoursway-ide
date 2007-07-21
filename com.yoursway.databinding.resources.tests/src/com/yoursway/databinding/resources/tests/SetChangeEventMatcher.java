package com.yoursway.databinding.resources.tests;

import static java.util.Arrays.asList;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.databinding.observable.set.SetChangeEvent;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class SetChangeEventMatcher extends BaseMatcher<SetChangeEvent> {
    
    private Set<Object> additions = new HashSet<Object>();
    
    private Set<Object> removals = new HashSet<Object>();
    
    public SetChangeEventMatcher additions(Object... additions) {
        this.additions.addAll(asList(additions));
        return this;
    }
    
    public SetChangeEventMatcher removals(Object... additions) {
        this.removals.addAll(asList(additions));
        return this;
    }
    
    public boolean matches(Object item) {
        SetChangeEvent event = (SetChangeEvent) item;
        return event.diff.getAdditions().equals(additions) && event.diff.getRemovals().equals(removals);
    }
    
    public void describeTo(Description description) {
        description.appendText("SetChangeEventMatcher")
        .appendValueList(" +(", ", ", ")", additions)
        .appendValueList(" -(", ", ", ")", removals)
        ;
    }
    
}
