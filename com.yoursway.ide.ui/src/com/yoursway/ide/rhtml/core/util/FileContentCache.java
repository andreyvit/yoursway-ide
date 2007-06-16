package com.yoursway.ide.rhtml.core.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.filebuffers.LocationKind;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.wst.sse.core.internal.util.Debug;

import com.yoursway.ide.ui.Activator;

@SuppressWarnings("restriction")
public class FileContentCache {
    private static class CacheEntry {
        String contents;
        long modificationStamp = IResource.NULL_STAMP;
        IPath path;
        
        CacheEntry(IPath path) {
            this.path = path;
            modificationStamp = getModificationStamp(path);
            contents = readContents(path);
        }
        
        private IFile getFile(IPath path) {
            return ResourcesPlugin.getWorkspace().getRoot().getFile(path);
        }
        
        boolean isStale() {
            if (modificationStamp == IResource.NULL_STAMP) {
                return true;
            }
            long newStamp = getModificationStamp(path);
            return newStamp > modificationStamp;
        }
        
        private String detectCharset(IFile file) {
            if (file.getType() == IResource.FILE && file.isAccessible()) {
                IContentDescription d = null;
                try {
                    // optimized description lookup, might not succeed
                    d = file.getContentDescription();
                    if (d != null)
                        return d.getCharset();
                } catch (CoreException e) {
                    // should not be possible given the accessible and file
                    // type
                    // check above
                }
                InputStream contents = null;
                try {
                    contents = file.getContents();
                    IContentDescription description = Platform.getContentTypeManager().getDescriptionFor(
                            contents, file.getName(), new QualifiedName[] { IContentDescription.CHARSET });
                    if (description != null) {
                        return description.getCharset();
                    }
                } catch (IOException e) {
                    // will try to cleanup in finally
                } catch (CoreException e) {
                    Activator.unexpectedError(e);
                } finally {
                    if (contents != null) {
                        try {
                            contents.close();
                        } catch (Exception e) {
                            // not sure how to recover at this point
                        }
                    }
                }
            }
            return ResourcesPlugin.getEncoding();
        }
        
        private long getModificationStamp(IPath filePath) {
            IFile f = getFile(filePath);
            if (f.isAccessible()) {
                return f.getModificationStamp();
            }
            File file = filePath.toFile();
            if (file.exists())
                return file.lastModified();
            return IResource.NULL_STAMP;
        }
        
        private String readContents(IPath filePath) {
            if (DEBUG)
                System.out.println("readContents:" + filePath);
            StringBuffer s = new StringBuffer();
            InputStream is = null;
            try {
                IFile f = getFile(filePath);
                if (f.isAccessible()) {
                    String charset = detectCharset(f);
                    is = f.getContents();
                    Reader reader = new InputStreamReader(is, charset);
                    char[] readBuffer = new char[2048];
                    int n = reader.read(readBuffer);
                    while (n > 0) {
                        s.append(readBuffer, 0, n);
                        n = reader.read(readBuffer);
                    }
                }
            } catch (Exception e) {
                if (Debug.debugStructuredDocument) {
                    Activator.unexpectedError(e);
                    e.printStackTrace();
                }
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (Exception e) {
                    // nothing to do
                }
            }
            if (is == null) {
                try {
                    FileBuffers.getTextFileBufferManager().connect(filePath, LocationKind.LOCATION,
                            new NullProgressMonitor());
                    ITextFileBuffer buffer = FileBuffers.getTextFileBufferManager().getTextFileBuffer(
                            filePath, LocationKind.LOCATION);
                    if (buffer != null) {
                        s.append(buffer.getDocument().get());
                        FileBuffers.getTextFileBufferManager().disconnect(filePath, LocationKind.LOCATION,
                                new NullProgressMonitor());
                    }
                } catch (CoreException e) {
                    // nothing to do
                    e.printStackTrace();
                }
            }
            return s.toString();
        }
        
    }
    
    static final boolean DEBUG = false;
    
    static FileContentCache instance = new FileContentCache();
    
    public static FileContentCache getInstance() {
        return instance;
    }
    
    private final HashMap<IPath, SoftReference<CacheEntry>> fContentMap;
    
    private FileContentCache() {
        super();
        fContentMap = new HashMap<IPath, SoftReference<CacheEntry>>();
    }
    
    private void cleanup() {
        Iterator<Map.Entry<IPath, SoftReference<CacheEntry>>> iterator = fContentMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<IPath, SoftReference<CacheEntry>> entry = iterator.next();
            if (entry.getValue() != null && entry.getValue().get() == null) {
                iterator.remove();
            }
        }
    }
    
    public String getContents(IPath filePath) {
        if (DEBUG)
            System.out.println("getContents:" + filePath);
        SoftReference<CacheEntry> o = fContentMap.get(filePath);
        CacheEntry entry = o.get();
        if (entry == null || entry.isStale()) {
            if (DEBUG && entry != null && entry.isStale())
                System.out.println("stale contents:" + filePath);
            entry = new CacheEntry(filePath);
            fContentMap.put(filePath, new SoftReference<CacheEntry>(entry));
        }
        cleanup();
        return entry.contents;
    }
    
}
