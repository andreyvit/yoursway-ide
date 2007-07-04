package com.yoursway.ruby.internal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

import com.yoursway.ide.ui.Activator;
import com.yoursway.utils.EnvTest;

public class PossibleRubyLocationsIterator {
    
    private final IPossibleRubyLocationsRequestor requestor;
    
    private static class SearchRoot {
        
        private final File path;
        private final int recursionDepth;
        
        public SearchRoot(File path, int recursionDepth) {
            this.path = path;
            this.recursionDepth = recursionDepth;
        }
        
        public File getPath() {
            return path;
        }
        
        public int getRecursionDepth() {
            return recursionDepth;
        }
        
        public int amountOfWork() {
            return 10 + 20 * recursionDepth * recursionDepth;
        }
        
        @Override
        public String toString() {
            return path.toString();
        }
        
    }
    
    private interface IRubyRequestor {
        
        void rubyFound(File pathToExecutable);
        
        void addSearchPath(SearchRoot path);
        
    }
    
    private static abstract class AbstractFolderAnalyzer {
        
        public abstract void analyzeFolder(File canonicalParent, Collection<File> files,
                Collection<File> subfolders, IRubyRequestor requestor, IProgressMonitor monitor);
        
    }
    
    private static class RubyFolderAnalyzer extends AbstractFolderAnalyzer {
        
        private final String executableName;
        
        public RubyFolderAnalyzer() {
            executableName = (EnvTest.isWindowsOS() ? "ruby.exe" : "ruby");
        }
        
        @Override
        public void analyzeFolder(File canonicalParent, Collection<File> files, Collection<File> subfolders,
                IRubyRequestor requestor, IProgressMonitor monitor) {
            SubMonitor progress = SubMonitor.convert(monitor, files.size());
            for (File file : files) {
                if (executableName.equalsIgnoreCase(file.getName()))
                    requestor.rubyFound(file);
                progress.worked(1);
            }
        }
        
    }
    
    private static class InstantRailsAnalyzer extends AbstractFolderAnalyzer {
        
        @Override
        public void analyzeFolder(File canonicalParent, Collection<File> files, Collection<File> subfolders,
                IRubyRequestor requestor, IProgressMonitor monitor) {
            SubMonitor progress = SubMonitor.convert(monitor, files.size());
            for (File file : files) {
                if ("instantrails.exe".equalsIgnoreCase(file.getName())) {
                    File possibleRuby = new File(canonicalParent, "ruby\\bin\\ruby.exe");
                    if (possibleRuby.isFile())
                        requestor.rubyFound(file);
                }
                progress.worked(1);
            }
        }
        
    }
    
    private static class RubyDiscoveryProcessor implements IRubyRequestor {
        
        private final LinkedList<SearchRoot> searchRoots;
        private final Collection<AbstractFolderAnalyzer> analyzers;
        private final Set<File> visitedFolders = new HashSet<File>();
        private int totalWork;
        private int doneWork;
        
        private final IPossibleRubyLocationsRequestor requestor;
        
        public RubyDiscoveryProcessor(Collection<SearchRoot> searchRoots,
                Collection<AbstractFolderAnalyzer> analyzers, IPossibleRubyLocationsRequestor requestor) {
            this.requestor = requestor;
            this.searchRoots = new LinkedList<SearchRoot>(searchRoots);
            this.analyzers = new ArrayList<AbstractFolderAnalyzer>(analyzers);
            totalWork = calculateTotalWork(searchRoots);
        }
        
        public void execute(IProgressMonitor monitor) {
            SubMonitor progress = SubMonitor.convert(monitor);
            while (!searchRoots.isEmpty()) {
                progress.setWorkRemaining(totalWork - doneWork);
                try {
                    final SearchRoot root = searchRoots.removeFirst();
                    final File canonicalLocation = root.getPath().getCanonicalFile();
                    analyzeFolder(canonicalLocation, root.getRecursionDepth(), progress.newChild(root
                            .amountOfWork()));
                    doneWork += root.amountOfWork();
                } catch (IOException e) {
                    Activator.unexpectedError(e);
                }
            }
        }
        
        private void analyzeFolder(File canonicalParent, int recursionDepth, IProgressMonitor monitor) {
            SubMonitor progress = SubMonitor.convert(monitor, 10);
            if (visitedFolders.contains(canonicalParent))
                return;
            visitedFolders.add(canonicalParent);
            
            File[] children = canonicalParent.listFiles();
            if (children == null) {
                System.out.println();
                return;
            }
            // 1/10th of total work is fetching a list of children
            progress.worked(1);
            progress.setWorkRemaining(10 * children.length);
            Collection<File> files = new ArrayList<File>(children.length);
            Collection<File> subfolders = new ArrayList<File>(children.length);
            // 1/10th of the remainder is classification
            classifyChildren(children, files, subfolders, progress.newChild(children.length));
            
            final int folderWeight = 10 * recursionDepth * recursionDepth;
            progress.setWorkRemaining(folderWeight * subfolders.size() + files.size());
            analyzeWithAnalyzers(canonicalParent, children, files, subfolders, progress
                    .newChild(files.size()));
            if (recursionDepth > 0)
                for (File subfolder : subfolders)
                    analyzeFolder(subfolder, recursionDepth - 1, progress.newChild(folderWeight));
        }
        
        private void analyzeWithAnalyzers(File canonicalParent, File[] children, Collection<File> files,
                Collection<File> subfolders, IProgressMonitor monitor) {
            SubMonitor progress = SubMonitor.convert(monitor, analyzers.size());
            for (AbstractFolderAnalyzer analyzer : analyzers)
                analyzer.analyzeFolder(canonicalParent, files, subfolders, this, progress.newChild(1));
        }
        
        private void classifyChildren(File[] children, Collection<File> files, Collection<File> subfolders,
                IProgressMonitor monitor) {
            SubMonitor progress = SubMonitor.convert(monitor, children.length);
            for (File child : children) {
                try {
                    File canonicalChild = child.getCanonicalFile();
                    if (canonicalChild.isDirectory())
                        subfolders.add(canonicalChild);
                    else if (canonicalChild.isFile())
                        files.add(canonicalChild);
                    else
                        Activator.unexpectedError("Unknown kind of file: " + canonicalChild);
                } catch (IOException e) {
                    Activator.unexpectedError(e);
                    // ignore otherwise
                }
                progress.worked(1);
            }
        }
        
        public void addSearchPath(SearchRoot root) {
            searchRoots.add(root);
            totalWork += root.amountOfWork();
        }
        
        public void rubyFound(File pathToExecutable) {
            System.out.println("Found possible Ruby at " + pathToExecutable);
            requestor.accept(pathToExecutable);
        }
        
    }
    
    public PossibleRubyLocationsIterator(IPossibleRubyLocationsRequestor requestor) {
        this.requestor = requestor;
    }
    
    public void build(IProgressMonitor monitor) {
        Collection<SearchRoot> searchRoots = collectSearchRoots();
        try {
            Collection<AbstractFolderAnalyzer> analyzers = collectAnalyzers();
            RubyDiscoveryProcessor processor = new RubyDiscoveryProcessor(searchRoots, analyzers, requestor);
            processor.execute(monitor);
        } finally {
            if (monitor != null)
                monitor.done();
        }
    }
    
    private Collection<AbstractFolderAnalyzer> collectAnalyzers() {
        Collection<AbstractFolderAnalyzer> analyzers = new ArrayList<AbstractFolderAnalyzer>();
        analyzers.add(new RubyFolderAnalyzer());
        if (EnvTest.isWindowsOS())
            analyzers.add(new InstantRailsAnalyzer());
        return analyzers;
    }
    
    static int calculateTotalWork(Collection<SearchRoot> paths) {
        int totalWork = 0;
        for (SearchRoot path : paths)
            totalWork += path.amountOfWork();
        return totalWork;
    }
    
    private Collection<SearchRoot> collectSearchRoots() {
        Collection<SearchRoot> paths = new ArrayList<SearchRoot>();
        addFoldersFromPathEnvironmentVariable(paths);
        if (EnvTest.isPosix()) {
            String[] hardCodedRoots = new String[] { "/bin", "/usr/bin", "/usr/local/bin", "/opt/bin",
                    "/opt/local/bin", "/sw/bin" };
            for (String root : hardCodedRoots)
                paths.add(new SearchRoot(new File(root), 0));
            paths.add(new SearchRoot(new File(System.getProperty("user.home")), 1));
        }
        if (EnvTest.isWindowsOS()) {
            // TODO: maybe look on every non-removable drive?
            String systemDrive = System.getenv("SystemDrive");
            if (systemDrive == null)
                systemDrive = "C:"; // Win9x?
            paths.add(new SearchRoot(new File(systemDrive), 2));
        }
        return paths;
    }
    
    private void addFoldersFromPathEnvironmentVariable(Collection<SearchRoot> paths) {
        String searchPath = System.getenv("PATH");
        StringTokenizer tokenizer = new StringTokenizer(searchPath, File.pathSeparator);
        while (tokenizer.hasMoreTokens()) {
            String pathString = tokenizer.nextToken().trim();
            if (pathString.startsWith("\""))
                pathString = pathString.substring(1);
            if (pathString.endsWith("\""))
                pathString = pathString.substring(0, pathString.length() - 1);
            pathString = pathString.trim();
            if (pathString.length() == 0)
                continue;
            File dir = new File(pathString);
            if (dir.isDirectory())
                paths.add(new SearchRoot(dir, 0));
        }
    }
}
