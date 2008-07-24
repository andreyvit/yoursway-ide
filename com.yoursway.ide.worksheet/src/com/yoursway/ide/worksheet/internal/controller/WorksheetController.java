package com.yoursway.ide.worksheet.internal.controller;

import java.util.LinkedList;
import java.util.Queue;

import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.events.VerifyEvent;

import com.yoursway.ide.worksheet.WorksheetShortcuts;
import com.yoursway.ide.worksheet.executors.OutputListener;
import com.yoursway.ide.worksheet.executors.WorksheetCommandExecutor;
import com.yoursway.ide.worksheet.internal.view.ResultInset;
import com.yoursway.ide.worksheet.internal.view.WorksheetView;
import com.yoursway.ide.worksheet.internal.view.WorksheetViewCallback;
import com.yoursway.ide.worksheet.internal.view.WorksheetViewFactory;
import com.yoursway.utils.annotations.SynchronizedWithMonitorOfThis;
import com.yoursway.utils.annotations.UseFromAnyThread;
import com.yoursway.utils.annotations.UseFromUIThread;

public class WorksheetController implements OutputListener, WorksheetViewCallback {
    
    private final WorksheetView view;
    private final WorksheetCommandExecutor executor;
    
    private final Queue<Execution> executions = new LinkedList<Execution>(); //? sync
    private ResultInset outputInset = null;
    private final WorksheetShortcuts shortcuts;
    
    public WorksheetController(WorksheetViewFactory viewFactory, WorksheetCommandExecutor executor,
            WorksheetShortcuts shortcuts) {
        if (executor == null)
            throw new NullPointerException("executor is null");
        if (shortcuts == null)
            throw new NullPointerException("shortcuts is null");
        
        view = viewFactory.createView(this);
        this.executor = executor;
        this.shortcuts = shortcuts;
        
        if (view == null)
            throw new NullPointerException("view created by viewFactory is null");
        
        executor.addOutputListener(this);
    }
    
    public void verifyKey(VerifyEvent e) {
        if (e.doit == false)
            return;
        
        boolean handled = true;
        if (shortcuts.isExecHotkey(e))
            executeSelectedCommands();
        else if (shortcuts.isRemoveInsetsHotkey(e))
            removeAllInsets();
        else if (shortcuts.isShowTextHotkey(e))
            showSelectedText();
        else
            handled = false;
        
        e.doit = !handled;
    }
    
    private void showSelectedText() {
        view.showSelectedText();
    }
    
    private void removeAllInsets() {
        view.removeAllInsets();
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
        
        view.makeInsetsObsolete(start, end);
    }
    
    @UseFromUIThread
    private void executeCommand(Command command) {
        executions.add(new Execution(command, executor));
        if (executions.size() == 1)
            outputInset = executions.peek().start();
    }
    
    @UseFromAnyThread
    @SynchronizedWithMonitorOfThis
    public synchronized void outputted(String text, boolean error) {
        outputInset.append(text, error);
    }
    
    @UseFromAnyThread
    @SynchronizedWithMonitorOfThis
    public synchronized void completed() {
        executions.poll();
        outputInset = null;
        
        Execution execution = executions.peek();
        if (execution != null)
            outputInset = execution.start();
    }
    
}
