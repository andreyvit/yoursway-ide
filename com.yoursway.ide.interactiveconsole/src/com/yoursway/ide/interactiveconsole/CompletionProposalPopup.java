package com.yoursway.ide.interactiveconsole;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class CompletionProposalPopup {
    
    private final Shell shell;
    private final Table proposalTable;
    private final IConsoleForProposalPopup console;
    
    public CompletionProposalPopup(Shell parent, final Console console) {
        shell = new Shell(parent, SWT.ON_TOP);
        
        proposalTable = new Table(shell, SWT.SINGLE); //? h&v scrollbars
        proposalTable.setBounds(shell.getClientArea());
        
        this.console = console;
        
        final Runnable update = new Runnable() {
            
            public void run() {
                updateProposalList();
                setLocation();
            }
            
        };
        
        console.addTraverseListener(new TraverseListener() {
            
            public void keyTraversed(TraverseEvent e) {
                shell.getDisplay().asyncExec(update);
            }
            
        });
        
        console.addMouseListener(new MouseListener() {
            
            public void mouseDoubleClick(MouseEvent e) {
                
            }
            
            public void mouseDown(MouseEvent e) {
                shell.getDisplay().asyncExec(update);
            }
            
            public void mouseUp(MouseEvent e) {
                
            }
            
        });
        
        console.addModifyListener(new ModifyListener() {
            
            public void modifyText(ModifyEvent e) {
                shell.getDisplay().asyncExec(update);
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
    
    public void show() {
        updateProposalList();
        setLocation();
        shell.setSize(100, 100);
        shell.setVisible(true);
    }
    
    private void updateProposalList() {
        
        proposalTable.removeAll();
        
        List<CompletionProposal> proposals = console.getCompletionProposals();
        for (CompletionProposal proposal : proposals) {
            TableItem item = new TableItem(proposalTable, SWT.FULL_SELECTION); //? FULL_SELECTION
            item.setText(proposal.text());
            item.setData(proposal);
        }
    }
    
    private void setLocation() {
        shell.setLocation(console.getLocationForPopup());
    }
}
