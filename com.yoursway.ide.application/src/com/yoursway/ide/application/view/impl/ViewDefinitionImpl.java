package com.yoursway.ide.application.view.impl;

import static com.yoursway.utils.DebugOutputHelper.reflectionBasedToString;

import com.google.common.base.Function;
import com.yoursway.ide.application.view.ViewDefinition;
import com.yoursway.utils.UniqueId;

public class ViewDefinitionImpl implements ViewDefinition {
    
    private final UniqueId uniqueId;
    private final ViewArea area;
    
    public ViewDefinitionImpl(UniqueId uniqueId, ViewArea area) {
        if (uniqueId == null)
            throw new NullPointerException("uniqueId is null");
        if (area == null)
            throw new NullPointerException("area is null");
        
        this.uniqueId = uniqueId;
        this.area = area;
        
    }
    
    @Override
    public String toString() {
        return reflectionBasedToString(this);
    }
    
    public UniqueId uniqueId() {
        return uniqueId;
    }
    
    public ViewArea area() {
        return area;
    }
    
    public static final Function<ViewDefinitionImpl, UniqueId> VIEW_DEFINITION_IMPL_TO_UID = new Function<ViewDefinitionImpl, UniqueId>() {
        
        public UniqueId apply(ViewDefinitionImpl from) {
            return from.uniqueId();
        }
        
    };
    
}
