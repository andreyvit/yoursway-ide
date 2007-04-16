/**
 * 
 */
package com.yoursway.ide.ui.railsview.presenters;

import org.eclipse.jface.resource.ImageDescriptor;

import com.yoursway.ide.ui.railsview.RailsViewImages;
import com.yoursway.ide.ui.railsview.presentation.IPresenterOwner;
import com.yoursway.rails.model.IRailsPartial;

public class PartialPresenter extends BaseViewPresenter {
    
    private final IRailsPartial railsView;
    
    public PartialPresenter(IPresenterOwner owner, IRailsPartial railsView) {
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
        return RailsViewImages.PARTIAL_ICON;
    }
    
}