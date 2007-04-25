/**
 * 
 */
package com.yoursway.ide.ui.railsview.presenters;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.widgets.Tree;

import com.yoursway.ide.ui.Activator;
import com.yoursway.ide.ui.railsview.presentation.ControlSnaps;
import com.yoursway.ide.ui.railsview.presentation.TreeSnaps;
import com.yoursway.rails.windowmodel.INonModalMode;
import com.yoursway.rails.windowmodel.RailsWindowModel;
import com.yoursway.ui.popup.IPopupHost;
import com.yoursway.ui.popup.IPopupLifecycleListener;
import com.yoursway.ui.popup.SnapPosition;
import com.yoursway.ui.popup.addons.PartDeactivationHider;
import com.yoursway.ui.popup.addons.PopupSnapPositionPersister;
import com.yoursway.ui.popup.addons.ShellDeactivationCloser;
import com.yoursway.ui.popup.menu.SnapToMenuItems;
import com.yoursway.ui.popup.trash.RenamePopup;
import com.yoursway.utils.StringUtils;

public final class RenameMode implements INonModalMode {
    
    private final IRenameContext renameContext;
    private final TreeEditor treeEditor;
    private RenamePopup popup;
    private TextEditorPopup popup2;
    
    public RenameMode(IRenameContext renameContext) {
        this.renameContext = renameContext;
        
        treeEditor = new TreeEditor(renameContext.getTree());
        treeEditor.horizontalAlignment = SWT.LEFT;
        treeEditor.verticalAlignment = SWT.TOP;
        treeEditor.grabHorizontal = true;
        treeEditor.minimumWidth = 100;
    }
    
    public void enterMode() {
        RailsWindowModel.instance().getWindow(renameContext.getWorkbenchPage().getWorkbenchWindow())
                .activateMode(this);
        openEditor();
        openPopup();
    }
    
    public void dispose() {
        if (popup != null)
            popup.close();
    }
    
    private void openEditor() {
        Tree baseControl = renameContext.getTree();
        popup2 = new TextEditorPopup(baseControl);
        final String initialText = StringUtils.join(renameContext.getRailsController().getPathComponents(),
                "/");
        popup2.getTextControl().setText(initialText);
        popup2.getTextControl().selectAll();
        Runnable cancelHandler = new Runnable() {
            
            public void run() {
                closeAll(true);
            }
            
        };
        Runnable acceptHandler = new Runnable() {
            
            public void run() {
                closeAll(true);
            }
            
        };
        new CancelOnEscape(popup2.getTextControl(), cancelHandler);
        new AcceptOnEnter(popup2.getTextControl(), acceptHandler);
        treeEditor.setItem(renameContext.getTreeItem());
        popup2.open(treeEditor);
    }
    
    protected void closeAll(boolean setFocus) {
        popup.close();
        if (setFocus)
            renameContext.getTree().setFocus();
    }
    
    private void openPopup() {
        IPopupHost owner = new PopupHostAdapter(renameContext);
        Collection<SnapPosition> snaps = new ArrayList<SnapPosition>();
        ControlSnaps treeItemSnaps = new ControlSnaps(popup2.getControl());
        TreeSnaps treeSnaps = new TreeSnaps(renameContext.getTree());
        treeItemSnaps.addTo(snaps);
        treeSnaps.addTo(snaps);
        popup = new RenamePopup(owner, snaps);
        new PopupSnapPositionPersister(popup, Activator.getDialogSettingsSection("RenamePopup"));
        new ShellDeactivationCloser(popup, popup.getMenuSupport());
        SnapToMenuItems snapToMenu = new SnapToMenuItems(popup, popup.getMenuSupport());
        treeItemSnaps.addTo(snapToMenu);
        snapToMenu.addSeparator();
        treeSnaps.addTo(snapToMenu);
        new PartDeactivationHider(popup, renameContext.getWorkbenchPage().getActivePart());
        popup.open();
        popup.addLifecycleListener(new IPopupLifecycleListener() {
            
            public void popupClosed() {
                modeDeactivated();
            }
            
            public void popupOpened() {
            }
            
        });
    }
    
    private void modeDeactivated() {
        closeEditor();
        RailsWindowModel.instance().getWindow(renameContext.getWorkbenchPage().getWorkbenchWindow())
                .modeDeactivated(this);
    }
    
    private void closeEditor() {
        popup2.dispose();
    }
    
    public void leave() {
        closeAll(false);
    }
    
}