/**
 * 
 */
package com.yoursway.rails.model.tests.layer1.timeline;

import java.util.GregorianCalendar;

import com.yoursway.model.repository.IBasicModelChangesRequestor;
import com.yoursway.model.repository.IBasicModelRegistry;
import com.yoursway.model.repository.IModelRoot;
import com.yoursway.model.repository.ModelDelta;
import com.yoursway.model.repository.SnapshotBuilder;

class MockModel {
    private final IBasicModelChangesRequestor requestor;
    
    int ticks;
    private final Class<? extends IModelRoot> rootClass;
    
    private String answer;
    
    public MockModel(IBasicModelRegistry registry, Class<? extends IModelRoot> rootClass, IModelRoot root) {
        this.rootClass = rootClass;
        requestor = registry.addBasicModel(((Class<IModelRoot>) rootClass), root);
        ticks = 0;
        answer = "42";
    }
    
    public int ticks() {
        return ticks;
    }
    
    public void setAnswer(String ans) {
        this.answer = ans;
    }
    
    public void tick() {
        System.out.println("MockModel.tick()");
        ticks++;
        SnapshotBuilder sb = new SnapshotBuilder();
        sb.put(new StubHandle("shit", rootClass), GregorianCalendar.getInstance().getTime().toString());
        sb.put(new StubHandle("milli", rootClass), Long.toString(System.currentTimeMillis()));
        sb.put(new StubHandle("answer", rootClass), answer);
        requestor.modelChanged(sb.getSnapshot(), new ModelDelta(sb.getChangedHandles(), sb
                .getAddedElements(), sb.getRemovedElements()));
        
    }
    
}