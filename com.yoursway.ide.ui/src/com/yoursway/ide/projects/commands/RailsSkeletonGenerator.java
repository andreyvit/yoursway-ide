package com.yoursway.ide.projects.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.osgi.util.NLS;

import com.yoursway.ide.ui.Activator;
import com.yoursway.rails.RailsInstance;
import com.yoursway.rails.Version;
import com.yoursway.ruby.RubyInstance;
import com.yoursway.ruby.RubyToolUtils;
import com.yoursway.ruby.ToolExecutionResult;
import com.yoursway.ruby.RubyInstance.RubyScriptInvokationError;
import com.yoursway.utils.Streams;

public class RailsSkeletonGenerator {
    
    private final static RailsSkeletonGenerator INSTANCE = new RailsSkeletonGenerator();
    private final Storage cache;
    private final File stagingFolder;
    
    private final static String STAGING_APP_NAME = "YOURSWAY_PROJECT_NAME";
    
    private RailsSkeletonGenerator() {
        IPath skelPath = Activator.getDefault().getStateLocation().append("rails-application-skeletons");
        final File skel = skelPath.toFile();
        skel.mkdirs();
        cache = new Storage(skel);
        stagingFolder = Activator.getDefault().getStateLocation().append("rails-application-staging")
                .toFile();
    }
    
    public static RailsSkeletonGenerator getInstance() {
        return INSTANCE;
    }
    
    public class CannotCreateSkeletonException extends Exception {
        
        private static final long serialVersionUID = 1L;
        
        public CannotCreateSkeletonException() {
            super();
        }
        
        public CannotCreateSkeletonException(String message, Throwable cause) {
            super(message, cause);
        }
        
        public CannotCreateSkeletonException(String message) {
            super(message);
        }
        
        public CannotCreateSkeletonException(Throwable cause) {
            super(cause);
        }
        
    }
    
    /**
     * @param folder
     * @param railsInstance
     * @param monitor
     *            the progress monitor to use for reporting progress to the
     *            user. It is the caller's responsibility to call done() on the
     *            given monitor. Accepts <code>null</code>, indicating that
     *            no progress should be reported and that the operation cannot
     *            be cancelled.
     * @throws CannotCreateSkeletonException
     */
    public void putSkeletonInto(File folder, RailsInstance railsInstance, IProgressMonitor monitor)
            throws CannotCreateSkeletonException {
        SubMonitor submonitor = SubMonitor.convert(monitor, 100);
        Entry entry = cache.get(railsInstance.getVersion());
        boolean needToCreate = !entry.exists();
        try {
            if (needToCreate)
                createSkeletonAndStoreIntoCache(railsInstance, entry, submonitor.newChild(80));
            submonitor.setWorkRemaining(20);
            entry.copyInto(folder, new ArrayList<File>());
            submonitor.worked(20);
        } catch (Exception e) {
            throw new CannotCreateSkeletonException(e);
        }
    }
    
    /**
     * @param railsInstance
     * @param entry
     * @param monitor
     *            the progress monitor to use for reporting progress to the
     *            user. It is the caller's responsibility to call done() on the
     *            given monitor. Accepts <code>null</code>, indicating that
     *            no progress should be reported and that the operation cannot
     *            be cancelled.
     * @throws IOException
     * @throws RubyScriptInvokationError
     */
    private void createSkeletonAndStoreIntoCache(RailsInstance railsInstance, Entry entry, IProgressMonitor monitor)
            throws IOException, RubyScriptInvokationError {
        SubMonitor submonitor = SubMonitor.convert(monitor, 100);
        File tempFile = null;
        File tempDir = null;
        try {
            stagingFolder.mkdirs();
            tempFile = File.createTempFile("staging", ".tmp", stagingFolder);
            tempDir = new Path(tempFile.getAbsolutePath()).removeFileExtension().toFile();
            File appDir = new File(tempDir, STAGING_APP_NAME);
            appDir.mkdirs();
            RubyInstance ruby = RubyInstance.adapt(railsInstance.getRawRuby());
            String scriptPath = RubyToolUtils.getScriptCopySuitableForRunning("new_rails_app.rb");
            final ArrayList<String> args = new ArrayList<String>();
            args.add(railsInstance.getRailsGem().getVersion());
            args.add(appDir.getPath());
            ToolExecutionResult result = ruby.runRubyScript(scriptPath, args, submonitor.newChild(80));
            if (result.getExitCode() != 0)
                // TODO: better error handling
                throw new RubyInstance.RubyScriptInvokationError("Non-zero exit code");
            entry.createFromFolder(appDir);
            submonitor.worked(20);
        } finally {
            if (tempFile != null)
                tempFile.delete();
            if (tempDir != null)
                delete(tempDir);
        }
    }
    
    private static class Storage {
        
        private final File path;
        
        public Storage(File path) {
            this.path = path;
        }
        
        public Entry get(Version version) {
            return new Entry(this, versionToCacheItemName(version));
        }
        
        private String versionToCacheItemName(Version version) {
            return version.getComponent(0, "") + "." + version.getComponent(1, "");
        }
        
        public File getPath() {
            return path;
        }
        
    }
    
    private static class Entry {
        
        private final File file;
        
        public Entry(Storage storage, String name) {
            file = new File(storage.getPath(), name + ".zip");
        }
        
        public boolean exists() {
            return file.exists();
        }
        
        public void delete() {
            if (!file.delete())
                // log, but don't make much noise
                Activator.unexpectedError("Cannot delete cache entry " + file);
        }
        
        public void createFromFolder(File sourceFolder) throws IOException {
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(file));
            zipDirectory(sourceFolder, zos, null);
            zos.close();
        }
        
        public void copyInto(File destinationFolder, Collection<File> createdFiles) throws IOException {
            ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                File destination = new File(destinationFolder, entry.getName());
                if (entry.isDirectory()) {
                    destination.mkdirs();
                } else {
                    destination.getParentFile().mkdirs();
                    FileOutputStream os = new FileOutputStream(destination);
                    Streams.copy(zis, os);
                    os.close();
                    createdFiles.add(destination);
                }
            }
            zis.close();
        }
    }
    
    /**
     * Recursively deletes directory and files.
     * 
     * @param file
     * @throws IOException
     */
    private static void delete(File file) throws IOException {
        if (file.isDirectory()) {
            File files[] = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                delete(files[i]);
            }
        }
        if (!file.delete()) {
            throw new IOException(NLS.bind("Cannot delete file {0}", file.getAbsolutePath()));
        }
    }
    
    /**
     * Adds files in a directory to a zip stream
     * 
     * @param dir
     *            directory with files to zip
     * @param zout
     *            ZipOutputStream
     * @param base
     *            directory prefix for file entries inside the zip or null
     * @throws IOException
     */
    private static void zipDirectory(File dir, ZipOutputStream zout, String base) throws IOException {
        String[] files = dir.list();
        if (files == null || files.length == 0)
            return;
        for (int i = 0; i < files.length; i++) {
            String path;
            if (base == null) {
                path = files[i];
            } else {
                path = base + "/" + files[i]; //$NON-NLS-1$
            }
            File f = new File(dir, files[i]);
            if (f.isDirectory())
                zipDirectory(f, zout, path);
            else {
                ZipEntry zentry = new ZipEntry(path);
                zout.putNextEntry(zentry);
                FileInputStream inputStream = new FileInputStream(f);
                Streams.copy(inputStream, zout);
                inputStream.close();
                zout.flush();
                zout.closeEntry();
            }
        }
    }
    
}
