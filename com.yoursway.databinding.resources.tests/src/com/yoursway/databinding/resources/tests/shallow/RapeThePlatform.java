package com.yoursway.databinding.resources.tests.shallow;

import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.junit.Ignore;
import org.junit.Test;

import com.yoursway.tests.commons.ProjectTests;
import com.yoursway.tests.commons.StateSync;

public final class RapeThePlatform extends ProjectTests {
    
    @Ignore
    @Test 
    public void hardcore() throws CoreException {
        final StateSync<Integer> sync = new StateSync<Integer>(0);
        IResourceChangeListener R = new IResourceChangeListener() {

            public void resourceChanged(IResourceChangeEvent event) {
                sync.setState(3);
                System.out.println("resourceChanged, waiting...");
                sync.waitState(1);
                System.out.println("resourceChanged done.");
            }
            
        };
        ResourcesPlugin.getWorkspace().addResourceChangeListener(R);
        final IProject P = ResourcesPlugin.getWorkspace().getRoot().getProject("fooooo");
        Thread thread = new Thread(new Runnable() {

            public void run() {
                try {
                    System.out.println("Creating project " + P);
                    create(P);
                    sync.setState(2);
                } catch (CoreException e) {
                    e.printStackTrace();
                }
            }
            
        });
        thread.start();
        IProject X = ResourcesPlugin.getWorkspace().getRoot().getProject("baaaaaar");
        sync.waitState(3);
        System.out.println("Creating project " + X);
        try {
            P.getFile("xxx").getLocation().toFile().createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        P.refreshLocal(IResource.DEPTH_INFINITE, null);
        System.out.println("Setting state 1");
        sync.setState(1);
        System.out.println("Waiting for state 2");
        sync.waitState(2);
        System.out.println("Done test.");
    }

}
