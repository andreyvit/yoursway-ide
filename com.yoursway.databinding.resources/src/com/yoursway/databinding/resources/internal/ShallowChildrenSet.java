/**
 * 
 */
package com.yoursway.databinding.resources.internal;

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Iterator;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import com.yoursway.common.resources.ResourceUtils;

public final class ShallowChildrenSet extends AbstractSet<IResource> {
    
    private final IContainer container;

    public ShallowChildrenSet(IContainer container) {
        this.container = container;
    }
    
    @Override
    public Iterator<IResource> iterator() {
        try {
            final IResource[] members = container.members();
            System.out.println(members.length + " member(s) of " + container + ":");
            for (IResource member : members)
                System.out.println("  - " + member);
            return Arrays.asList(members).iterator();
        } catch (CoreException e) {
            if (!ResourceUtils.isNotFound(e))
                throw new AssertionError("Error " + e + " was not expected");
            return ShallowChildrenObservable.NO_RESOURCES.iterator();
        }
    }
    
    @Override
    public int size() {
        try {
            return container.members().length;
        } catch (CoreException e) {
            if (!ResourceUtils.isNotFound(e))
                throw new AssertionError("Error " + e + " was not expected");
            return 0;
        }
    }
}