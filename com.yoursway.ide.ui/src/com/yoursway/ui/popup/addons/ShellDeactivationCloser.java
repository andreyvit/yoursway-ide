/**
 * 
 */
package com.yoursway.ui.popup.addons;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.services.IDisposable;

import com.yoursway.ui.popup.IPopupLifecycleListener;
import com.yoursway.ui.popup.IPopupVoter;
import com.yoursway.ui.popup.PopupPoll;
import com.yoursway.ui.popup.SnappableYellowPopup;
import com.yoursway.ui.popup.menu.MenuSupport;

/**
 * Closes the given popup when it's shell is deactivated, except when the
 * workbench window shell is activated. (In other words, closes the popup when
 * another popup or dialog opens.)
 * 
 * @author Andrey Tarantsov
 */
public class ShellDeactivationCloser implements IPopupLifecycleListener, IPopupVoter, ShellListener {
    
    private final SnappableYellowPopup popup;
    private boolean listenersInstalled;
    private final IDisposable voterHandle;
    private final IDisposable lifecycleListenerHandle;
    private boolean voteForClosing = false;
    private final MenuSupport menuSupport;
    
    public ShellDeactivationCloser(SnappableYellowPopup popup, MenuSupport menuSupport) {
        Assert.isLegal(popup != null);
        assert popup != null;
        
        this.popup = popup;
        this.menuSupport = menuSupport;
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
        
        // Leave linked mode when popup loses focus
        // (except when focus goes back to workbench window or menu is open):
        popup.getShell().addShellListener(this);
    }
    
    private void uninstallListeners() {
        listenersInstalled = false;
        // do nothing because the shell is disposed
    }
    
    public void popupClosed() {
        uninstallListeners();
    }
    
    public void popupOpened() {
        installListeners();
    }
    
    public void participate(PopupPoll poll) {
        if (voteForClosing)
            poll.voteClose();
    }
    
    public void shellActivated(ShellEvent e) {
    }
    
    public void shellClosed(ShellEvent e) {
    }
    
    public void shellDeactivated(ShellEvent e) {
        if (menuSupport != null && menuSupport.isMenuCurrentlyShown())
            return;
        
        final Shell editorShell = popup.getParentShell();
        final Display display = popup.getShell().getDisplay();
        display.asyncExec(new Runnable() {
            // post to UI thread since editor shell only gets activated after popup has lost focus
            public void run() {
                Shell activeShell = display.getActiveShell();
                if (activeShell != editorShell) {
                    voteForClosing = true;
                    popup.revote();
                }
            }
        });
    }
    
    public void shellDeiconified(ShellEvent e) {
    }
    
    public void shellIconified(ShellEvent e) {
    }
    
}