/**
 * 
 */
package com.yoursway.ide.ui.railsview.presenters;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;

import com.yoursway.ide.ui.Activator;
import com.yoursway.ide.ui.railsview.presentation.ControlSnaps;
import com.yoursway.ide.ui.railsview.presentation.TreeSnaps;
import com.yoursway.ide.windowing.INonModalMode;
import com.yoursway.ide.windowing.RailsWindowModel;
import com.yoursway.ui.popup.IPopupHost;
import com.yoursway.ui.popup.IPopupLifecycleListener;
import com.yoursway.ui.popup.SnapPosition;
import com.yoursway.ui.popup.addons.PartDeactivationHider;
import com.yoursway.ui.popup.addons.PopupSnapPositionPersister;
import com.yoursway.ui.popup.addons.ShellDeactivationCloser;
import com.yoursway.ui.popup.menu.SnapToMenuItems;
import com.yoursway.ui.popup.table.TableBasedPopup;

public abstract class PopupBasedMode implements INonModalMode {
    
    private final IRenameContext renameContext;
    private final TreeEditor treeEditor;
    private TableBasedPopup popup;
    private TextEditorPopup popup2;
    
    public PopupBasedMode(IRenameContext renameContext) {
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
        final String initialText = renameContext.getInitialValue();
        popup2.getTextControl().setText(initialText);
        popup2.getTextControl().selectAll();
        Runnable cancelHandler = new Runnable() {
            
            public void run() {
                closeAll(true);
            }
            
        };
        Runnable acceptHandler = new Runnable() {
            
            public void run() {
                String value = popup2.getTextControl().getText();
                renameContext.setValue(value);
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
        popup = createPopup(owner, snaps);
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
        final String hint = createHint();
        popup2.getTextControl().setText(hint);
        popup2.getTextControl().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
        popup2.getTextControl().addModifyListener(new ModifyListener() {
            
            public void modifyText(ModifyEvent e) {
                final Text text = popup2.getTextControl();
                if (text.getText().equals(hint))
                    ; // do nothing
                else if (text.getText().length() == 0) {
                    text.setText(hint);
                    text.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
                } else {
                    String t = text.getText();
                    int pos = t.indexOf(hint);
                    if (pos >= 0) {
                        Point sel = text.getSelection();
                        final int end = pos + hint.length();
                        t = t.substring(0, pos) + t.substring(end);
                        text.setText(t);
                        if (sel.x >= end)
                            sel.x -= hint.length();
                        else if (sel.x >= pos)
                            sel.x = pos;
                        if (sel.y >= end)
                            sel.y -= hint.length();
                        else if (sel.y >= pos)
                            sel.y = pos;
                        text.setSelection(sel);
                        text.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_FOREGROUND));
                    }
                }
            }
            
        });
        popup2.getTextControl().addKeyListener(new KeyListener() {
            
            public void keyPressed(KeyEvent e) {
                final Text text = popup2.getTextControl();
                if (text.getText().equals(hint)) {
                    Point selection = text.getSelection();
                    if (selection.x > 0 || selection.y > 0)
                        text.setSelection(0, 0);
                }
            }
            
            public void keyReleased(KeyEvent e) {
                final Text text = popup2.getTextControl();
                if (text.getText().equals(hint)) {
                    Point selection = text.getSelection();
                    if (selection.x > 0 || selection.y > 0)
                        text.setSelection(0, 0);
                }
            }
            
        });
        popup2.getTextControl().addMouseListener(new MouseListener() {
            
            public void mouseDoubleClick(MouseEvent e) {
            }
            
            public void mouseDown(MouseEvent e) {
                final Text text = popup2.getTextControl();
                if (text.getText().equals(hint)) {
                    Point selection = text.getSelection();
                    if (selection.x > 0 || selection.y > 0)
                        text.setSelection(0, 0);
                }
            }
            
            public void mouseUp(MouseEvent e) {
            }
            
        });
    }
    
    protected abstract String createHint();
    
    protected abstract TableBasedPopup createPopup(IPopupHost owner, Collection<SnapPosition> snaps);
    
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