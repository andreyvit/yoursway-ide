package com.yoursway.ide.rails.models;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.declarations.TypeDeclaration;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.link.LinkedPosition;

import com.yoursway.common.StringUtils;
import com.yoursway.ide.common.linkedmode.AbstractSingleAreaLinkedMode;
import com.yoursway.ide.ui.rubyeditor.HumaneRubyEditor;
import com.yoursway.ruby.model.RubyFile;
import com.yoursway.utils.RailsNamingConventions;

public class ModelCreationLinkedMode extends AbstractSingleAreaLinkedMode {
    
    public ModelCreationLinkedMode(HumaneRubyEditor editor) {
        super(editor);
    }
    
    @Override
    protected IRegion calculateLinkedRegion() {
        final ISourceModule sourceModule = getEditor().getSourceModule();
        ModuleDeclaration module = RubyFile.parseModule(sourceModule);
        TypeDeclaration[] types = module.getTypes();
        if (types.length == 0)
            return null;
        TypeDeclaration selectedNode = types[0];
        int nameStart = selectedNode.getNameStart();
        int nameEnd = selectedNode.getNameEnd();
        IRegion nameRegion = new Region(nameStart, nameEnd - nameStart);
        return nameRegion;
    }
    
    @Override
    protected IRegion calculateRegionToSelect(final LinkedPosition namePosition, final String nameText) {
        return new Region(namePosition.getOffset(), namePosition.getLength());
    }
    
    @Override
    protected void acceptOperation(final String originalName, String newName) throws ModelException,
            CoreException {
        String className = RailsNamingConventions.joinNamespaces(RailsNamingConventions
                .camelize(RailsNamingConventions.splitPath(newName)));
        
        final String[] nameComponents = RailsNamingConventions.underscore(RailsNamingConventions
                .splitNamespaces(className));
        
        final String fileName = nameComponents[nameComponents.length - 1] + ".rb";
        final ISourceModule sourceModule = getEditor().getSourceModule();
        if (nameComponents.length == 1)
            sourceModule.rename(fileName, false, null);
        else {
            String path = StringUtils.join(nameComponents, "/", 0, nameComponents.length - 1);
            IContainer container = (IContainer) sourceModule.getParent().getResource();
            IFolder folder = container.getFolder(new Path(path));
            if (!folder.exists())
                folder.create(false, true, null);
            IModelElement newParent = DLTKCore.create(folder);
            sourceModule.move(newParent, null, fileName, false, null);
        }
    }
    
}
