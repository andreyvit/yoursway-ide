package com.yoursway.rails.core.migrations.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;

import com.yoursway.ide.ui.Activator;
import com.yoursway.rails.core.projects.RailsProject;
import com.yoursway.utils.RailsNamingConventions;
import com.yoursway.utils.ResourceSwitch;
import com.yoursway.utils.ResourceUtils;

public class RailsMigrationsIterator {
    
    private static final Pattern NAME_PATTERN = Pattern.compile("^(\\d+)_([^.]*)\\.rb$");
    
    private final RailsProject railsProject;
    private final IRailsMigrationsRequestor requestor;
    
    public RailsMigrationsIterator(RailsProject railsProject, IRailsMigrationsRequestor requestor) {
        Assert.isNotNull(railsProject);
        Assert.isNotNull(requestor);
        
        this.railsProject = railsProject;
        this.requestor = requestor;
    }
    
    public void build() {
        IResource folder = railsProject.getProject().findMember(RailsNamingConventions.DB_MIGRATIONS_PATH);
        if (folder == null)
            return;
        try {
            folder.accept(new ResourceSwitch() {
                
                @Override
                protected boolean visitFile(IFile resource) {
                    RailsMigrationInfo info = analyzeFile(resource);
                    if (info != null)
                        requestor.accept(info);
                    return false;
                }
                
                @Override
                protected boolean visitFolder(IFolder resource) {
                    return false;
                }
                
            });
        } catch (CoreException e) {
            if (!ResourceUtils.isNotFoundOrOutOfSync(e))
                Activator.unexpectedError(e);
        }
    }
    
    private RailsMigrationInfo analyzeFile(IFile file) {
        String fileName = file.getName();
        final Matcher matcher = NAME_PATTERN.matcher(fileName);
        if (!matcher.find())
            return null;
        int ordinal = Integer.parseInt(matcher.group(1));
        String pureName = matcher.group(2);
        String expectedClassName = RailsNamingConventions.camelize(pureName);
        return new RailsMigrationInfo(file, ordinal, expectedClassName);
    }
}
