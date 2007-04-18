/**
 * 
 */
package com.yoursway.ide.ui.railsview.presentation.misc;

import java.util.Iterator;

import org.eclipse.jface.text.ITextListener;
import org.eclipse.jface.text.IViewportListener;
import org.eclipse.jface.text.TextEvent;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.util.Geometry;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;

import com.yoursway.ide.ui.railsview.presentation.misc.RenameInformationPopup.InfoEntry;

class PopupVisibilityManager implements IPartListener2, ControlListener, MouseListener, KeyListener,
        ITextListener, IViewportListener {
    
    private final RenameInformationPopup parent;
    
    public PopupVisibilityManager(RenameInformationPopup popup) {
        parent = popup;
    }
    
    private IWorkbenchWindow getWorkbenchWindow() {
        return parent.getPopupOwner().getWorkbenchWindow();
    }
    
    private Shell getParentShell() {
        return parent.getPopupOwner().getShell();
    }
    
    public void start() {
        getWorkbenchWindow().getPartService().addPartListener(this);
        final ISourceViewer viewer = getViewer();
        final StyledText textWidget = viewer.getTextWidget();
        textWidget.addControlListener(this);
        textWidget.addMouseListener(this);
        textWidget.addKeyListener(this);
        getParentShell().addControlListener(this);
        viewer.addTextListener(this);
        viewer.addViewportListener(this);
        parent.fPopup.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                getWorkbenchWindow().getPartService().removePartListener(PopupVisibilityManager.this);
                if (!textWidget.isDisposed()) {
                    textWidget.removeControlListener(PopupVisibilityManager.this);
                    textWidget.removeMouseListener(PopupVisibilityManager.this);
                    textWidget.removeKeyListener(PopupVisibilityManager.this);
                }
                getParentShell().removeControlListener(PopupVisibilityManager.this);
                viewer.removeTextListener(PopupVisibilityManager.this);
                viewer.removeViewportListener(PopupVisibilityManager.this);
                if (PopupVisibilityManager.this.parent.fMenuImage != null)
                    PopupVisibilityManager.this.parent.fMenuImage.dispose();
                if (PopupVisibilityManager.this.parent.fMinimizedMenuManager != null)
                    PopupVisibilityManager.this.parent.fMinimizedMenuManager.dispose();
                if (PopupVisibilityManager.this.parent.fTableMenuManager != null)
                    PopupVisibilityManager.this.parent.fTableMenuManager.dispose();
                
                //XXX workaround for bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=173438 :
                //					fRenameLinkedMode.cancel();
                getParentShell().getDisplay().asyncExec(new Runnable() {
                    public void run() {
                        PopupVisibilityManager.this.parent.cancelMode();
                    }
                });
            }
        });
    }
    
    public void partActivated(IWorkbenchPartReference partRef) {
        IWorkbenchPart fPart = getPart();
        if (partRef.getPart(false) == fPart) {
            updateVisibility();
        }
    }
    
    public void partBroughtToTop(IWorkbenchPartReference partRef) {
    }
    
    public void partClosed(IWorkbenchPartReference partRef) {
    }
    
    public void partDeactivated(IWorkbenchPartReference partRef) {
        IWorkbenchPart fPart = getPart();
        if (parent.fPopup != null && !parent.fPopup.isDisposed() && partRef.getPart(false) == fPart) {
            parent.fPopup.setVisible(false);
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
    
    public void controlMoved(ControlEvent e) {
        parent.updatePopupLocation(true);
        updateVisibility(); //only for hiding outside editor area
    }
    
    public void controlResized(ControlEvent e) {
        parent.updatePopupLocation(true);
        updateVisibility(); //only for hiding outside editor area
    }
    
    private void updateVisibility() {
        if (parent.fPopup != null && !parent.fPopup.isDisposed()) {
            boolean visible = false;
            if (true /* fRenameLinkedMode.isCaretInLinkedPosition() */) {
                StyledText textWidget = getViewer().getTextWidget();
                Rectangle eArea = Geometry.toDisplay(textWidget, textWidget.getClientArea());
                Rectangle pBounds = parent.fPopup.getBounds();
                if (eArea.intersects(pBounds)) {
                    visible = true;
                }
            }
            parent.fPopup.setVisible(visible);
        }
    }
    
    public void mouseDoubleClick(MouseEvent e) {
    }
    
    public void mouseDown(MouseEvent e) {
    }
    
    public void mouseUp(MouseEvent e) {
        parent.updatePopupLocation(false);
        updateVisibility();
    }
    
    public void keyPressed(KeyEvent e) {
        parent.updatePopupLocation(false);
        updateVisibility();
    }
    
    public void keyReleased(KeyEvent e) {
    }
    
    public void textChanged(TextEvent event) {
        updateEnablement();
        parent.updatePopupLocation(false);
        updateVisibility(); //only for hiding outside editor area
    }
    
    private void updateEnablement() {
        if (parent.fPopup != null && !parent.fPopup.isDisposed()) {
            boolean enabled = true; //fRenameLinkedMode.isEnabled();
            for (Iterator iterator = parent.fRefactorEntries.iterator(); iterator.hasNext();) {
                InfoEntry entry = (InfoEntry) iterator.next();
                entry.setEnabled(enabled);
            }
        }
    }
    
    public void viewportChanged(int verticalOffset) {
        parent.updatePopupLocation(true);
        updateVisibility(); //only for hiding outside editor area
    }
    
    ISourceViewer getViewer() {
        return null; // TODO FIXME XXX
    }
    
    IWorkbenchPart getPart() {
        return null; // TODO FIXME XXX 
    }
    
}