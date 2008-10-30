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
import org.eclipse.dltk.python.parser.ast.PythonArgument;
import org.eclipse.swt.widgets.Display;

import com.yoursway.completion.CompletionProposalUpdatesListener;
import com.yoursway.completion.CompletionProposalsProvider;
import com.yoursway.completion.demo.CompletionProposalImpl;
import com.yoursway.ide.application.model.Document;
import com.yoursway.sadr.blocks.foundation.values.RuntimeObject;
import com.yoursway.sadr.python.ASTUtils;
import com.yoursway.sadr.python.core.runtime.FileSourceUnit;
import com.yoursway.sadr.python.core.runtime.ProjectRuntime;
import com.yoursway.sadr.python.core.runtime.ProjectUnit;
import com.yoursway.sadr.python_v2.constructs.ClassDeclarationC;
import com.yoursway.sadr.python_v2.constructs.Frog;
import com.yoursway.sadr.python_v2.constructs.MethodDeclarationC;
import com.yoursway.sadr.python_v2.constructs.PythonConstruct;
import com.yoursway.sadr.python_v2.constructs.PythonFileC;
import com.yoursway.sadr.python_v2.constructs.PythonVariableAcceptor;
import com.yoursway.sadr.python_v2.constructs.VariableReferenceC;
import com.yoursway.sadr.python_v2.goals.CreateInstanceGoal;
import com.yoursway.sadr.python_v2.goals.ResolveNameToObjectGoal;
import com.yoursway.sadr.python_v2.goals.acceptors.PythonValueSetAcceptor;
import com.yoursway.sadr.python_v2.model.Context;
import com.yoursway.sadr.python_v2.model.ContextImpl;
import com.yoursway.sadr.python_v2.model.PythonArguments;
import com.yoursway.sadr.succeeder.DefaultSchedulingStrategy;
import com.yoursway.sadr.succeeder.Engine;
import com.yoursway.sadr.succeeder.IGoal;
import com.yoursway.sadr.succeeder.IGrade;

public class PythonCompletion implements CompletionProposalsProvider {
	
	private final Document document;
	private List<CompletionProposalImpl> proposals;
	StoppableEngineContainer completer;
	
	class StoppableEngineContainer {
		private final Engine engine;
		private final CompletionProposalUpdatesListener listener;

		private boolean stopped = false;
		
		public StoppableEngineContainer(CompletionProposalUpdatesListener listener) {
	    	this.listener = listener;
			this.engine = new Engine(new DefaultSchedulingStrategy(){
	    		@Override
	    		public boolean prune(IGoal goal) {
	    			return stopped;
	    		}
	    	});
		}
		
		public void asyncRun(final IGoal goal) {
			Display.getDefault().asyncExec(new Runnable(){

				public void run() {
					engine.run(goal);
				}
				
			});
		}
		
		public void stop() {
			stopped = true;
		}

		public Engine getEngine() {
			return engine;
		}

		public boolean isStopped() {
			return stopped;
		}

		public CompletionProposalUpdatesListener getListener() {
			return listener;
		}
	}

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
            	
                getCompletionProposals(listener, fileC, (VariableReference) minimalNode, caretOffset);
            }
            else if (wordStarting == null || wordStarting.length() == 0) {
            	getCompletionProposals(listener, fileC, null, caretOffset);
            }
        }
        
//        if (enableKeywordCompletion && wordStarting != null)
//            for (String element : PythonKeyword.findByPrefix(wordStarting))
//                reportKeyword(element);
	}
	
    private Context createSelfContext(final MethodDeclarationC func, RuntimeObject self) {
        ArrayList<PythonArgument> list = new ArrayList<PythonArgument>();
        Context context = new ContextImpl(list, new PythonArguments());
        PythonArgument arg = (PythonArgument) func.node().getArguments().get(0);
        context.put(arg.getName(), self);
        return context;
    }
    
    public IGoal createSelfGoal(PythonFileC fileC, final PythonVariableAcceptor pythonVariableAcceptor,
            final PythonConstruct construct, final Engine engine) {
        if (construct.innerScope() instanceof MethodDeclarationC
                && construct.innerScope().parentScope() instanceof ClassDeclarationC) {
            final MethodDeclarationC func = (MethodDeclarationC) construct.innerScope();
            ClassDeclarationC classC = (ClassDeclarationC) construct.innerScope().parentScope();
            return new CreateInstanceGoal(classC, func, new PythonArguments(), Context.EMPTY,
                    new PythonValueSetAcceptor(Context.EMPTY) {
                        @Override
                        protected <T> void acceptIndividualResult(RuntimeObject result, IGrade<T> grade) {
                            Context context = createSelfContext(func, result);
                            IGoal goal = findProposals(construct, context, pythonVariableAcceptor);
                            engine.schedule(null, goal);
                        }

                    });
        } else {
            return findProposals(construct, Context.EMPTY, pythonVariableAcceptor);
        }
    }
    
    private IGoal findProposals(PythonConstruct construct, Context context, PythonVariableAcceptor pythonVariableAcceptor) {
    	if(construct instanceof VariableReferenceC){
    		Frog frog = new Frog(((VariableReferenceC) construct).name() + "*");
    		return new ResolveNameToObjectGoal(frog, construct, context, pythonVariableAcceptor);
    	}else{
    		Frog frog = new Frog("*");
    		return new ResolveNameToObjectGoal(frog, construct, context, pythonVariableAcceptor);
    	}
    }
    
    public IGoal createGoal(Engine engine, PythonFileC fileC, PythonVariableAcceptor pythonVariableAcceptor, int namePos) {
        ASTNode node = ASTUtils.findNodeAt(fileC.node(), namePos);
        if(node != null){
	        PythonConstruct construct = fileC.subconstructFor(node);
	        return createSelfGoal(fileC, pythonVariableAcceptor, construct, engine);
        }else{
        	Frog frog = new Frog("*");
    		return new ResolveNameToObjectGoal(frog, fileC, Context.EMPTY, pythonVariableAcceptor);
        }
    }

    private void getCompletionProposals(CompletionProposalUpdatesListener listener, PythonFileC file, VariableReference minimalNode, int caretOffset) {
    	completer = new StoppableEngineContainer(listener);
		IGoal goal = createGoal(completer.getEngine(), file, new PythonVariableAcceptor(){

			@Override
			public void addResult(String key, RuntimeObject value) {
				proposals.add(new CompletionProposalImpl(key, 0));
			}

			public <T> void checkpoint(IGrade<T> grade) {
				updateProposals();
			}
			
		}, caretOffset);
		completer.asyncRun(goal);
	}

	protected void updateProposals() {
		if(completer != null && !completer.isStopped()){
			Collections.sort(proposals, new Comparator<CompletionProposalImpl>() {
				public int compare(CompletionProposalImpl o1, CompletionProposalImpl o2) {
					return o1.relevance() - o2.relevance();
				}
			});
	
			completer.getListener().setProposals(proposals);
		}
	}

	public void stopCompletion() {
		if(completer != null){
			completer.stop();
			completer = null;
		}
	}

}
