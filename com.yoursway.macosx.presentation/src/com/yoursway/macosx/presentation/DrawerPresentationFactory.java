package com.yoursway.macosx.presentation;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Drawer;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.IPreferenceConstants;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.preferences.IDynamicPropertyMap;
import org.eclipse.ui.internal.presentations.defaultpresentation.DefaultMultiTabListener;
import org.eclipse.ui.internal.presentations.defaultpresentation.DefaultSimpleTabListener;
import org.eclipse.ui.internal.presentations.defaultpresentation.DefaultTabFolder;
import org.eclipse.ui.internal.presentations.defaultpresentation.DefaultThemeListener;
import org.eclipse.ui.internal.presentations.defaultpresentation.EmptyTabFolder;
import org.eclipse.ui.internal.presentations.util.PresentablePartFolder;
import org.eclipse.ui.internal.presentations.util.StandardEditorSystemMenu;
import org.eclipse.ui.internal.presentations.util.StandardViewSystemMenu;
import org.eclipse.ui.internal.presentations.util.TabbedStackPresentation;
import org.eclipse.ui.presentations.AbstractPresentationFactory;
import org.eclipse.ui.presentations.IStackPresentationSite;
import org.eclipse.ui.presentations.StackPresentation;

@SuppressWarnings("restriction")
public class DrawerPresentationFactory extends AbstractPresentationFactory {

    Drawer drawer;
    
    Object foo, bar, boz, fubar;

    // don't reset these dynamically, so just keep the information static.
    // see bug:
    // 75422 [Presentations] Switching presentation to R21 switches immediately,
    // but only partially
    private static int editorTabPosition = WorkbenchPlugin
        .getDefault().getPreferenceStore().getInt(IPreferenceConstants.EDITOR_TAB_POSITION);
    private static int viewTabPosition = WorkbenchPlugin
        .getDefault().getPreferenceStore().getInt(IPreferenceConstants.VIEW_TAB_POSITION);

 
    public StackPresentation createEditorPresentation(Composite parent,
            IStackPresentationSite site) {
        DefaultTabFolder folder = new DefaultTabFolder(parent, editorTabPosition | SWT.BORDER, 
                site.supportsState(IStackPresentationSite.STATE_MINIMIZED), 
                site.supportsState(IStackPresentationSite.STATE_MAXIMIZED));
        
        /*
         * Set the minimum characters to display, if the preference is something
         * other than the default. This is mainly intended for RCP applications
         * or for expert users (i.e., via the plug-in customization file).
         * 
         * Bug 32789.
         */
        final IPreferenceStore store = PlatformUI.getPreferenceStore();
        if (store
                .contains(IWorkbenchPreferenceConstants.EDITOR_MINIMUM_CHARACTERS)) {
            final int minimumCharacters = store
                    .getInt(IWorkbenchPreferenceConstants.EDITOR_MINIMUM_CHARACTERS);
            if (minimumCharacters >= 0) {
                folder.setMinimumCharacters(minimumCharacters);
            }
        }
        
        PresentablePartFolder partFolder = new PresentablePartFolder(folder);
        
        TabbedStackPresentation result = new TabbedStackPresentation(site, partFolder, 
                new StandardEditorSystemMenu(site));
        
        DefaultThemeListener themeListener = new DefaultThemeListener(folder, result.getTheme());
        result.getTheme().addListener(themeListener);
        
        IDynamicPropertyMap workbenchPreferences = result.getPluginPreferences(WorkbenchPlugin.getDefault()); 
        
        new DefaultMultiTabListener(workbenchPreferences,
                IPreferenceConstants.SHOW_MULTIPLE_EDITOR_TABS, folder);

        new DefaultSimpleTabListener(result.getApiPreferences(),
                IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS,
                folder);        
        
        return result;
    }

    public StackPresentation createViewPresentation(final Composite parent,
            IStackPresentationSite site) {
        
        if (drawer == null) {
            drawer = new Drawer(parent.getShell());
            final Runnable openRunnable = new Runnable() {

                public void run() {
                    drawer.open();
//                    drawer.layout();
                }
                
            };  
            parent.getShell().addListener(SWT.Show, new Listener() {

                public void handleEvent(Event event) {
                    drawer.getDisplay().timerExec(1000, openRunnable);
                }
                
            });
            drawer.setLayout(new StackLayout());
            parent.getParent().setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_RED));
        }
        
        Composite composite = new Composite(drawer, SWT.NONE);
        
        DefaultTabFolder folder = new DefaultTabFolder(composite, viewTabPosition
                | SWT.BORDER, site
                .supportsState(IStackPresentationSite.STATE_MINIMIZED), site
                .supportsState(IStackPresentationSite.STATE_MAXIMIZED));

        final IPreferenceStore store = PlatformUI.getPreferenceStore();
        final int minimumCharacters = store
                .getInt(IWorkbenchPreferenceConstants.VIEW_MINIMUM_CHARACTERS);
        if (minimumCharacters >= 0) {
            folder.setMinimumCharacters(minimumCharacters);
        }

        PresentablePartFolder partFolder = new PresentablePartFolder(folder);

        folder.setUnselectedCloseVisible(false);
        folder.setUnselectedImageVisible(true);

        TabbedStackPresentation result = new TabbedStackPresentation(site,
                partFolder, new StandardViewSystemMenu(site));

        DefaultThemeListener themeListener = new DefaultThemeListener(folder,
                result.getTheme());
        result.getTheme().addListener(themeListener);

        new DefaultSimpleTabListener(result.getApiPreferences(),
                IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS,
                folder);

        return result;
    }

    public StackPresentation createStandaloneViewPresentation(Composite parent,
            IStackPresentationSite site, boolean showTitle) {
        
        if (showTitle) {
            return createViewPresentation(parent, site);
        }        
        EmptyTabFolder folder = new EmptyTabFolder(parent, true);
        TabbedStackPresentation presentation = new TabbedStackPresentation(site, folder, new StandardViewSystemMenu(site));
            
        return presentation;
    }
   
}
