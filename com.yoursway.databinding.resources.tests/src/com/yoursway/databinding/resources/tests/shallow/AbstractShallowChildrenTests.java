package com.yoursway.databinding.resources.tests.shallow;

import static org.junit.Assert.assertArrayEquals;

import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.set.ISetChangeListener;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.junit.After;
import org.junit.Before;

import com.yoursway.databinding.resources.ResourceObservables;
import com.yoursway.tests.commons.AbstractObservableSetTestCase;

public abstract class AbstractShallowChildrenTests extends AbstractObservableSetTestCase<IResource> {
    
    protected IProject project;
    
    @Before
    public void setupProject() throws CoreException {
        project = ResourcesPlugin.getWorkspace().getRoot().getProject("foo");
        create(project);
    }
    
    protected void observe(IContainer folder) {
        observable = ResourceObservables.observeChildren(realm, folder);
    }
    
}
