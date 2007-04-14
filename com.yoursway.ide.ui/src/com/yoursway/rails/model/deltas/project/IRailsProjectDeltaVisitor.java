package com.yoursway.rails.model.deltas.project;


public interface IRailsProjectDeltaVisitor {
    
    public void visitAddedProject(RailsProjectAddedDelta delta);
    
    public void visitRemovedProject(RailsProjectRemovedDelta delta);
    
    public void visitOpenedProject(RailsProjectOpenedDelta delta);
    
    public void visitClosedProject(RailsProjectClosedDelta delta);
    
    public void visitObtainedNatureProject(RailsProjectObtainedNatureDelta delta);
    
    public void visitLostNatureProject(RailsProjectLostNatureDelta delta);
    
    public void visitChangedProject(RailsProjectChangedDelta delta);
    
}
