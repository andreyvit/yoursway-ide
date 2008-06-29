package com.yoursway.ide.application.view;

public interface ViewFactory {
    
    View bindView(ViewDefinition definition, ViewCallback callback);
    
}
