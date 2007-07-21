package com.yoursway.ide.rails.models;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;

import com.yoursway.common.resources.ResourceUtils;
import com.yoursway.ide.common.creation.AbstractLinkedCreationHandler;
import com.yoursway.ide.common.creation.LinkedCreationDescriptor;
import com.yoursway.ide.common.linkedmode.AbstractSingleAreaLinkedMode;
import com.yoursway.ide.ui.rubyeditor.HumaneRubyEditor;
import com.yoursway.rails.core.projects.RailsProject;
import com.yoursway.utils.EditorUtils;
import com.yoursway.utils.RailsNamingConventions;

public class AddRailsModelHandler extends AbstractLinkedCreationHandler {
    
    private static final String FAILURE_MESSAGE = "Model creation failed";
    
    private static final String JOB_NAME = "Creating model";
    
    private static final String BODY = "\nclass YourModel < ActiveRecord::Base\nend\n";
    
    @Override
    protected LinkedCreationDescriptor createLinkedCreationDescriptor(RailsProject activeRailsProject)
            throws ExecutionException {
        final org.eclipse.core.resources.IFolder controllersFolder;
        try {
            controllersFolder = ResourceUtils.lookupOrCreateSubfolder(activeRailsProject.getProject(),
                    RailsNamingConventions.APP_MODELS_PATH);
        } catch (CoreException e) {
            throw new ExecutionException("Cannot create app/models", e);
        }
        final String fileName = EditorUtils.chooseUniqueFileName(controllersFolder, "(new_model", ").rb");
        return new LinkedCreationDescriptor(BODY, JOB_NAME, FAILURE_MESSAGE, controllersFolder, fileName) {
            
            @Override
            public AbstractSingleAreaLinkedMode createLinkedMode(HumaneRubyEditor editor) {
                return new ModelCreationLinkedMode(editor);
            }
            
        };
    }
}
