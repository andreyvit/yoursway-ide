package com.yoursway.genericmodel.ui.tests.viewmodel;

import java.util.Set;

import com.yoursway.genericmodel.ui.IViewModelRoot;
import com.yoursway.model.repository.ICalculatedModelUpdater;
import com.yoursway.model.repository.IHandle;
import com.yoursway.model.repository.IRepository;
import com.yoursway.model.repository.IResolver;
import com.yoursway.model.repository.ISnapshot;
import com.yoursway.model.tracking.CompleteTrackedSnapshot;
import com.yoursway.model.tracking.IMapSnapshot;

public class ViewModel implements ICalculatedModelUpdater {
    
    public ViewModel(IRepository repository) {
        repository.registerModel(IViewModelRoot.class, new ViewModelRoot(), this);
    }

    public ISnapshot buildInitialSnapshot(IResolver resovler) {
        return new CompleteTrackedSnapshot();
    }

    public void calculateHandle(IHandle<?> handle, IResolver resolver, ISnapshot snapshot,
            Set<IHandle<?>> updatedHandles) {
        IMapSnapshot s = ((IMapSnapshot) snapshot);
    }

}
