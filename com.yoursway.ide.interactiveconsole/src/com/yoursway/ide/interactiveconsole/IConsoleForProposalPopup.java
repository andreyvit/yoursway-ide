package com.yoursway.ide.interactiveconsole;

import java.util.List;

import org.eclipse.swt.graphics.Point;

public interface IConsoleForProposalPopup {
    //? remove this interface, use Console class
    
    List<CompletionProposal> getCompletionProposals();
    
    Point getLocationForPopup();
    
    void useCompletionProposal(CompletionProposal proposal);
    
}
