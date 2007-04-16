/**
 * 
 */
package com.yoursway.ide.ui.railsview.presenters;

import org.eclipse.jface.resource.ImageDescriptor;

import com.yoursway.ide.ui.railsview.RailsViewImages;
import com.yoursway.ide.ui.railsview.presentation.IPresenterOwner;
import com.yoursway.rails.model.IRailsView;

public class ViewPresenter extends BaseViewPresenter {
    
    private final IRailsView railsView;
    
    public ViewPresenter(IPresenterOwner owner, IRailsView railsView) {
        super(owner, railsView);
        this.railsView = railsView;
    }
    
    @Override
    public boolean canEditInPlace() {
        return false;
    }
    
    public String getCaption() {
        return railsView.getName() + " (" + railsView.getFormat().toString() + ")";
    }
    
    public ImageDescriptor getImage() {
        return RailsViewImages.VIEW_ICON;
    }
    
}