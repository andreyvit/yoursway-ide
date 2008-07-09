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
        shell = new Shell(console.getShell(), SWT.ON_TOP | SWT.RESIZE);
        proposalTable = new Table(shell, SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
        addScrollbars();
        
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
    
    private void addScrollbars() {
        GridLayout layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        shell.setLayout(layout);
        
        GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
        proposalTable.setLayoutData(data);
        
        shell.pack();
        shell.setSize(200, 100);
    }
    
    public void show() {
        updateProposalList();
        setLocation();
        
        if (proposalTable.getItemCount() == 1)
            apply(proposalTable.getItem(0), true);
        else
            shell.setVisible(true);
    }
    
    public void hide() {
        shell.setVisible(false);
    }
    
    public boolean visible() {
        return shell.getVisible();
    }
    
    private void updateProposalList() {
        List<CompletionProposal> proposals = console.getCompletionProposals();
        
        if (proposals == null) {
            hide();
            return;
        }
        
        if (proposalsChanged(proposals)) {
            proposalTable.removeAll();
            
            for (CompletionProposal proposal : proposals) {
                TableItem item = new TableItem(proposalTable, SWT.NULL);
                item.setText(proposal.text());
                item.setData(proposal);
            }
        }
    }
    
    private boolean proposalsChanged(List<CompletionProposal> newProposals) {
        if (proposalTable.getItemCount() != newProposals.size())
            return true;
        
        for (int i = 0; i < newProposals.size(); i++) {
            CompletionProposal proposal = (CompletionProposal) proposalTable.getItem(i).getData();
            if (!newProposals.get(i).equals(proposal))
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
        if (!visible())
            show();
        else {
            selectNext();
        }
    }
    
    public void selectNext() {
        if (proposalTable.getItemCount() == 0)
            return;
        int i = proposalTable.getSelectionIndex();
        i++;
        if (i >= proposalTable.getItemCount())
            i = 0;
        selectAndScroll(i);
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
    
    public void apply() {
        TableItem[] selection = proposalTable.getSelection();
        if (selection.length == 1)
            apply(selection[0], false);
        hide();
    }
    
    private void apply(TableItem tableItem, boolean select) {
        CompletionProposal proposal = (CompletionProposal) tableItem.getData();
        console.useCompletionProposal(proposal, select);
    }
}
