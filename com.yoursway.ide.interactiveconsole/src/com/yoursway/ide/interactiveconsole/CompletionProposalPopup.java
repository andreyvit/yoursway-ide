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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class CompletionProposalPopup {
    
    private final Shell shell;
    private final Table proposalTable;
    private final IConsoleForProposalPopup console;
    
    public CompletionProposalPopup(Shell parent, final Console console) {
        shell = new Shell(parent, SWT.ON_TOP | SWT.RESIZE);
        
        proposalTable = new Table(shell, SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
        
        // scrollbars
        
        GridLayout layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        shell.setLayout(layout);
        
        GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
        proposalTable.setLayoutData(data);
        
        shell.pack();
        shell.setSize(200, 100);
        
        //
        
        this.console = console;
        
        Updater updater = new Updater();
        console.addTraverseListener(updater);
        console.addMouseListener(updater);
        console.addModifyListener(updater);
        
        proposalTable.addSelectionListener(new SelectionListener() {
            
            public void widgetDefaultSelected(SelectionEvent e) {
                
            }
            
            public void widgetSelected(SelectionEvent e) {
                CompletionProposal proposal = (CompletionProposal) e.item.getData();
                console.useCompletionProposal(proposal);
                
                hide();
            }
            
        });
    }
    
    public void show() {
        updateProposalList();
        setLocation();
        
        shell.setVisible(true);
    }
    
    public void hide() {
        shell.setVisible(false);
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
    
    private class Updater implements TraverseListener, MouseListener, ModifyListener, Runnable {
        
        public void keyTraversed(TraverseEvent e) {
            update();
        }
        
        public void mouseDoubleClick(MouseEvent e) {
            // nothing
        }
        
        public void mouseDown(MouseEvent e) {
            update();
        }
        
        public void mouseUp(MouseEvent e) {
            // nothing
        }
        
        public void modifyText(ModifyEvent e) {
            update();
        }
        
        private void update() {
            shell.getDisplay().asyncExec(this);
        }
        
        public void run() {
            updateProposalList();
            setLocation();
        }
        
    }
    
}
