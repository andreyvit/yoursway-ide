package com.yoursway.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.internal.wizards.datatransfer.ArchiveFileManipulations;
import org.eclipse.ui.internal.wizards.datatransfer.DataTransferMessages;
import org.eclipse.ui.internal.wizards.datatransfer.TarException;
import org.eclipse.ui.internal.wizards.datatransfer.TarFile;
import org.eclipse.ui.internal.wizards.datatransfer.TarLeveledStructureProvider;
import org.eclipse.ui.internal.wizards.datatransfer.ZipLeveledStructureProvider;
import org.eclipse.ui.wizards.datatransfer.IImportStructureProvider;

public class ProjectImportUtils {
    public static final String METADATA_FOLDER = ".metadata"; //$NON-NLS-1$
    
    /**
     * Collect the list of .project files that are under directory into files.
     * 
     * @param files
     * @param monitor
     *            The monitor to report to
     * @return boolean <code>true</code> if the operation was completed.
     */
    private boolean collectProjectFilesFromProvider(Collection files, IImportStructureProvider provider,
            Object entry, int level, IProgressMonitor monitor) {
        
        if (monitor.isCanceled()) {
            return false;
        }
        monitor.subTask(NLS.bind(DataTransferMessages.WizardProjectsImportPage_CheckingMessage, provider
                .getLabel(entry)));
        List children = provider.getChildren(entry);
        if (children == null) {
            children = new ArrayList(1);
        }
        Iterator childrenEnum = children.iterator();
        while (childrenEnum.hasNext()) {
            Object child = childrenEnum.next();
            if (provider.isFolder(child)) {
                collectProjectFilesFromProvider(files, provider, child, level + 1, monitor);
            }
            String elementLabel = provider.getLabel(child);
            if (elementLabel.equals(IProjectDescription.DESCRIPTION_FILE_NAME)) {
                files.add(new ProjectRecord(child, entry, level, provider));
            }
        }
        return true;
    }
    
    /**
     * Answer a handle to the zip file currently specified as being the source.
     * Return null if this file does not exist or is not of valid format.
     */
    private ZipFile getSpecifiedZipSourceFile(String fileName) {
        if (fileName.length() == 0) {
            return null;
        }
        
        try {
            return new ZipFile(fileName);
        } catch (ZipException e) {
            //displayErrorDialog(DataTransferMessages.ZipImport_badFormat);
        } catch (IOException e) {
            //displayErrorDialog(DataTransferMessages.ZipImport_couldNotRead);
        }
        
        //archivePathField.setFocus();
        return null;
    }
    
    /**
     * Answer a handle to the zip file currently specified as being the source.
     * Return null if this file does not exist or is not of valid format.
     */
    private TarFile getSpecifiedTarSourceFile(String fileName) {
        if (fileName.length() == 0) {
            return null;
        }
        
        try {
            return new TarFile(fileName);
        } catch (TarException e) {
            //displayErrorDialog(DataTransferMessages.TarImport_badFormat);
        } catch (IOException e) {
            //displayErrorDialog(DataTransferMessages.ZipImport_couldNotRead);
        }
        //archivePathField.setFocus();
        return null;
    }
    
    /**
     * Collect the list of .project files that are under directory into files.
     * 
     * @param files
     * @param directory
     * @param monitor
     *            The monitor to report to
     * @return boolean <code>true</code> if the operation was completed.
     */
    private boolean collectProjectFilesFromDirectory(Collection files, File directory,
            IProgressMonitor monitor) {
        
        if (monitor.isCanceled()) {
            return false;
        }
        monitor.subTask(NLS.bind(DataTransferMessages.WizardProjectsImportPage_CheckingMessage, directory
                .getPath()));
        File[] contents = directory.listFiles();
        // first look for project description files
        final String dotProject = IProjectDescription.DESCRIPTION_FILE_NAME;
        for (int i = 0; i < contents.length; i++) {
            File file = contents[i];
            if (file.isFile() && file.getName().equals(dotProject)) {
                files.add(file);
                // don't search sub-directories since we can't have nested
                // projects
                return true;
            }
        }
        // no project description found, so recurse into sub-directories
        for (int i = 0; i < contents.length; i++) {
            if (contents[i].isDirectory()) {
                if (!contents[i].getName().equals(METADATA_FOLDER)) {
                    collectProjectFilesFromDirectory(files, contents[i], monitor);
                }
            }
        }
        return true;
    }
    
    public void runCollectProjects(Shell shell, IProgressMonitor monitor, String path) {
        
        monitor.beginTask(DataTransferMessages.WizardProjectsImportPage_SearchingMessage, 100);
        File directory = new File(path);
        ProjectRecord[] selectedProjects = new ProjectRecord[0];
        Collection files = new ArrayList();
        monitor.worked(10);
        boolean dirSelected = directory.isDirectory();
        if (!dirSelected && ArchiveFileManipulations.isTarFile(path)) {
            TarFile sourceTarFile = getSpecifiedTarSourceFile(path);
            if (sourceTarFile == null) {
                return;
            }
            
            TarLeveledStructureProvider provider = ArchiveFileManipulations.getTarStructureProvider(
                    sourceTarFile, shell);
            Object child = provider.getRoot();
            
            if (!collectProjectFilesFromProvider(files, provider, child, 0, monitor)) {
                return;
            }
            Iterator filesIterator = files.iterator();
            selectedProjects = new ProjectRecord[files.size()];
            int index = 0;
            monitor.worked(50);
            monitor.subTask(DataTransferMessages.WizardProjectsImportPage_ProcessingMessage);
            while (filesIterator.hasNext()) {
                selectedProjects[index++] = (ProjectRecord) filesIterator.next();
            }
        } else if (!dirSelected && ArchiveFileManipulations.isZipFile(path)) {
            ZipFile sourceFile = getSpecifiedZipSourceFile(path);
            if (sourceFile == null) {
                return;
            }
            ZipLeveledStructureProvider provider = ArchiveFileManipulations.getZipStructureProvider(
                    sourceFile, shell);
            Object child = provider.getRoot();
            
            if (!collectProjectFilesFromProvider(files, provider, child, 0, monitor)) {
                return;
            }
            Iterator filesIterator = files.iterator();
            selectedProjects = new ProjectRecord[files.size()];
            int index = 0;
            monitor.worked(50);
            monitor.subTask(DataTransferMessages.WizardProjectsImportPage_ProcessingMessage);
            while (filesIterator.hasNext()) {
                selectedProjects[index++] = (ProjectRecord) filesIterator.next();
            }
        }

        else if (dirSelected && directory.isDirectory()) {
            
            if (!collectProjectFilesFromDirectory(files, directory, monitor)) {
                return;
            }
            Iterator filesIterator = files.iterator();
            selectedProjects = new ProjectRecord[files.size()];
            int index = 0;
            monitor.worked(50);
            monitor.subTask(DataTransferMessages.WizardProjectsImportPage_ProcessingMessage);
            while (filesIterator.hasNext()) {
                File file = (File) filesIterator.next();
                selectedProjects[index] = new ProjectRecord(file);
                index++;
            }
        } else {
            monitor.worked(60);
        }
        monitor.done();
    }
    
}
