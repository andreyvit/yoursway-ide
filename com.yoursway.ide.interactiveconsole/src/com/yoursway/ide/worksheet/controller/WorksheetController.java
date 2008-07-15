package com.yoursway.ide.worksheet.controller;

import java.util.LinkedList;
import java.util.Queue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.VerifyEvent;

import com.yoursway.ide.debug.model.IDebug;
import com.yoursway.ide.debug.model.IOutputListener;
import com.yoursway.ide.worksheet.view.Insertion;
import com.yoursway.ide.worksheet.view.Worksheet;
import com.yoursway.ide.worksheet.viewmodel.IUserSettings;

public class WorksheetController implements VerifyKeyListener, KeyListener, IOutputListener {
    
    private final Worksheet view;
    private final IUserSettings settings;
    private final IDebug debug;
    
    private final Queue<Execution> executions = new LinkedList<Execution>(); //? sync
    private Insertion outputInsertion = null;
    
    public WorksheetController(Worksheet view, IUserSettings settings) {
        this.view = view;
        this.settings = settings;
        
        debug = settings.debug();
        debug.addOutputListener(this);
    }
    
    public void verifyKey(VerifyEvent e) {
        if (settings.isExecHotkey(e)) {
            e.doit = false;
            if (!view.command().trim().equals("")) {
                executeCommand();
                view.selectInsertionLineEnd();
            }
            if (view.inLastLine())
                view.newLineAtEnd();
            else
                view.lineDown();
        }

        else if (settings.isRemoveInsertionsHotkey(e)) {
            view.removeAllInsertions();
        }

        else if (e.character == '\n' || e.character == '\r') {
            if (view.lineHasInsertion())
                view.selectInsertionLineEnd();
        }

        else {
            // not handle or block other keys
        }
    }
    
    public void keyPressed(KeyEvent e) {
        if (view.inInsertionLine()) {
            if (e.keyCode == SWT.ARROW_UP || e.keyCode == SWT.ARROW_LEFT || e.character == '\b') {
                view.lineUp();
            }

            else if (e.keyCode == SWT.ARROW_DOWN || e.keyCode == SWT.ARROW_RIGHT) {
                if (view.inLastLine())
                    view.lineUp();
                else
                    view.lineDown();
            }

            else {
                // not handle or block other keys
            }
        }
    }
    
    public void keyReleased(KeyEvent e) {
        // nothing                
    }
    
    private synchronized void executeCommand() {
        executions.add(new Execution(view.command(), view.insertion(), debug));
        if (executions.size() == 1)
            outputInsertion = executions.peek().start();
    }
    
    public synchronized void outputted(String text, boolean error) {
        outputInsertion.append(text, error);
    }
    
    public synchronized void completed() {
        executions.poll();
        outputInsertion = null;
        
        Execution execution = executions.peek();
        if (execution != null)
            outputInsertion = execution.start();
    }
    
}
