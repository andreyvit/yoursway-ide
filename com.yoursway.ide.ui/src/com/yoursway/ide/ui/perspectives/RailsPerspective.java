/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.yoursway.ide.ui.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IPlaceholderFolderLayout;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.progress.IProgressConstants;

import com.yoursway.ide.ui.advisor.AdvisorView;
import com.yoursway.ide.ui.railsview.RailsView;

/**
 * Defines the layout of the primary perspective for Rails development.
 * 
 * @author Andrey Tarantsov
 */
public class RailsPerspective implements IPerspectiveFactory {
    
    public static final String ID = "com.yoursway.ide.ui.perspectives.RailsPerspective";
    
    private static final String RIGHT_BOTTOM_FOLDER = "rightBottom";
    private static final String RIGHT_FOLDER = "right";
    private static final String BOTTOM_FOLDER = "bottom";
    private IPageLayout factory;
    
    public RailsPerspective() {
        super();
    }
    
    public void createInitialLayout(IPageLayout factory) {
        this.factory = factory;
        addViews();
        addActionSets();
        addNewWizardShortcuts();
        addPerspectiveShortcuts();
        addViewShortcuts();
    }
    
    private void addViews() {
        IPlaceholderFolderLayout bottom = factory.createPlaceholderFolder(BOTTOM_FOLDER, IPageLayout.BOTTOM,
                0.75f, factory.getEditorArea());
        bottom.addPlaceholder(IPageLayout.ID_PROBLEM_VIEW);
        bottom.addPlaceholder(ThirdPartyInterfaceComponentsConstants.GENERIC_HISTORY_VIEW);
        bottom.addPlaceholder(IPageLayout.ID_PROBLEM_VIEW);
        bottom.addPlaceholder(IPageLayout.ID_TASK_LIST);
        bottom.addPlaceholder(ThirdPartyInterfaceComponentsConstants.RUBY_DOCUMENTATION_VIEW);
        bottom.addPlaceholder(IPageLayout.ID_BOOKMARKS);
        bottom.addPlaceholder(IProgressConstants.PROGRESS_VIEW_ID);
        bottom.addPlaceholder(IConsoleConstants.ID_CONSOLE_VIEW);
        
        IFolderLayout right = factory.createFolder(RIGHT_FOLDER, IPageLayout.RIGHT, 0.7f, factory
                .getEditorArea());
        right.addView(RailsView.ID);
        right.addView(ThirdPartyInterfaceComponentsConstants.DLTK_SCRIPT_EXPLORER_VIEW);
        right.addPlaceholder(IPageLayout.ID_OUTLINE);
        right.addPlaceholder("com.yoursway.introspection.dltk.Model");
        right.addPlaceholder("com.yoursway.introspection.rails.Model");
        right.addPlaceholder("com.yoursway.introspection.resources.Model");
        
        IPlaceholderFolderLayout rightBottom = factory.createPlaceholderFolder(RIGHT_BOTTOM_FOLDER,
                IPageLayout.BOTTOM, 0.7f, RIGHT_FOLDER);
        rightBottom.addPlaceholder(AdvisorView.ID);
        
        // factory.addFastView(CVS_REPOSITORIES_VIEW, 0.50f);
        // factory.addFastView(TEAM_SYNCHRONIZE_VIEW, 0.50f);
    }
    
    private void addActionSets() {
        // factory.addActionSet(ThirdPartyInterfaceComponentsConstants.LAUNCH_ACTION_SET);
        // factory.addActionSet(ThirdPartyInterfaceComponentsConstants.DEBUG_ACTION_SET);
        // factory.addActionSet(ThirdPartyInterfaceComponentsConstants.PROFILE_ACTION_SET);
        // factory.addActionSet(ThirdPartyInterfaceComponentsConstants.TEAM_UI_ACTION_SET);
        // factory.addActionSet(ThirdPartyInterfaceComponentsConstants.CVS_UI_ACTION_SET);
        // factory.addActionSet(IPageLayout.ID_NAVIGATE_ACTION_SET);
    }
    
    private void addPerspectiveShortcuts() {
        factory.addPerspectiveShortcut(ThirdPartyInterfaceComponentsConstants.TEAM_SYNCHRONIZING_PERSPECTIVE);
        factory.addPerspectiveShortcut(ThirdPartyInterfaceComponentsConstants.CVS_PERSPECTIVE);
        factory.addPerspectiveShortcut(ThirdPartyInterfaceComponentsConstants.RESOURCE_PERSPECTIVE);
    }
    
    private void addNewWizardShortcuts() {
        factory.addNewWizardShortcut(ThirdPartyInterfaceComponentsConstants.NEW_PROJECT_CHECKOUT_WIZARD);
        factory.addNewWizardShortcut(ThirdPartyInterfaceComponentsConstants.RUBY_NEW_PROJECT_WIZARD);
        factory.addNewWizardShortcut(ThirdPartyInterfaceComponentsConstants.RUBY_NEW_FILE_WIZARD);
        factory.addNewWizardShortcut(ThirdPartyInterfaceComponentsConstants.NEW_FOLDER_WIZARD);
        factory.addNewWizardShortcut(ThirdPartyInterfaceComponentsConstants.NEW_FILE_WIZARD);
        factory.addNewWizardShortcut(ThirdPartyInterfaceComponentsConstants.NEW_UNTITLED_TEXT_FILE_WIZARD);
    }
    
    private void addViewShortcuts() {
        factory.addShowViewShortcut(ThirdPartyInterfaceComponentsConstants.ANT_VIEW);
        factory.addShowViewShortcut(ThirdPartyInterfaceComponentsConstants.CVS_ANNOTATE_VIEW);
        factory.addShowViewShortcut(ThirdPartyInterfaceComponentsConstants.GENERIC_HISTORY_VIEW);
        factory.addShowViewShortcut(IConsoleConstants.ID_CONSOLE_VIEW);
        factory.addShowViewShortcut(IPageLayout.ID_RES_NAV);
        factory.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW);
        factory.addShowViewShortcut(IPageLayout.ID_OUTLINE);
        factory.addShowViewShortcut(IPageLayout.ID_TASK_LIST);
        factory.addShowViewShortcut(IProgressConstants.PROGRESS_VIEW_ID);
    }
    
}
