package com.yoursway.ide.undo;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class UndoListViewPart extends ViewPart {
    
    private ListViewer viewer;
    
    public UndoListViewPart() {
        // nothing to do
    }
    
    @Override
    public void createPartControl(Composite parent) {
        viewer = new ListViewer(parent, SWT.SINGLE | SWT.V_SCROLL);
        
        IBaseLabelProvider labelProvider = new ILabelProvider() {
            
            public void addListener(ILabelProviderListener listener) {
                // nothing to do
            }
            
            public void dispose() {
                // nothing to do
            }
            
            public boolean isLabelProperty(Object element, String property) {
                return false;
            }
            
            public void removeListener(ILabelProviderListener listener) {
                // nothing to do                
            }
            
            public Image getImage(Object element) {
                return null;
            }
            
            public String getText(Object element) {
                if (element instanceof IUndoableOperation) {
                    IUndoableOperation operation = (IUndoableOperation) element;
                    return operation.getLabel();
                }
                else return "---"; //!
            }
            
        };
        
        viewer.setLabelProvider(labelProvider);
        // viewer.setInput(OperationHistory.get());
        
        OperationHistory history = OperationHistory.get();
        for (IUndoableOperation operation : history.getUndoHistory()) {
            viewer.add(operation);
        }
        
        history.addListener(new IOperationHistoryListener() {
            
            public void add(IUndoableOperation operation) {
                viewer.add(operation);
            }
            
            public void remove(IUndoableOperation operation) {
                viewer.remove(operation);
            }
            
        });
        
        IToolBarManager toolbarManager = getViewSite().getActionBars().getToolBarManager();
        toolbarManager.add(new Action() {
            
            @Override
            public void run() {
                // MessageDialog.openInformation(null, "proga", "" + list.getSelectionIndex());
                
                OperationHistory history = OperationHistory.get();
                history.undoLastOperation();
            }
        });
    }
    
    @Override
    public void setFocus() {
        // TODO Auto-generated method stub
        
    }
    
}
