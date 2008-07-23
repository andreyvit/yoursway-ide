package com.yoursway.ide.worksheet.internal.controller;

import java.util.LinkedList;
import java.util.Queue;

import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.events.VerifyEvent;

import com.yoursway.ide.worksheet.WorksheetShortcuts;
import com.yoursway.ide.worksheet.executors.OutputListener;
import com.yoursway.ide.worksheet.executors.WorksheetCommandExecutor;
import com.yoursway.ide.worksheet.internal.view.ResultBlock;
import com.yoursway.ide.worksheet.internal.view.WorksheetView;
import com.yoursway.ide.worksheet.internal.view.WorksheetViewCallback;
import com.yoursway.ide.worksheet.internal.view.WorksheetViewFactory;

public class WorksheetController implements OutputListener, WorksheetViewCallback {
    
    private final WorksheetView view;
    private final WorksheetCommandExecutor executor;
    
    private final Queue<Execution> executions = new LinkedList<Execution>(); //? sync
    private ResultBlock outputBlock = null;
    private final WorksheetShortcuts shortcuts;
    
    public WorksheetController(WorksheetViewFactory viewFactory, WorksheetCommandExecutor executor,
            WorksheetShortcuts shortcuts) {
        this.view = viewFactory.createView(this);
        this.executor = executor;
        this.shortcuts = shortcuts;
        
        executor.addOutputListener(this);
    }
    
    public void verifyKey(VerifyEvent e) {
        if (e.doit == false)
            return;
        
        boolean handled = true;
        if (shortcuts.isExecHotkey(e))
            executeSelectedCommands();
        else if (shortcuts.isRemoveInsertionsHotkey(e))
            removeAllInsertion();
        else if (shortcuts.isShowTextHotkey(e))
            showSelectedText();
        else
            handled = false;
        
        e.doit = !handled;
    }
    
    private void showSelectedText() {
        view.showSelectedText();
    }
    
    private void removeAllInsertion() {
        view.removeAllInsertions();
    }
    
    private void executeSelectedCommands() {
        for (Command command : view.selectedCommands())
            executeCommand(command);
        
        if (!view.multilineSelection())
            if (view.inLastLine())
                view.makeNewLineAtEnd();
            else
                view.lineDown();
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
