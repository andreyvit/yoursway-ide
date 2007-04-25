package com.yoursway.ide.ui.railsview.presenters;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.widgets.Control;

public class CancelOnEscape {
    
    private final Control text;
    private final Runnable handler;
    
    public CancelOnEscape(Control control, Runnable handler) {
        this.text = control;
        this.handler = handler;
        text.addKeyListener(new KeyAdapter() {
            // hook key pressed - see PR 14201  
            @Override
            public void keyPressed(KeyEvent e) {
                keyReleaseOccured(e);
            }
        });
        text.addTraverseListener(new TraverseListener() {
            public void keyTraversed(TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_ESCAPE) {
                    e.doit = false;
                }
            }
        });
    }
    
    /**
     * Handles a default selection event from the text control by applying the
     * editor value and deactivating this cell editor.
     * 
     * @param event
     *            the selection event
     * 
     * @since 3.0
     */
    protected void handleDefaultSelection(SelectionEvent event) {
        handler.run();
    }
    
    protected void keyReleaseOccured(KeyEvent keyEvent) {
        if (keyEvent.character == '\u001b') { // Escape character
            handler.run();
        }
    }
    
}
