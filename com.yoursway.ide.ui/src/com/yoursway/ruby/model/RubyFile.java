package com.yoursway.ruby.model;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.core.IModelStatusConstants;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ISourceModuleInfoCache;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.core.ISourceModuleInfoCache.ISourceModuleInfo;
import org.eclipse.dltk.internal.core.ModelManager;
import org.eclipse.dltk.ruby.internal.parser.RubySourceElementParser;

import com.yoursway.ide.ui.Activator;
import com.yoursway.utils.Streams;

public class RubyFile {
    
    private final ISourceModule sourceModule;
    
    public RubyFile(ISourceModule sourceModule) {
        this.sourceModule = sourceModule;
    }
    
    public ISourceModule getSourceModule() {
        return sourceModule;
    }
    
    public ModuleDeclaration getAST() {
        ISourceModuleInfoCache cache = ModelManager.getModelManager().getSourceModuleInfoCache();
        ISourceModuleInfo info = cache.get(sourceModule);
        if (info != null) {
            ModuleDeclaration ast = (ModuleDeclaration) info.get(RubySourceElementParser.AST);
            if (ast != null)
                return ast;
        }
        // oops, no cached AST
        return parseModule(sourceModule);
    }
    
    public static ModuleDeclaration parseModule(ISourceModule module) {
        ISourceModuleInfoCache sourceModuleInfoCache = ModelManager.getModelManager()
                .getSourceModuleInfoCache();
        char[] source;
        try {
            source = module.getSourceAsCharArray();
        } catch (ModelException e) {
            if (e.getStatus() != null
                    && e.getStatus().getCode() == IModelStatusConstants.ELEMENT_NOT_ON_BUILDPATH) {
                // TODO: megafix
                IFile file;
                file = (IFile) module.getResource();
                try {
                    String content = Streams.readToString(file.getContents());
                    source = content.toCharArray();
                } catch (IOException e1) {
                    Activator.log(e1);
                    return null;
                } catch (CoreException e1) {
                    Activator.log(e1);
                    return null;
                }
            } else {
                Activator.log(e);
                return null;
            }
        }
        return RubySourceElementParser.parseModule(sourceModuleInfoCache.get(module), source, null, null);
    }
    
}
