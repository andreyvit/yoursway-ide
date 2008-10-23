package com.yoursway.ide.editors.text;

import static com.google.common.collect.Lists.newArrayListWithCapacity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.references.VariableReference;

import com.yoursway.completion.CompletionProposalUpdatesListener;
import com.yoursway.completion.CompletionProposalsProvider;
import com.yoursway.completion.demo.CompletionProposalImpl;
import com.yoursway.ide.application.model.Document;
import com.yoursway.sadr.python.ASTUtils;
import com.yoursway.sadr.python.core.runtime.FileSourceUnit;
import com.yoursway.sadr.python.core.runtime.ProjectRuntime;
import com.yoursway.sadr.python.core.runtime.ProjectUnit;
import com.yoursway.sadr.python_v2.constructs.PythonFileC;

public class PythonCompletion implements CompletionProposalsProvider {
	
	private final Document document;
	private List<CompletionProposalImpl> proposals;

	public PythonCompletion(Document document) {
		this.document = document;
	}

	public int findStartOfWord(CharSequence text, int caretOffset) {
		int beginIndex = caretOffset - 1;
		while(beginIndex>=0 && isCompletable(text.charAt(beginIndex))){
			beginIndex --;
		}
		return beginIndex + 1;
	}

	public boolean isCompletable(char character) {
		return Character.isJavaIdentifierPart(character);
	}

	public void startCompletionFor(final CompletionProposalUpdatesListener listener,
			CharSequence wholeDocument, int caretOffset) {
		int beginIndex = findStartOfWord(wholeDocument, caretOffset);
		proposals = new ArrayList<CompletionProposalImpl>();
		
		Collection<File> files = document.project().findAllFiles();
		Collection<FileSourceUnit> sourceUnits = newArrayListWithCapacity(files.size());
		for (File file : files)
			if (file.getName().toLowerCase().endsWith(".py")) {
				sourceUnits.add(new FileSourceUnit(file));
			}
		
		ProjectRuntime projectRuntime = new ProjectRuntime(new ProjectUnit(sourceUnits));
        PythonFileC fileC = projectRuntime.getModule(document.file());
        
		String wordStarting = wholeDocument.subSequence(beginIndex, caretOffset).toString();
        
        ASTNode minimalNode = ASTUtils.findMinimalNode(fileC.node(), caretOffset, caretOffset);
        if (minimalNode != null) {
            if (minimalNode instanceof VariableReference) {
            	
                getCompletionProposals((VariableReference) minimalNode, caretOffset);
            }
            else if (wordStarting == null || wordStarting.length() == 0) {
            	// TODO: fields?..
//                IModelElement[] children = modelModule.getChildren();
//                if (children != null)
//                    for (int j = 0; j < children.length; j++) {
//                        if (children[j] instanceof IField)
//                            reportField((IField) children[j], rel);
//                        if (children[j] instanceof IMethod) {
//                            IMethod method = (IMethod) children[j];
//                            if ((method.getFlags() & Modifiers.AccStatic) == 0)
//                                reportMethod(method, rel);
//                        }
//                        if (children[j] instanceof IType
//                                && !children[j].getElementName().trim().startsWith("<<"))
//                            reportType((IType) children[j], rel);
//                    }
            }
        }
        
//        if (enableKeywordCompletion && wordStarting != null)
//            for (String element : PythonKeyword.findByPrefix(wordStarting))
//                reportKeyword(element);



		Collections.sort(proposals, new Comparator<CompletionProposalImpl>() {
			public int compare(CompletionProposalImpl o1, CompletionProposalImpl o2) {
				return o1.relevance() - o2.relevance();
			}
		});

		listener.setProposals(proposals);
	}
	
	private void getCompletionProposals(VariableReference minimalNode, int caretOffset) {
		// TODO Auto-generated method stub
		
	}

	public void stopCompletion() {
	}

}
