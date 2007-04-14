package com.yoursway.rails.model.internal;

import org.eclipse.core.resources.IFile;

import com.yoursway.rails.model.IRailsBaseView;
import com.yoursway.rails.model.IRailsController;
import com.yoursway.rails.model.IRailsProject;
import com.yoursway.utils.PathUtils;

public class RailsBaseView implements IRailsBaseView {
    
    private final IRailsController controller;
    private final IFile file;
    private final boolean isPartial;
    private final String baseName;
    private final Format format;
    
    public RailsBaseView(IRailsController controller, IFile file) {
        this.controller = controller;
        this.file = file;
        String baseName = PathUtils.getBaseNameWithoutExtension(file);
        isPartial = (baseName.startsWith("_"));
        if (isPartial)
            baseName = baseName.substring(1);
        this.baseName = baseName;
        format = determineViewFormat(file);
    }
    
    private static Format determineViewFormat(IFile file) {
        String extension = file.getFileExtension();
        if ("rhtml".equalsIgnoreCase(extension))
            return Format.RHTML;
        if ("rjs".equalsIgnoreCase(extension))
            return Format.RJS;
        if ("rxml".equalsIgnoreCase(extension))
            return Format.RXML;
        if ("ejs".equalsIgnoreCase(extension) || "js".equalsIgnoreCase(extension))
            return Format.EJS;
        return Format.UNKNOWN;
    }
    
    public IRailsController getController() {
        return controller;
    }
    
    public IFile getCorrespondingFile() {
        return file;
    }
    
    public Format getFormat() {
        return format;
    }
    
    public String getName() {
        return baseName;
    }
    
    public IRailsProject getRailsProject() {
        return controller.getRailsProject();
    }
    
}
