package com.yoursway.ide.ui.railsview;

public class StoppingServerState extends RailsServerState {
    @Override
    public void visit(IRailsServerStateVisitor visitor) {
        visitor.visit(this);
    }
}
