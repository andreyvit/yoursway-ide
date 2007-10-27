package com.yoursway.ide.ui.railsview.presenters;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.widgets.Text;

public class AcceptOnEnter {
    
    private final Text text;
    private final Runnable handler;
    
    public AcceptOnEnter(Text text, Runnable handler) {
        this.text = text;
        this.handler = handler;
        text.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                handleDefaultSelection(e);
            }
        });
        text.addKeyListener(new KeyAdapter() {
            // hook key pressed - see PR 14201  
            @Override
            public void keyPressed(KeyEvent e) {
                keyReleaseOccured(e);
            }
        });
        text.addTraverseListener(new TraverseListener() {
            public void keyTraversed(TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_RETURN) {
                    e.doit = false;
                }
            }
        });
    }
    
    protected void handleDefaultSelection(SelectionEvent event) {
        handler.run();
    }
    
    protected void keyReleaseOccured(KeyEvent keyEvent) {
        if (keyEvent.character == '\r') { // Return key
            // Enter is handled in handleDefaultSelection.
            // Do not apply the editor value in response to an Enter key event
            // since this can be received from the IME when the intent is -not-
            // to apply the value.  
            // See bug 39074 [CellEditors] [DBCS] canna input mode fires bogus event from Text Control
            //
            // An exception is made for Ctrl+Enter for multi-line texts, since
            // a default selection event is not sent in this case. 
            if (text != null && !text.isDisposed() && (text.getStyle() & SWT.MULTI) != 0) {
                if ((keyEvent.stateMask & SWT.CTRL) != 0) {
                    superKeyReleaseOccured(keyEvent);
                }
            }
            return;
        }
        superKeyReleaseOccured(keyEvent);
    }
    
    protected void superKeyReleaseOccured(KeyEvent keyEvent) {
        if (keyEvent.character == '\r') { // Return key
            handler.run();
        }
    }
    
}
