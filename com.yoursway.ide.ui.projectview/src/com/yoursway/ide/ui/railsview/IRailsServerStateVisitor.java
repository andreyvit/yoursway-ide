package com.yoursway.ide.ui.railsview;

public interface IRailsServerStateVisitor {
    
    void visit(StartingServerState state);
    
    void visit(StoppingServerState state);
    
    void visit(RunningServerState state);
    
    void visit(StoppedServerState state);
    
}
