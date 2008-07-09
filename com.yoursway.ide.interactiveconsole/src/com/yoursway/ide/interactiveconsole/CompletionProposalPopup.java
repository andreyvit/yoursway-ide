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
        //? parent = console.getShell();
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
                apply();
            }
            
            public void widgetSelected(SelectionEvent e) {
                
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
        List<CompletionProposal> proposals = console.getCompletionProposals();
        
        if (proposalsChanged(proposals)) {
            
            proposalTable.removeAll();
            
            for (CompletionProposal proposal : proposals) {
                TableItem item = new TableItem(proposalTable, SWT.FULL_SELECTION); //? FULL_SELECTION
                item.setText(proposal.text());
                item.setData(proposal);
            }
        }
    }
    
    private boolean proposalsChanged(List<CompletionProposal> proposals) {
        // compare proposal lists: current & new
        
        if (proposalTable.getItemCount() != proposals.size())
            return true;
        
        for (int i = 0; i < proposals.size(); i++) {
            CompletionProposal proposal = (CompletionProposal) proposalTable.getItem(i).getData();
            if (!proposals.get(i).equals(proposal))
                return true;
        }
        
        return false;
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
    
    public void showOrSelectNext() {
        if (!shell.getVisible())
            show();
        else {
            int i = proposalTable.getSelectionIndex();
            i++;
            if (i == proposalTable.getItemCount())
                i = 0;
            selectAndScroll(i);
        }
    }
    
    public void selectPrevious() {
        int i = proposalTable.getSelectionIndex();
        i--;
        if (i < 0)
            i = proposalTable.getItemCount() - 1;
        selectAndScroll(i);
    }

    private void selectAndScroll(int itemIndex) {
        proposalTable.select(itemIndex);
        proposalTable.showItem(proposalTable.getItem(itemIndex));
    }
    
    public boolean visible() {
        return shell.getVisible();
    }
    
    public void apply() {
        TableItem[] selection = proposalTable.getSelection();
        if (selection.length == 1) {
            CompletionProposal proposal = (CompletionProposal) selection[0].getData();
            console.useCompletionProposal(proposal);
        }
        hide();
    }
}
