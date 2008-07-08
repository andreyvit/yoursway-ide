package com.yoursway.ide.interactiveconsole;

import java.util.List;

import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;

public interface IConsoleForProposalPopup {
    
    void addModifyListener(ModifyListener modifyListener);
    
    void useCompletionProposal(CompletionProposal proposal);
    
    List<CompletionProposal> getCompletionProposals();
    
    Point getLocationForPopup();
    
}
