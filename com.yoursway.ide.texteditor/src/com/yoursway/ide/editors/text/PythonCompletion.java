package com.yoursway.ide.editors.text;

import static com.google.common.collect.Lists.newArrayListWithCapacity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.dltk.ast.ASTNode;
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
	
	public void asyncUpdate() {
		Display.getDefault().asyncExec(
				new Runnable(){
					public void run() {
						updateProposals();
					}
				});
	}

	
	private static int counter = 0;
	public static int setId() {
		return ++counter;
	}
	
	class StoppableEngineContainer {
		private final Engine engine;
		private final CompletionProposalUpdatesListener listener;

		private boolean stopped = false;
		private int uid;
		private Thread thread;
		
		public StoppableEngineContainer(CompletionProposalUpdatesListener listener) {
			uid = setId();
	    	this.listener = listener;
			this.engine = new Engine(new DefaultSchedulingStrategy(){
	    		@Override
	    		public boolean prune(IGoal goal) {
	    			return stopped;
	    		}
	    	});

		}
		
		public void asyncRun(final int caretOffset, final String wordStarting) {
			
			thread = new Thread(){

				public void run() {
					System.out.println("Engine #"+uid+" started.");
					long start = System.currentTimeMillis();
					PythonFileC fileC = currentProject();
			        long dur = System.currentTimeMillis() - start;
					
					IGoal goal = createGoal(completer.getEngine(), fileC, wordStarting, new PythonVariableAcceptor(){

						@Override
						public void addResult(String key, RuntimeObject value) {
							proposals.add(new CompletionProposalImpl(key, key.startsWith(wordStarting)?1:0));
						}

						public <T> void checkpoint(IGrade<T> grade) {
							asyncUpdate();
						}
						
					}, caretOffset);

					try{
						engine.run(goal);
					}catch(RuntimeException e){
						e.printStackTrace();
						asyncUpdate();
					}
					System.out.println("Engine #"+uid+" finished, took "+dur+" ms.");
				}

				
			};
			thread.start();
		}
		
		public void stop() {
			thread.interrupt();
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
		
        
		String wordStarting = wholeDocument.subSequence(beginIndex, caretOffset).toString();
        
		getCompletionProposals(listener, caretOffset, wordStarting);

        
//        if (enableKeywordCompletion && wordStarting != null)
//            for (String element : PythonKeyword.findByPrefix(wordStarting))
//                reportKeyword(element);
	}

	private PythonFileC currentProject() {
		Collection<File> files = document.project().findAllFiles();
		Collection<FileSourceUnit> sourceUnits = newArrayListWithCapacity(files.size());
		String root = document.project().getLocation().getAbsolutePath();
		if(!root.endsWith("/")) root += "/";
		for (File file : files)
			if (file.getName().toLowerCase().endsWith(".py")) {
				String path = file.getAbsolutePath();
				path = path.substring(0, path.length()-3).replace(root, "").replace("/", ".");
				sourceUnits.add(new FileSourceUnit(file, path));
			}
		
		ProjectRuntime projectRuntime = new ProjectRuntime(new ProjectUnit(sourceUnits));
        PythonFileC fileC = projectRuntime.getModule(document.file());
		return fileC;
	}
	
    private Context createSelfContext(final MethodDeclarationC func, RuntimeObject self) {
        ArrayList<PythonArgument> list = new ArrayList<PythonArgument>();
        Context context = new ContextImpl(list, new PythonArguments());
        PythonArgument arg = (PythonArgument) func.node().getArguments().get(0);
        context.put(arg.getName(), self);
        return context;
    }
    
    public IGoal createSelfGoal(PythonFileC fileC, final PythonVariableAcceptor pythonVariableAcceptor,
            final PythonConstruct construct, final Engine engine, final String wordStarting) {
        if (construct.innerScope() instanceof MethodDeclarationC
                && construct.innerScope().parentScope() instanceof ClassDeclarationC) {
            final MethodDeclarationC func = (MethodDeclarationC) construct.innerScope();
            ClassDeclarationC classC = (ClassDeclarationC) construct.innerScope().parentScope();
            return new CreateInstanceGoal(classC, func, new PythonArguments(), Context.EMPTY,
                    new PythonValueSetAcceptor(Context.EMPTY) {
                        @Override
                        protected <T> void acceptIndividualResult(RuntimeObject result, IGrade<T> grade) {
                            Context context = createSelfContext(func, result);
                            IGoal goal = findProposals(construct, context, pythonVariableAcceptor, wordStarting);
                            engine.schedule(null, goal);
                        }

                    });
        } else {
            return findProposals(construct, Context.EMPTY, pythonVariableAcceptor, wordStarting);
        }
    }
    
    private IGoal findProposals(PythonConstruct construct, Context context, PythonVariableAcceptor pythonVariableAcceptor, String wordStarting) {
		Frog frog = new Frog(wordStarting + "*");
		return new ResolveNameToObjectGoal(frog, construct, context, pythonVariableAcceptor);
    }
    
    public IGoal createGoal(Engine engine, PythonFileC fileC, String wordStarting, PythonVariableAcceptor pythonVariableAcceptor, int namePos) {
        ASTNode node = ASTUtils.findNodeAt(fileC.node(), namePos);
        PythonConstruct construct = null;
        try{
			construct = fileC.subconstructFor(node);
        }catch(NullPointerException e){
        }catch(RuntimeException e){
        	e.printStackTrace();
        }
        if(node != null && construct != null){
	        return createSelfGoal(fileC, pythonVariableAcceptor, construct, engine, wordStarting);
        }else{
        	Frog frog = new Frog(wordStarting+"*");
    		return new ResolveNameToObjectGoal(frog, fileC, Context.EMPTY, pythonVariableAcceptor);
        }
    }

    private void getCompletionProposals(CompletionProposalUpdatesListener listener, int caretOffset, final String wordStarting) {
    	completer = new StoppableEngineContainer(listener);
		completer.asyncRun(caretOffset, wordStarting);
	}

	protected void updateProposals() {
		if(completer != null && !completer.isStopped()){
			Collections.sort(proposals, new Comparator<CompletionProposalImpl>() {
				public int compare(CompletionProposalImpl o1, CompletionProposalImpl o2) {
					int f = o1.relevance() - o2.relevance();
					if (f>0) return 1;
					if (f<0) return -1;
					return o1.completion().compareToIgnoreCase(o2.completion());
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

