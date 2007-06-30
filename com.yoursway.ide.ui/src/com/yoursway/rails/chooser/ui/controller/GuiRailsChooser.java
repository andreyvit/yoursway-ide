package com.yoursway.rails.chooser.ui.controller;

import java.util.List;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import com.yoursway.rails.Rails;
import com.yoursway.rails.RailsRuntime;
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
        private Rails result;
        
        public void run() {
            dialog = new ChooseRailsDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
            updateDialogInformation();
            int code = dialog.open();
            if (code == Window.CANCEL)
                result = null;
            else {
                IChoice choice = dialog.getChoice();
                if (choice instanceof LatestRailsChoice)
                    result = getRailsByDescription(((LatestRailsChoice) choice).getRails());
                if (choice instanceof SpecificRailsChoice)
                    result = getRailsByDescription(((SpecificRailsChoice) choice).getRails());
                result = null;
            }
        }
    }
    
    private ChooseRailsDialog dialog;
    
    public Rails choose() {
        final ChooseRunnable chooser = new ChooseRunnable();
        Display.getDefault().syncExec(chooser);
        return chooser.result;
    }
    
    private void updateDialogInformation() {
        RailsChooserParameters parameters = new RailsChooserParameters();
        RecommendedChoice recommendedChoice = RecommendedChoice.INSTALL_RAILS;
        List<Rails> rails = RailsRuntime.getRails();
        if (!rails.isEmpty()) {
            RailsRecommender railsRecommender = new RailsRecommender();
            Rails bestRails = railsRecommender.chooseBestRailsVersion(rails);
            parameters.setLatestRails(new RailsDescription(bestRails));
            recommendedChoice = RecommendedChoice.LATEST_RAILS;
        }
        for (Rails item : rails)
            parameters.getSpecificRailsVersions().add(new RailsDescription(item));
        parameters.setRecommendedChoice(recommendedChoice);
        dialog.setParameters(parameters);
    }
    
    private Rails getRailsByDescription(final IRailsDescription rails) {
        return ((RailsDescription) rails).getRails();
    }
    
}
