package com.yoursway.ide.worksheet.internal.controller;

import java.util.LinkedList;
import java.util.Queue;

import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.VerifyEvent;

import com.yoursway.ide.worksheet.UserSettings;
import com.yoursway.ide.worksheet.Worksheet;
import com.yoursway.ide.worksheet.executors.OutputListener;
import com.yoursway.ide.worksheet.executors.WorksheetCommandExecutor;
import com.yoursway.ide.worksheet.internal.view.ResultBlock;

public class WorksheetController implements OutputListener, VerifyKeyListener, ExtendedModifyListener {
    
    private final Worksheet view;
    private final UserSettings settings;
    private final WorksheetCommandExecutor executor;
    
    private final Queue<Execution> executions = new LinkedList<Execution>(); //? sync
    private ResultBlock outputBlock = null;
    
    public WorksheetController(Worksheet view, UserSettings settings) {
        this.view = view;
        this.settings = settings;
        
        executor = settings.executor();
        executor.addOutputListener(this);
    }
    
    public void verifyKey(VerifyEvent e) {
        if (settings.isExecHotkey(e)) {
            e.doit = false;
            
            for (Command command : view.selectedCommands()) {
                executeCommand(command);
            }
            
            if (!view.multilineSelection()) {
                if (view.inLastLine())
                    view.makeNewLineAtEnd();
                else
                    view.lineDown();
            }
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
        
        //> check backspaces which doesn't change the line 
        if (e.length == 1 && view.isNewLineChar(e.start)) //> check line breaking 
            return;
        
        view.makeInsertionsObsolete(start, end);
    }
    
    private void executeCommand(Command command) {
        executions.add(new Execution(command, executor));
        if (executions.size() == 1)
            outputBlock = executions.peek().start();
    }
    
    public synchronized void outputted(String text, boolean error) {
        outputBlock.append(text, error);
    }
    
    public synchronized void completed() {
        executions.poll();
        outputBlock = null;
        
        Execution execution = executions.peek();
        if (execution != null)
            outputBlock = execution.start();
    }
    
}
