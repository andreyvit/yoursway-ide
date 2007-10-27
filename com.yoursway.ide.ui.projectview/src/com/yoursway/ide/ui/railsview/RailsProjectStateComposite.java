package com.yoursway.ide.ui.railsview;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;

public class RailsProjectStateComposite extends Composite {
    
    private interface ILinkListener {
        void linkClicked(String href);
    };
    
    private RailsServerState state;
    private final Link stoppedServerLink;
    private final Link stoppingServerLink;
    private final Link runningServerLink;
    private final Link startingServerLink;
    private final StackLayout stackLayout;
    
    public RailsProjectStateComposite(Composite parent, int style, RailsServerState initialState) {
        super(parent, style);
        stackLayout = new StackLayout();
        setLayout(stackLayout);
        stackLayout.marginHeight = 3;
        stackLayout.marginWidth = 3;
        stoppedServerLink = createLink(this, "Server is stopped. <a href=\"start\">Start</a>",
                new ILinkListener() {
                    
                    public void linkClicked(String href) {
                        RailsProjectStateComposite.this.setState(new StartingServerState());
                    }
                    
                });
        stoppingServerLink = createLink(this, "Server is stopping... <a href=\"kill\">Kill</a>",
                new ILinkListener() {
                    
                    public void linkClicked(String href) {
                        // TODO Auto-generated method stub
                    }
                    
                });
        runningServerLink = createLink(this,
                "Server is running at <a href=\"hui\">Pizda</a>. <a href=\"stop\">Stop</a>",
                new ILinkListener() {
                    
                    public void linkClicked(String href) {
                        // TODO Auto-generated method stub
                    }
                    
                });
        startingServerLink = createLink(this, "Server is starting... <a href=\"kill\">Kill</a>",
                new ILinkListener() {
                    
                    public void linkClicked(String href) {
                        // TODO Auto-generated method stub
                    }
                    
                });
        setState(initialState);
    }
    
    private Link createLink(Composite parent, String text, final ILinkListener listener) {
        Link link = new Link(parent, SWT.NONE);
        //        link.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        link.setText(text);
        link.setVisible(false);
        link.addSelectionListener(new SelectionAdapter() {
            
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (listener != null)
                    listener.linkClicked(e.text);
            }
            
        });
        return link;
    }
    
    private void setState(RailsServerState status) {
        state = status;
        state.visit(new IRailsServerStateVisitor() {
            
            public void visit(StartingServerState state) {
                stackLayout.topControl = startingServerLink;
                RailsProjectStateComposite.this.layout();
            }
            
            public void visit(StoppingServerState state) {
                stackLayout.topControl = stoppingServerLink;
                RailsProjectStateComposite.this.layout();
            }
            
            public void visit(RunningServerState state) {
                stackLayout.topControl = runningServerLink;
                //TODO : fix server address
                RailsProjectStateComposite.this.layout();
            }
            
            public void visit(StoppedServerState state) {
                stackLayout.topControl = stoppedServerLink;
                RailsProjectStateComposite.this.layout();
            }
            
        });
    }
    
}
