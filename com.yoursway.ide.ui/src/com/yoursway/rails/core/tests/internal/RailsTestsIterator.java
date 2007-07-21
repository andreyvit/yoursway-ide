package com.yoursway.rails.core.tests.internal;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

import com.yoursway.common.resources.ResourceSwitch;
import com.yoursway.ide.ui.Activator;
import com.yoursway.rails.core.projects.RailsProject;
import com.yoursway.utils.RailsFileUtils;
import com.yoursway.utils.RailsNamingConventions;

public class RailsTestsIterator {
    
    private final IRailsTestsRequestor requestor;
    private final RailsProject railsProject;
    
    public RailsTestsIterator(RailsProject railsProject, IRailsTestsRequestor requestor) {
        this.railsProject = railsProject;
        Assert.isLegal(requestor != null);
        this.requestor = requestor;
    }
    
    private void buildFor(IPath path) {
        IResource testFixtures = railsProject.getProject().findMember(path);
        if (testFixtures == null)
            return;
        try {
            testFixtures.accept(new ResourceSwitch() {
                
                @Override
                protected boolean visitFile(IFile resource) {
                    if (isTest(resource))
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
    
    public void build() {
        buildFor(RailsNamingConventions.TEST_UNIT_PATH);
        buildFor(RailsNamingConventions.TEST_FUNCTIONAL_PATH);
    }
    
    private static boolean isTest(IFile file) {
        // TODO: add checks if TestCase class are presented inside
        return RailsFileUtils.isRubyFile(file);
    }
    
}
