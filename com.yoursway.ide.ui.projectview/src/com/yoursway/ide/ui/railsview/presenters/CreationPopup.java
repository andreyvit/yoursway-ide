package com.yoursway.ide.ui.railsview.presenters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.bindings.keys.IKeyLookup;
import org.eclipse.jface.bindings.keys.KeyLookupFactory;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class CreationPopup extends TableBasedPopup {
    
    public CreationPopup(IPopupHost popupOwner, Collection<? extends SnapPosition> snapPositions) {
        super(popupOwner, snapPositions, ParentShellActivation.ACTIVATE_PARENT_WHEN_POSSIBLE);
    }
    
    private List<InfoEntry> fRefactorEntries;
    
    @Override
    protected void createContent(final Display display, Color foreground, final Composite table) {
        HyperlinkGroup refactorGroup = new HyperlinkGroup(display);
        refactorGroup.setForeground(foreground);
        refactorGroup.setHyperlinkUnderlineMode(HyperlinkSettings.UNDERLINE_HOVER);
        fRefactorEntries = new ArrayList<InfoEntry>();
        
        InfoEntry refactorEntry = new InfoEntry(table, refactorGroup, "Create", new Runnable() {
            public void run() {
            }
        }, KeyStroke.getInstance(KeyLookupFactory.getDefault().formalKeyLookup(IKeyLookup.CR_NAME)).format(),
                this);
        refactorEntry.getLink().setFont(JFaceResources.getFontRegistry().getBold("")); //$NON-NLS-1$ // bold OS font
        fRefactorEntries.add(refactorEntry);
        
        HyperlinkGroup cancelGroup = new HyperlinkGroup(display);
        cancelGroup.setForeground(foreground);
        cancelGroup.setHyperlinkUnderlineMode(HyperlinkSettings.UNDERLINE_HOVER);
        
        new InfoEntry(table, cancelGroup, "Cancel", new Runnable() {
            public void run() {
                close();
            }
        },
                KeyStroke.getInstance(KeyLookupFactory.getDefault().formalKeyLookup(IKeyLookup.ESC_NAME))
                        .format(), this);
    }
    
}
