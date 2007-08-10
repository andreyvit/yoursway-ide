package com.yoursway.ide.projects.editor.aux;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IMessageManager;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;

public abstract class ManagedFormSection extends SectionPart {
    
    private final IMessageManager messageManager;
    
    public ManagedFormSection(Composite parent, FormToolkit toolkit, int style,
            IMessageManager messageManager, boolean titleBar) {
        super(parent, toolkit, titleBar ? (ExpandableComposite.TITLE_BAR | style) : style);
        this.messageManager = messageManager;
        getSection().clientVerticalSpacing = FormLayoutFactory.SECTION_HEADER_VERTICAL_SPACING;
    }
    
    public IMessageManager getMessageManager() {
        return messageManager;
    }
    
}
