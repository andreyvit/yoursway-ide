/**
 * 
 */
package com.yoursway.ide.ui.railsview.presenters.controller;

import java.util.Collection;

import com.yoursway.ide.ui.railsview.presenters.IRenameContext;
import com.yoursway.ide.ui.railsview.presenters.PopupBasedMode;
import com.yoursway.ui.popup.IPopupHost;
import com.yoursway.ui.popup.SnapPosition;
import com.yoursway.ui.popup.table.TableBasedPopup;

public final class ControllerCreationMode extends PopupBasedMode {
    
    public ControllerCreationMode(IRenameContext renameContext) {
        super(renameContext);
    }
    
    @Override
    protected TableBasedPopup createPopup(IPopupHost owner, Collection<SnapPosition> snaps) {
        return new ControllerCreationPopup(owner, snaps);
    }
    
}