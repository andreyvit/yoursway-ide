package com.yoursway.ui.popup.trash;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.bindings.keys.IKeyLookup;
import org.eclipse.jface.bindings.keys.KeyLookupFactory;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.HyperlinkGroup;
import org.eclipse.ui.forms.HyperlinkSettings;
import org.eclipse.ui.keys.IBindingService;

import com.yoursway.ui.popup.IPopupHost;
import com.yoursway.ui.popup.SnapPosition;
import com.yoursway.ui.popup.table.InfoEntry;
import com.yoursway.ui.popup.table.TableBasedPopup;

/**
 * A sample popup that resembles the one JDT displays for in-place rename
 * refactoring.
 * 
 * @author Andrey Tarantsov
 */
public class RenamePopup extends TableBasedPopup {
    
    public RenamePopup(IPopupHost popupOwner, Collection<? extends SnapPosition> snapPositions) {
        super(popupOwner, snapPositions, ParentShellActivation.ACTIVATE_PARENT_WHEN_POSSIBLE);
    }
    
    private List<InfoEntry> fRefactorEntries;
    
    @Override
    protected void createContent(final Display display, Color foreground, final Composite table) {
        HyperlinkGroup refactorGroup = new HyperlinkGroup(display);
        refactorGroup.setForeground(foreground);
        refactorGroup.setHyperlinkUnderlineMode(HyperlinkSettings.UNDERLINE_HOVER);
        fRefactorEntries = new ArrayList<InfoEntry>();
        
        InfoEntry refactorEntry = new InfoEntry(table, refactorGroup, "Rename", new Runnable() {
            public void run() {
            }
        }, KeyStroke.getInstance(KeyLookupFactory.getDefault().formalKeyLookup(IKeyLookup.CR_NAME)).format(),
                this);
        refactorEntry.getLink().setFont(JFaceResources.getFontRegistry().getBold("")); //$NON-NLS-1$ // bold OS font
        fRefactorEntries.add(refactorEntry);
        
        InfoEntry previewEntry = new InfoEntry(table, refactorGroup, "Preview...", new Runnable() {
            public void run() {
            }
        }, KeyStroke.getInstance(SWT.CTRL, KeyLookupFactory.getDefault().formalKeyLookup(IKeyLookup.CR_NAME))
                .format(), this);
        fRefactorEntries.add(previewEntry);
        
        new InfoEntry(table, refactorGroup, "Open Dialog", new Runnable() {
            public void run() {
            }
        }, getOpenDialogBinding(), this);
        
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
    
    private static String getOpenDialogBinding() {
        IBindingService bindingService = (IBindingService) PlatformUI.getWorkbench().getAdapter(
                IBindingService.class);
        if (bindingService == null)
            return ""; //$NON-NLS-1$
        // TODO [AT]: really get bindings
        //        String binding = bindingService
        //                .getBestActiveBindingFormattedFor(IJavaEditorActionDefinitionIds.RENAME_ELEMENT);
        //        return binding == null ? "" : binding; //$NON-NLS-1$
        return "C-M-x ;)";
    }
    
}
