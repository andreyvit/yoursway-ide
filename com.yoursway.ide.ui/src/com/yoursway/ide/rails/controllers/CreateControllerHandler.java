package com.yoursway.ide.rails.controllers;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;

import com.yoursway.ide.common.creation.AbstractLinkedCreationHandler;
import com.yoursway.ide.common.creation.LinkedCreationDescriptor;
import com.yoursway.ide.common.linkedmode.AbstractSingleAreaLinkedMode;
import com.yoursway.ide.ui.rubyeditor.HumaneRubyEditor;
import com.yoursway.rails.models.project.RailsProject;
import com.yoursway.utils.EditorUtils;
import com.yoursway.utils.RailsNamingConventions;
import com.yoursway.utils.ResourceUtils;

public class CreateControllerHandler extends AbstractLinkedCreationHandler {
    
    private static final String FAILURE_MESSAGE = "Controller creation failed";
    
    private static final String JOB_NAME = "Creating controller";
    
    private static final String BODY = "\nclass YourCoolController < ApplicationController\nend\n";
    
    @Override
    protected LinkedCreationDescriptor createLinkedCreationDescriptor(RailsProject activeRailsProject)
            throws ExecutionException {
        final org.eclipse.core.resources.IFolder controllersFolder;
        try {
            controllersFolder = ResourceUtils.lookupOrCreateSubfolder(activeRailsProject.getProject(),
                    RailsNamingConventions.APP_CONTROLLERS_PATH);
        } catch (CoreException e) {
            throw new ExecutionException("Cannot create app/controllers", e);
        }
        final String fileName = EditorUtils
                .chooseUniqueFileName(controllersFolder, "(new_controller", ").rb");
        return new LinkedCreationDescriptor(BODY, JOB_NAME, FAILURE_MESSAGE, controllersFolder, fileName) {
            
            @Override
            public AbstractSingleAreaLinkedMode createLinkedMode(HumaneRubyEditor editor) {
                return new ControllerCreationLinkedMode(editor);
            }
            
        };
    }
}
