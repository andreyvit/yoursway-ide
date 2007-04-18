package com.yoursway.ui.popup.trash;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.bindings.keys.IKeyLookup;
import org.eclipse.jface.bindings.keys.KeyLookupFactory;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.HyperlinkGroup;
import org.eclipse.ui.forms.HyperlinkSettings;
import org.eclipse.ui.keys.IBindingService;
import org.eclipse.ui.part.PageBook;

import com.yoursway.ui.popup.IPopupHost;
import com.yoursway.ui.popup.SnapPosition;
import com.yoursway.ui.popup.SnappableYellowPopup;
import com.yoursway.ui.popup.menu.MenuSupport;
import com.yoursway.ui.popup.table.InfoEntry;
import com.yoursway.utils.SWTUtils;

/**
 * A popup that uses a pagebook to display itself in two states, normal and
 * minimized. Probably does not work and is a leftover from the code copied from
 * JDT.
 * 
 * @author Andrey Tarantsov
 */
public class MinimizableTableBasedPopup extends SnappableYellowPopup {
    
    private final MenuSupport menuSupport;
    
    public MinimizableTableBasedPopup(IPopupHost popupOwner,
            Collection<? extends SnapPosition> snapPositions, ParentShellActivation activateParentShell) {
        super(popupOwner, snapPositions, activateParentShell);
        menuSupport = new MenuSupport(this);
    }
    
    private boolean fIsMinimized;
    
    List<InfoEntry> fRefactorEntries;
    private MenuManager menuManager;
    private MenuManager minimizedMenuManager;
    
    @Override
    protected Control createPopupContents(Composite parent) {
        final Display display = parent.getDisplay();
        Color foreground = display.getSystemColor(SWT.COLOR_INFO_FOREGROUND);
        Color background = display.getSystemColor(SWT.COLOR_INFO_BACKGROUND);
        
        final PageBook book = new PageBook(parent, SWT.NONE);
        final Composite table = new Composite(book, SWT.NONE);
        final Composite minimized = new Composite(book, SWT.NONE);
        
        GridLayout tableLayout = new GridLayout(2, false);
        tableLayout.marginHeight = 4;
        tableLayout.marginWidth = 4;
        tableLayout.horizontalSpacing = 10;
        tableLayout.verticalSpacing = 2;
        table.setLayout(tableLayout);
        
        HyperlinkGroup refactorGroup = createContent(display, foreground, table);
        
        addMoveSupport(table);
        
        minimizedMenuManager = menuSupport.createMenuManager();
        ToolBar tableToolBar = menuSupport.addViewMenu(minimized, minimizedMenuManager);
        GridData gridData = new GridData();
        gridData.exclude = true;
        tableToolBar.setLayoutData(gridData);
        Point tableSize = table.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        // having the button overlap the keybinding for refactor is OK, since Ctrl+Enter is always wider than Enter...
        tableToolBar.setLocation(tableSize.x - tableToolBar.getSize().x, 0);
        
        // minimized:
        GridLayout minimizedLayout = new GridLayout(2, false);
        minimizedLayout.marginWidth = minimizedLayout.marginHeight = 0;
        minimizedLayout.horizontalSpacing = 0;
        minimized.setLayout(minimizedLayout);
        
        Composite minimizedBorder = new Composite(minimized, SWT.NONE);
        GridLayout minimizedBorderLayout = new GridLayout(1, false);
        minimizedBorderLayout.marginHeight = tableLayout.marginHeight;
        minimizedBorderLayout.marginWidth = tableLayout.marginWidth;
        minimizedBorder.setLayout(minimizedBorderLayout);
        
        InfoEntry minRefactorEntry = new InfoEntry(minimizedBorder, refactorGroup, "Rename", new Runnable() {
            public void run() {
            }
        }, null, this);
        minRefactorEntry.getLink().setFont(JFaceResources.getFontRegistry().getBold("")); //$NON-NLS-1$ // bold OS font
        fRefactorEntries.add(minRefactorEntry);
        
        addMoveSupport(minimized);
        addMoveSupport(minimizedBorder);
        
        menuManager = menuSupport.createMenuManager();
        menuSupport.addViewMenu(table, menuManager);
        
        SWTUtils.recursiveSetBackgroundColor(book, background);
        book.showPage(fIsMinimized ? minimized : table);
        return table;
    }
    
    private HyperlinkGroup createContent(final Display display, Color foreground, final Composite table) {
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
            }
        },
                KeyStroke.getInstance(KeyLookupFactory.getDefault().formalKeyLookup(IKeyLookup.ESC_NAME))
                        .format(), this);
        return refactorGroup;
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
