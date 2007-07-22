package com.yoursway.ide.ui.railsview.presenters;

import java.util.Collection;

import org.eclipse.osgi.util.NLS;

import com.yoursway.common.SegmentedName;
import com.yoursway.ide.ui.railsview.presentation.IPresenterOwner;
import com.yoursway.rails.commons.RailsNamingConventions;
import com.yoursway.rails.core.models.RailsModel;

public class ModelPackagePresenter extends AbstractPackagePresenter {
    
    private final SegmentedName namespace;
    
    public ModelPackagePresenter(IPresenterOwner owner, SegmentedName namespace,
            Collection<RailsModel> controllers) {
        super(owner);
        this.namespace = namespace;
    }
    
    public String getCaption() {
        if (namespace.getSegmentCount() == 0)
            return "Models";
        else
            return NLS.bind("Models in {0}", RailsNamingConventions.joinNamespaces(namespace));
    }
    
}
