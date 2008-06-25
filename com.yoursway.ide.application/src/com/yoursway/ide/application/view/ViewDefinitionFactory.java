package com.yoursway.ide.application.view;

import com.yoursway.ide.application.view.impl.ViewArea;
import com.yoursway.utils.UniqueId;

public interface ViewDefinitionFactory {
    
    ViewDefinition defineView(UniqueId uniqueId, ViewArea role);

//    Collection<? extends ViewDefinition> filterByRole(ViewSiteRole role);
//
//    ViewDefinition findOneByRole(ViewSiteRole role);
    
}
