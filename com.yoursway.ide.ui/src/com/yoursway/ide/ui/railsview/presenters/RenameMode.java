/**
 * 
 */
package com.yoursway.ide.ui.railsview.presenters;

import java.util.Collection;

import com.yoursway.ui.popup.IPopupHost;
import com.yoursway.ui.popup.SnapPosition;
import com.yoursway.ui.popup.table.TableBasedPopup;
import com.yoursway.ui.popup.trash.RenamePopup;

public final class RenameMode extends PopupBasedMode {
    
    public RenameMode(IRenameContext renameContext) {
        super(renameContext);
    }
    
    @Override
    protected TableBasedPopup createPopup(IPopupHost owner, Collection<SnapPosition> snaps) {
        return new RenamePopup(owner, snaps);
    }
    
}