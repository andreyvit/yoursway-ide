package com.yoursway.ide.rails.migrations;

import java.text.DecimalFormat;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;

import com.yoursway.common.resources.ResourceUtils;
import com.yoursway.ide.common.creation.AbstractLinkedCreationHandler;
import com.yoursway.ide.common.creation.LinkedCreationDescriptor;
import com.yoursway.ide.common.linkedmode.AbstractSingleAreaLinkedMode;
import com.yoursway.ide.ui.rubyeditor.HumaneRubyEditor;
import com.yoursway.rails.core.migrations.PerProjectRailsMigrationsCollection;
import com.yoursway.rails.core.migrations.RailsMigrationsCollection;
import com.yoursway.rails.core.projects.RailsProject;
import com.yoursway.utils.EditorUtils;
import com.yoursway.utils.RailsNamingConventions;

public class AddRailsMigrationHandler extends AbstractLinkedCreationHandler {
    
    private static final String FAILURE_MESSAGE = "Migration creation failed";
    
    private static final String JOB_NAME = "Creating migration";
    
    private static final String BODY = "\nclass CreateSomethingUseful < ActiveRecord::Migration\n"
            + "  def self.up\n" + "  end\n" + "  def self.down\n" + "  end\n" + "end\n";
    
    @Override
    protected LinkedCreationDescriptor createLinkedCreationDescriptor(RailsProject activeRailsProject)
            throws ExecutionException {
        final org.eclipse.core.resources.IFolder folder;
        try {
            folder = ResourceUtils.lookupOrCreateSubfolder(activeRailsProject.getProject(),
                    RailsNamingConventions.DB_MIGRATIONS_PATH);
        } catch (CoreException e) {
            throw new ExecutionException("Cannot create db/migrate", e);
        }
        PerProjectRailsMigrationsCollection collection = RailsMigrationsCollection.instance().get(
                activeRailsProject);
        final int ordinal = collection.getNextUnusedOrdinal();
        final String fileName = EditorUtils.chooseUniqueFileName(folder, new DecimalFormat("000")
                .format(ordinal)
                + "_(new_migration", ").rb");
        return new LinkedCreationDescriptor(BODY, JOB_NAME, FAILURE_MESSAGE, folder, fileName) {
            
            @Override
            public AbstractSingleAreaLinkedMode createLinkedMode(HumaneRubyEditor editor) {
                return new MigrationCreationLinkedMode(editor, ordinal);
            }
            
        };
    }
}
