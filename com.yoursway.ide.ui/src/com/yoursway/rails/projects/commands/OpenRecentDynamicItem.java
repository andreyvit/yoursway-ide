package com.yoursway.rails.projects.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.actions.CompoundContributionItem;

import com.yoursway.ide.ui.Activator;
import com.yoursway.utils.ProjectUtils;

public class OpenRecentDynamicItem extends CompoundContributionItem {
    
    public OpenRecentDynamicItem() {
    }
    
    public OpenRecentDynamicItem(String id) {
        super(id);
    }
    
    @Override
    protected IContributionItem[] getContributionItems() {
        List<IContributionItem> result = new ArrayList<IContributionItem>();
        for (final IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects())
            result.add(new ActionContributionItem(new Action("Open " + project.getName()) {
                @Override
                public void run() {
                    try {
                        ProjectUtils.openProjectInNewWindow(project);
                    } catch (WorkbenchException e) {
                        Activator.unexpectedError(e);
                    }
                }
            }));
        return result.toArray(new IContributionItem[result.size()]);
    }
    
}
