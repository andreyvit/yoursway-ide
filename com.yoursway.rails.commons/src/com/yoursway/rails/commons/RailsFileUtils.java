package com.yoursway.rails.commons;

import org.eclipse.core.resources.IFile;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.IType;
import org.eclipse.dltk.core.ModelException;

import com.yoursway.common.resources.PathUtils;
import com.yoursway.error_reporting.ER;

public class RailsFileUtils {
    
    public static IType findControllerTypeInFile(IFile file) {
        String baseName = PathUtils.getBaseNameWithoutExtension(file);
        String correctedUnderscoredName;
        if ("application".equals(baseName))
            correctedUnderscoredName = "application_controller";
        else
            correctedUnderscoredName = baseName;
        return findTypeInFile(file, correctedUnderscoredName);
    }
    
    public static IType findModelTypeInFile(IFile file) {
        String baseName = PathUtils.getBaseNameWithoutExtension(file);
        return findTypeInFile(file, baseName);
    }
    
    public static IType findTypeInFile(IFile file, String underscoredName) {
        IModelElement modelElement = DLTKCore.create(file);
        if (modelElement.getElementType() == IModelElement.SOURCE_MODULE) {
            ISourceModule sourceModule = (ISourceModule) modelElement;
            try {
                IType[] allTypes = sourceModule.getAllTypes();
                for (IType type : allTypes) {
                    String elementName = type.getElementName();
                    String simpleName = RailsNamingConventions.demodulize(elementName);
                    String pathName = RailsNamingConventions.underscore(simpleName);
                    if (pathName.equals(underscoredName)) {
                        return type;
                    }
                }
            } catch (ModelException e) {
                ER.unexpected(e);
            }
        }
        return null;
    }
    
    public static boolean isRubyFile(IFile file) {
        return "rb".equals(file.getFileExtension());
    }
    
    public static boolean isYamlFile(IFile file) {
        return "yml".equals(file.getFileExtension());
    }
    
}
