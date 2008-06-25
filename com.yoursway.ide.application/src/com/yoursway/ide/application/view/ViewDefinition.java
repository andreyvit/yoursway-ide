package com.yoursway.ide.application.view;

import com.yoursway.ide.application.view.impl.ViewArea;
import com.yoursway.utils.UniqueId;

public interface ViewDefinition {
    
    UniqueId uniqueId();

    ViewArea area();
    
}
