package com.yoursway.ide.ui.railsview.presentation;

import org.eclipse.core.resources.IFile;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.internal.ui.editor.EditorUtility;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;

import com.yoursway.ide.ui.Activator;

public abstract class AbstractPresenter implements IElementPresenter {
    
    private final IPresenterOwner owner;
    
    public AbstractPresenter(IPresenterOwner owner) {
        this.owner = owner;
    }
    
    protected void openEditor(IFile file) {
        try {
            IDE.openEditor(owner.getWorkbenchPage(), file);
        } catch (PartInitException e) {
            Activator.log(e);
        }
    }
    
    protected void openEditor(IModelElement element) {
        try {
            IEditorPart part = EditorUtility.openInEditor(element, true);
            if (element instanceof IModelElement)
                EditorUtility.revealInEditor(part, element);
        } catch (PartInitException e) {
            Activator.log(e);
        } catch (ModelException e) {
            Activator.log(e);
        }
    }
}
