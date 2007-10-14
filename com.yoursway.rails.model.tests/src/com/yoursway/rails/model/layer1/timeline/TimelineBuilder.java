package com.yoursway.rails.model.layer1.timeline;

import java.util.ArrayList;
import java.util.Collection;

import com.yoursway.rails.model.layer1.models.ModelFamily;

public class TimelineBuilder {
    
    private Collection<ModelFamily<?>> basicFamilies = new ArrayList<ModelFamily<?>>();
    
    public void addBasicFamily(ModelFamily<?> family) {
        basicFamilies.add(family);
    }
    
    public Timeline build() {
        return new Timeline(basicFamilies);
    }
    
}
