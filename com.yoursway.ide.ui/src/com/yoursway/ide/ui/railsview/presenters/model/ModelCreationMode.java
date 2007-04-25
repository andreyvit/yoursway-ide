/**
 * 
 */
package com.yoursway.ide.ui.railsview.presenters.model;

import java.util.Collection;

import com.yoursway.ide.ui.railsview.presenters.CreationPopup;
import com.yoursway.ide.ui.railsview.presenters.IRenameContext;
import com.yoursway.ide.ui.railsview.presenters.PopupBasedMode;
import com.yoursway.ui.popup.IPopupHost;
import com.yoursway.ui.popup.SnapPosition;
import com.yoursway.ui.popup.table.TableBasedPopup;

public final class ModelCreationMode extends PopupBasedMode {
    
    public ModelCreationMode(IRenameContext renameContext) {
        super(renameContext);
    }
    
    @Override
    protected TableBasedPopup createPopup(IPopupHost owner, Collection<SnapPosition> snaps) {
        return new CreationPopup(owner, snaps);
    }
    
    @Override
    protected String createHint() {
        return "stock/quote or Stock::Quote";
    }
    
}