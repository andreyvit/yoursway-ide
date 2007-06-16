package com.yoursway.ide.rhtml.internal.contentassist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.dltk.core.CompletionProposal;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.ui.text.completion.CompletionProposalComparator;
import org.eclipse.dltk.ui.text.completion.CompletionProposalLabelProvider;
import org.eclipse.dltk.ui.text.completion.IScriptCompletionProposal;
import org.eclipse.dltk.ui.text.completion.ScriptCompletionProposal;
import org.eclipse.dltk.ui.text.completion.ScriptCompletionProposalCollector;
import org.eclipse.dltk.ui.text.completion.ScriptContentAssistInvocationContext;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;

import com.yoursway.ide.rhtml.core.java.JSPTranslation;

/**
 * Passed into ICodeComplete#codeComplete(int offset, CompletionRequestor
 * requestor). Adapts IJavaCompletionProposals to JSPCompletion proposals. This
 * includes: - translating offsets - "fixing" up display strings - filtering
 * some unwanted proposals
 * 
 * @plannedfor 1.0
 */
public class JSPProposalCollector extends ScriptCompletionProposalCollector {
    
    private final JSPTranslation fTranslation;
    private Comparator fComparator;
    
    public JSPProposalCollector(ISourceModule cu, JSPTranslation translation) {
        super(cu);
        
        if (translation == null)
            throw new IllegalArgumentException("JSPTranslation cannot be null"); //$NON-NLS-1$
            
        fTranslation = translation;
    }
    
    /**
     * Ensures that we only return JSPCompletionProposals.
     * 
     * @return an array of JSPCompletionProposals
     */
    public JSPCompletionProposal[] getJSPCompletionProposals() {
        List results = new ArrayList();
        IScriptCompletionProposal[] javaProposals = getScriptCompletionProposals();
        // need to filter out non JSPCompletionProposals
        // because their offsets haven't been translated
        for (int i = 0; i < javaProposals.length; i++) {
            if (javaProposals[i] instanceof JSPCompletionProposal)
                results.add(javaProposals[i]);
        }
        Collections.sort(results, getComparator());
        return (JSPCompletionProposal[]) results.toArray(new JSPCompletionProposal[results.size()]);
    }
    
    private Comparator getComparator() {
        if (fComparator == null)
            fComparator = new CompletionProposalComparator();
        return fComparator;
    }
    
    /**
     * Overridden to: - translate Java -> JSP offsets - fix
     * cursor-position-after - fix mangled servlet name in display string -
     * remove unwanted proposals (servlet constructor)
     */
    protected IScriptCompletionProposal createJavaCompletionProposal(CompletionProposal proposal) {
        
        JSPCompletionProposal jspProposal = null;
        
        // ignore constructor proposals (they're not relevant for our JSP proposal list)
        if (!proposal.isConstructor()) {
            
            if (proposal.getKind() == CompletionProposal.TYPE_REF) {
                //                String signature = String.valueOf(proposal.get);
                //                String completion = String.valueOf(proposal.getCompletion());
                //                if (completion.indexOf(signature) != -1) {
                //                    jspProposal = createAutoImportProposal(proposal);
                //                }
            }
            
            // default behavior
            if (jspProposal == null)
                jspProposal = createJspProposal(proposal);
        }
        return jspProposal;
    }
    
    private JSPCompletionProposal createJspProposal(CompletionProposal proposal) {
        
        JSPCompletionProposal jspProposal;
        String completion = String.valueOf(proposal.getCompletion());
        // java offset
        int offset = proposal.getReplaceStart();
        // replacement length
        int length = proposal.getReplaceEnd() - offset;
        // translate offset from Java > JSP
        offset = fTranslation.getJspOffset(offset);
        // cursor position after must be calculated
        int positionAfter = calculatePositionAfter(proposal, completion, offset);
        
        // from java proposal
        IScriptCompletionProposal javaProposal = super.createScriptCompletionProposal(proposal);
        // XXX andreyvit: commented this out
        //        proposal.getDeclarationSignature();
        Image image = javaProposal.getImage();
        String displayString = javaProposal.getDisplayString();
        displayString = getTranslation().fixupMangledName(displayString);
        IContextInformation contextInformation = javaProposal.getContextInformation();
        // String additionalInfo = javaProposal.getAdditionalProposalInfo();
        int relevance = javaProposal.getRelevance();
        
        boolean updateLengthOnValidate = true;
        
        jspProposal = new JSPCompletionProposal(completion, offset, length, positionAfter, image,
                displayString, contextInformation, null, relevance, updateLengthOnValidate);
        
        // https://bugs.eclipse.org/bugs/show_bug.cgi?id=124483
        // set wrapped java proposal so additional info can be calculated on demand
        jspProposal.setJavaCompletionProposal(javaProposal);
        
        return jspProposal;
    }
    
    /**
     * Cacluates the where the cursor should be after applying this proposal.
     * eg. method(|) if the method proposal chosen had params.
     * 
     * @param proposal
     * @param completion
     * @param currentCursorOffset
     * @return
     */
    private int calculatePositionAfter(CompletionProposal proposal, String completion, int currentCursorOffset) {
        // calculate cursor position after
        // https://bugs.eclipse.org/bugs/show_bug.cgi?id=118398
        //int positionAfter = currentCursorOffset+completion.length();
        int positionAfter = completion.length();
        
        int kind = proposal.getKind();
        
        // may need better logic here...
        // put cursor inside parenthesis if there's params
        // only checking for any kind of declaration
        
        // XXX andreyvit: commented this out
        //        if (/*
        //         * kind == ScriptCompletionProposal.ANONYMOUS_CLASS_DECLARATION ||
        //         */kind == CompletionProposal.METHOD_DECLARATION
        //                || kind == CompletionProposal.POTENTIAL_METHOD_DECLARATION
        //                || kind == CompletionProposal.METHOD_REF) {
        //            String[] params = Signature.getParameterTypes(String.valueOf(proposal.getSignature()));
        //            if (completion.length() > 0 && params.length > 0)
        //                positionAfter--;
        //        }
        return positionAfter;
    }
    
    public JSPTranslation getTranslation() {
        return fTranslation;
    }
    
    @Override
    protected CompletionProposalLabelProvider createLabelProvider() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    protected ScriptCompletionProposal createOverrideCompletionProposal(IScriptProject scriptProject,
            ISourceModule compilationUnit, String name, String[] paramTypes, int start, int length,
            String label, String string) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    protected ScriptCompletionProposal createScriptCompletionProposal(String completion, int replaceStart,
            int length, Image image, String displayString, int i) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    protected ScriptCompletionProposal createScriptCompletionProposal(String completion, int replaceStart,
            int length, Image image, String displayString, int i, boolean isInDoc) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    protected ScriptContentAssistInvocationContext createScriptContentAssistInvocationContext(
            ISourceModule sourceModule) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    protected char[] getVarTrigger() {
        // TODO Auto-generated method stub
        return null;
    }
    
}