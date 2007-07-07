package com.yoursway.ide.projects.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.CompoundContributionItem;

import com.yoursway.ide.projects.YourSwayProjects;
import com.yoursway.ide.projects.mru.MostRecentProjects;

public class OpenRecentDynamicItem extends CompoundContributionItem {
    
    public OpenRecentDynamicItem() {
    }
    
    public OpenRecentDynamicItem(String id) {
        super(id);
    }
    
    @Override
    protected IContributionItem[] getContributionItems() {
        List<IContributionItem> result = new ArrayList<IContributionItem>();
        List<File> recentLocations = computeRecentLocationsToShow(10);
        for (final File location : recentLocations)
            result.add(new ActionContributionItem(new Action("Reopen " + location) {
                @Override
                public void run() {
                    try {
                        YourSwayProjects.openRailsApplication(location);
                    } catch (NoRailsApplicationAtGivenLocation e) {
                        MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                                .getShell(), "Cannot open project", "Rails application at " + location
                                + " no longer exists.");
                    }
                }
            }));
        return result.toArray(new IContributionItem[result.size()]);
    }
    
    private List<File> computeRecentLocationsToShow(int limit) {
        List<File> result = new ArrayList<File>();
        for (File location : MostRecentProjects.instance().getRecentLocations(limit * 2)) {
            IProject project = YourSwayProjects.findProjectByLocation(location);
            if (project == null) {
                result.add(location);
                if (result.size() >= limit)
                    break;
            }
        }
        return result;
    }
}
