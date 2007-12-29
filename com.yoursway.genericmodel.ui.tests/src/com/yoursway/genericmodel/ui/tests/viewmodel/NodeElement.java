package com.yoursway.genericmodel.ui.tests.viewmodel;

import java.util.Collection;

import com.yoursway.genericmodel.ui.INodeElement;
import com.yoursway.genericmodel.ui.IViewModelRoot;
import com.yoursway.genericmodel.ui.Visualizer;
import com.yoursway.model.repository.IHandle;
import com.yoursway.model.repository.IModelRoot;
import com.yoursway.model.repository.ISnapshot;
import com.yoursway.model.tracking.IMapSnapshot;

public class NodeElement implements INodeElement {
    
    private final NodeElement parent;

    public NodeElement(NodeElement parent) {
        this.parent = parent;
    }

    public IHandle<Collection<INodeElement>> children() {
        return new IHandle<Collection<INodeElement>>() {

            public Class<? extends IModelRoot> getModelRootInterface() {
                return IViewModelRoot.class;
            }

            public Collection<INodeElement> resolve(ISnapshot snapshot) {
                return ((IMapSnapshot) snapshot).get(this);
            }
            
            @Override
            public String toString() {
                return "children";
            }
            
        };
    }

    public INodeElement getParent() {
        return parent;
    }

    public IHandle<Visualizer> visualizer() {
        return new IHandle<Visualizer>() {

            public Class<? extends IModelRoot> getModelRootInterface() {
                return IViewModelRoot.class;
            }

            public Visualizer resolve(ISnapshot snapshot) {
                return ((IMapSnapshot) snapshot).get(this);
            }
            
            @Override
            public String toString() {
                return "visualizer";
            }
            
        };
    }
    
}
