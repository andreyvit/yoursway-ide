package com.yoursway.rails.chooser.ui.controller;

import java.util.List;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import com.yoursway.rails.RailsInstance;
import com.yoursway.rails.RailsInstancesManager;
import com.yoursway.rails.chooser.IRailsChooser;
import com.yoursway.rails.chooser.RailsRecommender;
import com.yoursway.rails.chooser.ui.view.ChooseRailsDialog;
import com.yoursway.rails.chooser.ui.view.IChoice;
import com.yoursway.rails.chooser.ui.view.IRailsDescription;
import com.yoursway.rails.chooser.ui.view.LatestRailsChoice;
import com.yoursway.rails.chooser.ui.view.RecommendedChoice;
import com.yoursway.rails.chooser.ui.view.SpecificRailsChoice;

public class GuiRailsChooser implements IRailsChooser {
    
    private final class ChooseRunnable implements Runnable {
        private RailsInstance result;
        
        public void run() {
            dialog = new ChooseRailsDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
            updateDialogInformation();
            int code = dialog.open();
            if (code == Window.CANCEL)
                result = null;
            else {
                IChoice choice = dialog.getChoice();
                if (choice instanceof LatestRailsChoice)
                    result = getRailsInstanceByDescription(((LatestRailsChoice) choice).getRails());
                else if (choice instanceof SpecificRailsChoice)
                    result = getRailsInstanceByDescription(((SpecificRailsChoice) choice).getRails());
                else
                    result = null;
            }
        }
    }
    
    private ChooseRailsDialog dialog;
    
    public RailsInstance choose() {
        final ChooseRunnable chooser = new ChooseRunnable();
        Display.getDefault().syncExec(chooser);
        return chooser.result;
    }
    
    private void updateDialogInformation() {
        RailsChooserParameters parameters = new RailsChooserParameters();
        RecommendedChoice recommendedChoice = RecommendedChoice.INSTALL_RAILS;
        List<RailsInstance> railsInstance = RailsInstancesManager.getRailsInstance();
        if (!railsInstance.isEmpty()) {
            RailsRecommender railsRecommender = new RailsRecommender();
            RailsInstance bestRailsInstance = railsRecommender.chooseBestRailsInstanceVersion(railsInstance);
            parameters.setLatestRails(new RailsDescription(bestRailsInstance));
            recommendedChoice = RecommendedChoice.LATEST_RAILS;
        }
        for (RailsInstance item : railsInstance)
            parameters.getSpecificRailsVersions().add(new RailsDescription(item));
        parameters.setRecommendedChoice(recommendedChoice);
        dialog.setParameters(parameters);
    }
    
    private RailsInstance getRailsInstanceByDescription(final IRailsDescription rails) {
        return ((RailsDescription) rails).getRailsInstance();
    }
    
}
