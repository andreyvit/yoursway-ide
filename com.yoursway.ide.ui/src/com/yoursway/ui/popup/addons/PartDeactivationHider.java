/**
 * 
 */
package com.yoursway.ui.popup.addons;

import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.services.IDisposable;

import com.yoursway.ui.popup.IPopupLifecycleListener;
import com.yoursway.ui.popup.IPopupVoter;
import com.yoursway.ui.popup.PopupPoll;
import com.yoursway.ui.popup.SnappableYellowPopup;

/**
 * Hides the given popup while the given part is inactivate.
 * 
 * @author Andrey Tarantsov
 */
public class PartDeactivationHider implements IPartListener2, IPopupLifecycleListener, IPopupVoter {
    
    private final SnappableYellowPopup popup;
    private final IWorkbenchPart parentPart;
    private boolean listenersInstalled;
    private final IDisposable voterHandle;
    private final IDisposable lifecycleListenerHandle;
    
    public PartDeactivationHider(SnappableYellowPopup popup, IWorkbenchPart parentPart) {
        this.popup = popup;
        this.parentPart = parentPart;
        lifecycleListenerHandle = popup.addLifecycleListener(this);
        voterHandle = popup.addVoter(this);
    }
    
    public void dispose() {
        lifecycleListenerHandle.dispose();
        voterHandle.dispose();
    }
    
    private void installListeners() {
        if (listenersInstalled)
            return;
        listenersInstalled = true;
        popup.getPopupOwner().getWorkbenchWindow().getPartService().addPartListener(this);
    }
    
    private void uninstallListeners() {
        if (!listenersInstalled)
            return;
        listenersInstalled = false;
        popup.getPopupOwner().getWorkbenchWindow().getPartService().removePartListener(this);
    }
    
    public void partActivated(IWorkbenchPartReference partRef) {
        if (partRef.getPart(false) == parentPart) {
            popup.revote();
        }
    }
    
    public void partBroughtToTop(IWorkbenchPartReference partRef) {
    }
    
    public void partClosed(IWorkbenchPartReference partRef) {
    }
    
    public void partDeactivated(IWorkbenchPartReference partRef) {
        if (popup.isOpen() && partRef.getPart(false) == parentPart) {
            popup.revote();
        }
    }
    
    public void partHidden(IWorkbenchPartReference partRef) {
    }
    
    public void partInputChanged(IWorkbenchPartReference partRef) {
    }
    
    public void partOpened(IWorkbenchPartReference partRef) {
    }
    
    public void partVisible(IWorkbenchPartReference partRef) {
    }
    
    public void popupClosed() {
        uninstallListeners();
    }
    
    public void popupOpened() {
        installListeners();
    }
    
    public void participate(PopupPoll poll) {
        if (parentPart.getSite().getWorkbenchWindow().getActivePage().getActivePart() != parentPart)
            poll.voteHidden();
    }
    
}