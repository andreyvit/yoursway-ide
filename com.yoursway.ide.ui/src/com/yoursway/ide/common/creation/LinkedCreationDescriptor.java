package com.yoursway.ide.common.creation;

import org.eclipse.core.resources.IFolder;

import com.yoursway.ide.common.linkedmode.AbstractSingleAreaLinkedMode;
import com.yoursway.ide.ui.rubyeditor.HumaneRubyEditor;

public abstract class LinkedCreationDescriptor {
    private final String body;
    private final String jobName;
    private final String failureMessage;
    private final IFolder folder;
    private final String fileName;
    
    public LinkedCreationDescriptor(String body, String jobName, String failureMessage, IFolder folder,
            String fileName) {
        this.body = body;
        this.jobName = jobName;
        this.failureMessage = failureMessage;
        this.folder = folder;
        this.fileName = fileName;
    }
    
    public String getBody() {
        return body;
    }
    
    public String getJobName() {
        return jobName;
    }
    
    public String getFailureMessage() {
        return failureMessage;
    }
    
    public IFolder getFolder() {
        return folder;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public abstract AbstractSingleAreaLinkedMode createLinkedMode(HumaneRubyEditor editor);
    
}