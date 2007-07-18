package com.yoursway.ide.projects.editor;

import org.eclipse.ui.forms.events.HyperlinkEvent;

public class FormEntryAdapter implements IFormEntryListener {
    
    public FormEntryAdapter() {
        //        this(contextPart, null);
    }
    
    //    public FormEntryAdapter(IContextPart contextPart, IActionBars actionBars) {
    //        this.contextPart = contextPart;
    //        this.actionBars = actionBars;
    //    }
    
    public void focusGained(FormEntry entry) {
        //        ITextSelection selection = new TextSelection(1, 1);
        //        contextPart.getPage().getPDEEditor().getContributor().updateSelectableActions(selection);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.pde.internal.ui.newparts.IFormEntryListener#textDirty(org.eclipse.pde.internal.ui.newparts.FormEntry)
     */
    public void textDirty(FormEntry entry) {
        //        contextPart.fireSaveNeeded();
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.pde.internal.ui.newparts.IFormEntryListener#textValueChanged(org.eclipse.pde.internal.ui.newparts.FormEntry)
     */
    public void textValueChanged(FormEntry entry) {
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.pde.internal.ui.newparts.IFormEntryListener#browseButtonSelected(org.eclipse.pde.internal.ui.newparts.FormEntry)
     */
    public void browseButtonSelected(FormEntry entry) {
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.forms.events.HyperlinkListener#linkEntered(org.eclipse.ui.forms.events.HyperlinkEvent)
     */
    public void linkEntered(HyperlinkEvent e) {
        //        if (actionBars == null)
        //            return;
        //        IStatusLineManager mng = actionBars.getStatusLineManager();
        //        mng.setMessage(e.getLabel());
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.forms.events.HyperlinkListener#linkExited(org.eclipse.ui.forms.events.HyperlinkEvent)
     */
    public void linkExited(HyperlinkEvent e) {
        //        if (actionBars == null)
        //            return;
        //        IStatusLineManager mng = actionBars.getStatusLineManager();
        //        mng.setMessage(null);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.forms.events.HyperlinkListener#linkActivated(org.eclipse.ui.forms.events.HyperlinkEvent)
     */
    public void linkActivated(HyperlinkEvent e) {
    }
    
    public void selectionChanged(FormEntry entry) {
        //        ITextSelection selection = new TextSelection(1, 1);
        //        contextPart.getPage().getPDEEditor().getContributor().updateSelectableActions(selection);
    }
}
