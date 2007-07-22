package com.yoursway.rails.core.fixtures.internal;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;

import com.yoursway.common.resources.ResourceSwitch;
import com.yoursway.ide.ui.Activator;
import com.yoursway.rails.commons.RailsFileUtils;
import com.yoursway.rails.commons.RailsNamingConventions;
import com.yoursway.rails.core.projects.RailsProject;

public class RailsFixturesIterator {
    
    private final IRailsFixturesRequestor requestor;
    private final RailsProject railsProject;
    
    public RailsFixturesIterator(RailsProject railsProject, IRailsFixturesRequestor requestor) {
        this.railsProject = railsProject;
        Assert.isLegal(requestor != null);
        this.requestor = requestor;
    }
    
    public void build() {
        IResource testFixtures = railsProject.getProject().findMember(
                RailsNamingConventions.TEST_FIXTURES_PATH);
        if (testFixtures == null)
            return;
        try {
            testFixtures.accept(new ResourceSwitch() {
                
                @Override
                protected boolean visitFile(IFile resource) {
                    if (isFixture(resource))
                        requestor.accept(resource);
                    return false;
                }
                
                @Override
                protected boolean visitFolder(IFolder resource) {
                    return true;
                }
                
            });
        } catch (CoreException e) {
            Activator.unexpectedError(e);
        }
    }
    
    private static boolean isFixture(IFile file) {
        return RailsFileUtils.isYamlFile(file);
    }
    
}
