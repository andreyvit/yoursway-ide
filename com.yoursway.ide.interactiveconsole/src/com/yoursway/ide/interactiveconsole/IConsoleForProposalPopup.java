package com.yoursway.ide.interactiveconsole;

import java.util.List;

import org.eclipse.swt.graphics.Point;

public interface IConsoleForProposalPopup {
    
    List<CompletionProposal> getCompletionProposals();
    
    Point getLocationForPopup();
    
    void useCompletionProposal(CompletionProposal proposal);
    
}
