package com.yoursway.ide.ui.railsview.presenters;

import java.util.Collection;

import org.eclipse.osgi.util.NLS;

import com.yoursway.ide.ui.railsview.presentation.IPresenterOwner;
import com.yoursway.rails.core.controllers.RailsController;
import com.yoursway.utils.RailsNamingConventions;
import com.yoursway.utils.SegmentedName;

public class ControllerPackagePresenter extends AbstractPackagePresenter {
    
    private final SegmentedName namespace;
    
    public ControllerPackagePresenter(IPresenterOwner owner, SegmentedName namespace,
            Collection<RailsController> controllers) {
        super(owner);
        this.namespace = namespace;
    }
    
    public String getCaption() {
        if (namespace.getSegmentCount() == 0)
            return "Controllers";
        else
            return NLS.bind("Controllers in {0}", RailsNamingConventions.joinNamespaces(namespace));
    }
    
}
