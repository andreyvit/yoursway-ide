package com.yoursway.ide.analysis.sample;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.expressions.CallExpression;
import org.eclipse.dltk.core.ElementChangedEvent;
import org.eclipse.dltk.core.IMethod;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IModelElementDelta;
import org.eclipse.dltk.core.IModelElementVisitor;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.IType;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.core.ISourceModuleInfoCache.ISourceModuleInfo;
import org.eclipse.dltk.internal.core.ModelManager;
import org.eclipse.dltk.ruby.internal.parser.RubySourceElementParser;
import org.eclipse.dltk.ruby.typeinference.RubyModelUtils;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.swt.graphics.GC;

import com.yoursway.ide.analysis.model.AdvicesChangeEvent;
import com.yoursway.ide.analysis.model.IAdvice;
import com.yoursway.ide.analysis.model.IAdviceProvider;
import com.yoursway.ide.analysis.model.IAdviceProviderListener;
import com.yoursway.ide.ui.Activator;
import com.yoursway.utils.PathUtils;
import com.yoursway.utils.RailsNamingConventions;
import com.yoursway.utils.RubyASTUtils;
import com.yoursway.utils.RubyModelUtils2;

public class ControllerNavigationProvider implements IAdviceProvider {
    
    private final static class Advice implements IAdvice {
        private final int line;
        private final String caption;
        
        public Advice(int line, String caption) {
            this.line = line;
            this.caption = caption;
        }
        
        public int getLineNumber() {
            return line;
        }
        
        public int getPriority() {
            return 0;
        }
        
        public void paint(GC gc, int x, int y) {
            gc.drawText(caption, x, y, true);
        }
    }
    
    private final ISourceModule sourceModule;
    
    private final Collection<IAdvice> advices = new ArrayList<IAdvice>();
    
    private IAdviceProviderListener listener;
    
    public ControllerNavigationProvider(ISourceModule sourceModule) {
        this.sourceModule = sourceModule;
        recalculate(false);
    }
    
    public void dispose() {
        
    }
    
    public void processModelChange(ElementChangedEvent event, IModelElementDelta delta) {
        recalculate(true);
    }
    
    public void setListener(IAdviceProviderListener listener) {
        this.listener = listener;
    }
    
    public Iterator<IAdvice> iterateAdvices() {
        return advices.iterator();
    }
    
    private void recalculate(boolean notifyListener) {
        advices.clear();
        calculateAdvices(advices);
        if (notifyListener)
            listener.advicesChanged(new AdvicesChangeEvent(null));
    }
    
    private void calculateAdvices(final Collection<IAdvice> newAdvices) {
        IResource resource = sourceModule.getResource();
        IPath path = resource.getProjectRelativePath();
        boolean isController;
        String fileName = path.lastSegment();
        if (fileName.endsWith("_controller.rb"))
            isController = true;
        else if (fileName.equals("application.rb")
                && PathUtils.startsWith(path, new String[] { "app", "controllers" })) {
            isController = true;
        } else {
            isController = false;
        }
        if (isController) {
            try {
                sourceModule.accept(new IModelElementVisitor() {
                    
                    public boolean visit(IModelElement element) {
                        if (element instanceof IMethod) {
                            IMethod method = (IMethod) element;
                            calculateAdvicesInMethod(newAdvices, method);
                        }
                        return true;
                    }
                    
                });
            } catch (ModelException e) {
                Activator.log(e);
            }
        }
    }
    
    private void calculateAdvicesInMethod(Collection<IAdvice> newAdvices, IMethod method) {
        try {
            String methodName = method.getElementName();
            int offset = method.getSourceRange().getOffset();
            Document document = new Document(sourceModule.getSource());
            int line = document.getLineOfOffset(offset);
            
            final Collection<String> renderedActions = new HashSet<String>();
            
            ModuleDeclaration astRoot = null;
            ISourceModuleInfo cache = ModelManager.getModelManager().getSourceModuleInfoCache().get(
                    sourceModule);
            if (cache != null)
                astRoot = (ModuleDeclaration) cache.get(RubySourceElementParser.AST);
            MethodDeclaration methodNode = null;
            if (astRoot != null)
                methodNode = RubyModelUtils.getNodeByMethod(astRoot, method);
            if (methodNode != null) {
                try {
                    methodNode.traverse(new ASTVisitor() {
                        
                        @Override
                        public void endvisitGeneral(ASTNode node) throws Exception {
                            super.endvisitGeneral(node);
                        }
                        
                        @Override
                        public boolean visitGeneral(ASTNode node) throws Exception {
                            if (node instanceof CallExpression) {
                                CallExpression callExpression = (CallExpression) node;
                                ASTNode receiver = callExpression.getReceiver();
                                if (receiver == null) {
                                    String name = callExpression.getName();
                                    if ("render".equals(name))
                                        processRenderMethod(renderedActions, callExpression);
                                    else if ("render_action".equals(name))
                                        processRenderActionMethod(renderedActions, callExpression);
                                }
                            }
                            return super.visitGeneral(node);
                        }
                        
                        private void processRenderActionMethod(final Collection<String> renderedActions,
                                CallExpression callExpression) {
                            ASTNode argNode = RubyASTUtils.getArgumentValue(callExpression, 0);
                            String stringValue = RubyASTUtils.resolveConstantStringValue(argNode);
                            if (stringValue != null)
                                renderedActions.add(stringValue);
                        }
                        
                        private void processRenderMethod(final Collection<String> renderedActions,
                                CallExpression callExpression) {
                            //FIXME: broken at latest DLTK
                            //                          Statement arg = RubyASTUtils.getArgumentValue(callExpression, 0);
                            //                            if (arg instanceof RubyHashExpression) {
                            //                                Statement v = RubyASTUtils.findHashItemValue((RubyHashExpression) arg,
                            //                                        "action");
                            //                                String stringValue = RubyASTUtils.resolveConstantStringValue(v);
                            //                                if (stringValue != null)
                            //                                    renderedActions.add(stringValue);
                            //                            }
                        }
                        
                    });
                } catch (Exception e) {
                    Activator.log(e);
                }
            }
            
            renderedActions.add(methodName);
            
            Advice advice;
            IProject project = sourceModule.getScriptProject().getProject();
            IType type = (IType) method.getAncestor(IModelElement.TYPE);
            String[] fqn = RubyModelUtils2.getFullyQualifiedName(type);
            IFolder folder = null;
            if (fqn != null) {
                String[] segments = RailsNamingConventions.controllerNameToPath(fqn);
                IPath path = new Path("app/views");
                for (String segment : segments)
                    path = path.append(segment);
                folder = project.getFolder(path);
            }
            for (String actionName : renderedActions) {
                IFile fileToGoTo = null;
                if (folder != null && folder.exists())
                    try {
                        IResource[] members = folder.members();
                        for (IResource resource : members)
                            if (resource instanceof IFile) {
                                IFile file = (IFile) resource;
                                String baseName = PathUtils.getBaseNameWithoutExtension(file);
                                if (baseName.equals(actionName)) {
                                    fileToGoTo = file;
                                    break;
                                }
                            }
                    } catch (CoreException e) {
                        Activator.log(e);
                    }
                if (fileToGoTo != null)
                    advice = new Advice(line, "-> go to " + fileToGoTo.getName());
                else
                    advice = new Advice(line, "-> create " + actionName + ".rhtml");
                newAdvices.add(advice);
                line++;
            }
        } catch (ModelException e) {
            Activator.log(e);
        } catch (BadLocationException e) {
            Activator.log(e);
        }
    }
}
