package com.yoursway.ide.interactiveconsole;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class CompletionProposalPopup {
    
    private final Shell shell;
    private final Table proposalTable;
    private final Console console;
    
    public CompletionProposalPopup(Shell parent, final Console console) {
        shell = new Shell(parent, SWT.ON_TOP);
        
        proposalTable = new Table(shell, SWT.SINGLE); //? h&v scrollbars
        proposalTable.setBounds(shell.getClientArea());
        
        this.console = console; //? special interface of Console will be better
        
        console.addModifyListener(new ModifyListener() {
            
            public void modifyText(ModifyEvent e) {
                shell.getDisplay().asyncExec(new Runnable() {
                    
                    public void run() {
                        setLocation();
                    }
                    
                });
            }
            
        });
        
        proposalTable.addSelectionListener(new SelectionListener() {
            
            public void widgetDefaultSelected(SelectionEvent e) {
                
            }
            
            public void widgetSelected(SelectionEvent e) {
                CompletionProposal proposal = (CompletionProposal) e.item.getData();
                console.useCompletionProposal(proposal);
                
                shell.setVisible(false);
            }
            
        });
    }
    
    public void show(final List<CompletionProposal> proposals) {
        proposalTable.removeAll();
        
        for (CompletionProposal proposal : proposals) {
            TableItem item = new TableItem(proposalTable, SWT.FULL_SELECTION); //? FULL_SELECTION
            item.setText(proposal.text());
            item.setData(proposal);
        }
        
        setLocation();
        shell.setSize(100, 100);
        
        shell.setVisible(true);
    }
    
    private void setLocation() {
        int offset = console.inputStartOffset(); //? .getSelection().x;
        Point p = console.getLocationAtOffset(offset);
        /*
        p.x -= fProposalShell.getBorderWidth();
        if (p.x < 0) p.x= 0;
        if (p.y < 0) p.y= 0;
        */
        p.y += console.getLineHeight(offset);
        p = console.toDisplay(p);
        //? move to Console
        
        shell.setLocation(p);
    }
}
