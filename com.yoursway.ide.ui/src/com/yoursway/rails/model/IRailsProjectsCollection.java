package com.yoursway.rails.model;

import java.util.Collection;

public interface IRailsProjectsCollection extends IRailsElement {
    
    Collection<? extends IRailsProject> getRailsProjects();
    
}
