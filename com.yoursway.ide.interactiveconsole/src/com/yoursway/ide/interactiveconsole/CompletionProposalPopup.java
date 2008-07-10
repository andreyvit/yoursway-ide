package com.yoursway.ide.interactiveconsole;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class CompletionProposalPopup {
    
    private final Shell shell;
    private final Table proposalTable;
    private final Console console;
    
    public CompletionProposalPopup(Shell parent, final Console console) {
        shell = new Shell(parent, SWT.ON_TOP | SWT.RESIZE);
        proposalTable = new Table(shell, SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
        
        shell.setLayout(new FillLayout());
        shell.setSize(200, 100);
        
        this.console = console;
        
        proposalTable.addSelectionListener(new SelectionListener() {
            
            public void widgetDefaultSelected(SelectionEvent e) {
                apply();
            }
            
            public void widgetSelected(SelectionEvent e) {
                
            }
            
        });
        
        proposalTable.addMouseListener(new MouseListener() {
            
            public void mouseDoubleClick(MouseEvent e) {
                
            }
            
            public void mouseDown(MouseEvent e) {
                
            }
            
            public void mouseUp(MouseEvent e) {
                console.focus();
            }
            
        });
        
    }
    
    public void show() {
        updateProposalList();
        setLocation();
        
        switch (proposalTable.getItemCount()) {
        case 0:
            // need not to show
            //> beep
            break;
        case 1:
            apply(proposalTable.getItem(0), true);
            break;
        default:
            shell.setVisible(true);
        }
    }
    
    public void hide() {
        shell.setVisible(false);
    }
    
    public boolean visible() {
        return shell.getVisible();
    }
    
    public void update() {
        if (visible()) {
            updateProposalList();
            setLocation();
        }
    }
    
    private void updateProposalList() {
        List<CompletionProposal> proposals = console.getCompletionProposals();
        
        if (proposals == null || proposals.size() == 0) {
            proposalTable.removeAll();
            hide();
            //> beep
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
