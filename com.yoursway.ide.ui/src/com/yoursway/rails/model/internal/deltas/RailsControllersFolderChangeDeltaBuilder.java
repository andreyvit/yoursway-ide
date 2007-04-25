package com.yoursway.rails.model.internal.deltas;

import java.util.ArrayList;
import java.util.Collection;

import com.yoursway.rails.model.deltas.controllers.ControllerDelta;
import com.yoursway.rails.model.deltas.controllers.ControllersFolderChangedDelta;
import com.yoursway.rails.model.deltas.controllers.ControllersFolderDelta;
import com.yoursway.rails.model.internal.RailsControllersFolder;

public class RailsControllersFolderChangeDeltaBuilder {
    
    private final Collection<ControllersFolderDelta> childFolders = new ArrayList<ControllersFolderDelta>();
    private final Collection<ControllerDelta> controllerDeltas = new ArrayList<ControllerDelta>();
    private final RailsControllersFolder folder;
    
    public RailsControllersFolderChangeDeltaBuilder(RailsControllersFolder folder) {
        this.folder = folder;
    }
    
    public void addChildFolderDelta(ControllersFolderDelta delta) {
        childFolders.add(delta);
    }
    
    public void addControllerDelta(ControllerDelta delta) {
        controllerDeltas.add(delta);
    }
    
    public ControllersFolderChangedDelta build() {
        ControllersFolderDelta[] array = childFolders
                .toArray(new ControllersFolderDelta[childFolders.size()]);
        ControllerDelta[] array2 = controllerDeltas.toArray(new ControllerDelta[controllerDeltas.size()]);
        return new ControllersFolderChangedDelta(folder, array, array2);
    }
}
