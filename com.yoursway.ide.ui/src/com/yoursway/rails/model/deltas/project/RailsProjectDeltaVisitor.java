package com.yoursway.rails.model.deltas.project;

public class RailsProjectDeltaVisitor implements IRailsProjectDeltaVisitor {
    
    public void visitAddedProject(RailsProjectAddedDelta delta) {
        visitNewlyAppearedProject(delta);
    }
    
    public void visitChangedProject(RailsProjectChangedDelta delta) {
        visitProject(delta);
    }
    
    public void visitClosedProject(RailsProjectClosedDelta delta) {
        visitDisappearedProject(delta);
    }
    
    public void visitLostNatureProject(RailsProjectLostNatureDelta delta) {
        visitDisappearedProject(delta);
    }
    
    public void visitObtainedNatureProject(RailsProjectObtainedNatureDelta delta) {
        visitNewlyAppearedProject(delta);
    }
    
    public void visitOpenedProject(RailsProjectOpenedDelta delta) {
        visitNewlyAppearedProject(delta);
    }
    
    public void visitRemovedProject(RailsProjectRemovedDelta delta) {
        visitDisappearedProject(delta);
    }
    
    public void visitNewlyAppearedProject(RailsProjectDelta delta) {
        visitProject(delta);
    }
    
    public void visitDisappearedProject(RailsProjectDelta delta) {
        visitProject(delta);
    }
    
    public void visitProject(RailsProjectDelta delta) {
        // does nothing
    }
    
}
