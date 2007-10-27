package com.yoursway.ide.ui.railsview.presenters;

import java.util.Collection;

import org.eclipse.osgi.util.NLS;

import com.yoursway.ide.ui.railsview.presentation.IPresenterOwner;

public class ModelPackagePresenter extends PackagePresenter {
    
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
