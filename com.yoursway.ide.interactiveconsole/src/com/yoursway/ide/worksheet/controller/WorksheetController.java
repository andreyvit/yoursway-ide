package com.yoursway.ide.worksheet.controller;

import java.util.LinkedList;
import java.util.Queue;

import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.VerifyEvent;

import com.yoursway.ide.debug.model.IDebug;
import com.yoursway.ide.debug.model.IOutputListener;
import com.yoursway.ide.worksheet.view.Insertion;
import com.yoursway.ide.worksheet.view.Worksheet;
import com.yoursway.ide.worksheet.viewmodel.IUserSettings;

public class WorksheetController implements IOutputListener, VerifyKeyListener, ExtendedModifyListener {
    
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
            if (view.command().trim().length() != 0) {
                executeCommand();
                view.selectInsertionLineEnd();
            }
            if (view.inLastLine())
                view.makeNewLineAtEnd();
            else
                view.lineDown();
        }

        else if (settings.isRemoveInsertionsHotkey(e)) {
            view.removeAllInsertions();
        }

        else if (settings.isShowTextHotkey(e)) {
            view.showSelectedText();
        }

        else {
            // not handle or block other keys
        }
    }
    
    public void modifyText(ExtendedModifyEvent e) {
        int start = e.start;
        int end = e.start + (e.length > 0 ? e.length - 1 : 0);
        
        if (end >= start) { //!
            String text = ((StyledText) e.widget).getText(start, end); //!
            if (text.equals("\n" + settings.insertionPlaceholder()) || text.equals("\n")) //! hack
                return;
        }
        
        view.makeInsertionsObsolete(start, end);
    }
    
    private void executeCommand() {
        executions.add(new Execution(view.command(), view.insertion(), debug));
        if (executions.size() == 1)
            outputInsertion = executions.peek().start();
    }
    
    public synchronized void outputted(String text, boolean error) {
        outputInsertion.append(text, error);
    }
    
    public synchronized void completed() {
        executions.poll();
        outputInsertion.updateSize();
        outputInsertion = null;
        
        Execution execution = executions.peek();
        if (execution != null)
            outputInsertion = execution.start();
    }
    
}
