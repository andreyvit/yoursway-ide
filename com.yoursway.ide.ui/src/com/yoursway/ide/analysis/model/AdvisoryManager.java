package com.yoursway.ide.analysis.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.ElementChangedEvent;
import org.eclipse.dltk.core.IElementChangedListener;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IModelElementDelta;
import org.eclipse.dltk.core.ISourceModule;

public class AdvisoryManager implements IAdvisoryManager {
    
    private final class ElementChangedListener implements IElementChangedListener {
        
        public void install() {
            DLTKCore.addElementChangedListener(this);
        }
        
        public void uninstall() {
            DLTKCore.removeElementChangedListener(this);
        }
        
        public void elementChanged(ElementChangedEvent event) {
            IModelElementDelta delta = event.getDelta();
            visitDelta(event, delta);
        }
        
        private void visitDelta(ElementChangedEvent event, IModelElementDelta delta) {
            IModelElement element = delta.getElement();
            int elementType = element.getElementType();
            if (elementType > IModelElement.BINARY_MODULE)
                return; // do not visit member deltas
            if (elementType == IModelElement.SOURCE_MODULE)
                visitSourceModuleDelta(event, (ISourceModule) element, delta);
            visit(event, delta.getAddedChildren());
        }
        
        private void visit(ElementChangedEvent event, IModelElementDelta[] children) {
            for (IModelElementDelta child : children) {
                visitDelta(event, child);
            }
        }
    }
    
    class EditorManager implements IIteratesAdvices, IAdviceProviderListener {
        
        private final IAdvisingEditor editor;
        
        private IAdviceProvider[] providers;
        
        public EditorManager(IAdvisingEditor editor) {
            this.editor = editor;
        }
        
        public IAdvisingEditor getEditor() {
            return editor;
        }
        
        public void setProviders(IAdviceProvider[] providers) {
            this.providers = providers;
            for (IAdviceProvider provider : providers) {
                provider.setListener(this);
            }
        }
        
        public IAdviceProvider[] getProviders() {
            return providers;
        }
        
        public Iterator<IAdvice> iterateAdvices() {
            return new Iterator<IAdvice>() {
                
                private int providerIndex = -1;
                
                private Iterator<IAdvice> currentIterator;
                
                {
                    advanceProviderIndex();
                }
                
                public boolean hasNext() {
                    return currentIterator != null;
                }
                
                public IAdvice next() {
                    IAdvice next = currentIterator.next();
                    advanceProviderIndex();
                    return next;
                }
                
                public void remove() {
                    throw new UnsupportedOperationException();
                }
                
                void advanceProviderIndex() {
                    while (currentIterator == null || !currentIterator.hasNext()) {
                        if (++providerIndex >= providers.length) {
                            currentIterator = null;
                            break;
                        }
                        currentIterator = providers[providerIndex].iterateAdvices();
                    }
                }
                
            };
        }
        
        public void advicesChanged(IAdvicesChangeEvent event) {
            editor.advicesChanged(event);
        }
        
        public void processModelChange(ElementChangedEvent event, IModelElementDelta delta) {
            for (IAdviceProvider provider : providers) {
                provider.processModelChange(event, delta);
            }
        }
    }
    
    private static AdvisoryManager INSTANCE = new AdvisoryManager();
    
    private final ElementChangedListener elementChangedListener = new ElementChangedListener();
    
    private final Map<ISourceModule, Collection<EditorManager>> editors = new HashMap<ISourceModule, Collection<EditorManager>>();
    
    private final Collection<IAdviceProviderFactory> factories = new ArrayList<IAdviceProviderFactory>();
    
    public static AdvisoryManager instance() {
        return INSTANCE;
    }
    
    public void visitSourceModuleDelta(ElementChangedEvent event, ISourceModule sourceModule,
            IModelElementDelta delta) {
        Collection<EditorManager> editorsForSourceModule = editors.get(sourceModule);
        if (editorsForSourceModule != null) {
            for (EditorManager editorManager : editorsForSourceModule) {
                editorManager.processModelChange(event, delta);
            }
        }
    }
    
    public AdvisoryManager() {
        elementChangedListener.install();
    }
    
    public IIteratesAdvices registerEditor(IAdvisingEditor editor) {
        ISourceModule sourceModule = editor.getSourceModule();
        Collection<EditorManager> editorsForSourceModule = editors.get(sourceModule);
        if (editorsForSourceModule == null) {
            editorsForSourceModule = new ArrayList<EditorManager>();
            editors.put(sourceModule, editorsForSourceModule);
        }
        EditorManager editorManager = new EditorManager(editor);
        editorsForSourceModule.add(editorManager);
        final Collection<IAdviceProvider> providers = new ArrayList<IAdviceProvider>(factories.size());
        IAdviceProviderRequestor requestor = new IAdviceProviderRequestor() {
            
            public void acceptProvider(IAdviceProvider provider) {
                providers.add(provider);
            }
            
        };
        for (IAdviceProviderFactory factory : factories) {
            factory.createProviders(sourceModule, requestor);
        }
        editorManager.setProviders(providers.toArray(new IAdviceProvider[providers.size()]));
        return editorManager;
    }
    
    public void sourceModuleChanged(ISourceModule sourceModule) {
        // TODO Auto-generated method stub
    }
    
    public void unregisterEditor(IAdvisingEditor editor) {
        // TODO: dispose advice providers, clean up hashes
    }
    
    public void addAdviceProviderFactory(IAdviceProviderFactory factory) {
        factories.add(factory);
    }
    
}
