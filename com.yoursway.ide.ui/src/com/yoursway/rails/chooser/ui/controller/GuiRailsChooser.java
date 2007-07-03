package com.yoursway.rails.chooser.ui.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
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
import com.yoursway.ruby.RubyInstance;
import com.yoursway.ruby.RubyInstanceCollection;

public class GuiRailsChooser implements IRailsChooser {
    
    private IChoice previousChoice;
    
    private final class ChooseRunnable implements Runnable {
        private RailsInstance result;
        private final IChoice previousChoice;
        
        public ChooseRunnable(IChoice latestRailsChoice) {
            this.previousChoice = latestRailsChoice;
        }
        
        public void run() {
            result = showChooser(previousChoice);
        }
    }
    
    private RailsInstance showChooser(IChoice previousChoice) {
        this.previousChoice = previousChoice;
        synchronized (this) {
            Assert.isTrue(dialog == null);
            dialog = new ChooseRailsDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
        }
        try {
            updateDialogInformation();
            int code = dialog.open();
            if (code == Window.CANCEL)
                return null;
            else {
                IChoice choice = dialog.getChoice();
                if (choice instanceof LatestRailsChoice)
                    return getRailsInstanceByDescription(((LatestRailsChoice) choice).getRails());
                else if (choice instanceof SpecificRailsChoice)
                    return getRailsInstanceByDescription(((SpecificRailsChoice) choice).getRails());
                else
                    return null;
            }
        } finally {
            synchronized (this) {
                dialog = null;
            }
        }
    }
    
    private ChooseRailsDialog dialog;
    
    public RailsInstance choose(RailsInstance previousChoice) {
        final IChoice initialChoice;
        if (previousChoice == null)
            initialChoice = null;
        else
            initialChoice = new SpecificRailsChoice(new RailsDescription(previousChoice));
        final ChooseRunnable chooser = new ChooseRunnable(initialChoice);
        Display.getDefault().syncExec(chooser);
        return chooser.result;
    }
    
    private void updateDialogInformation() {
        RailsChooserParameters parameters = new RailsChooserParameters();
        
        parameters.setRailsVersionToInstall("1.2.3");
        for (RubyInstance rubyInstance : RubyInstanceCollection.instance().getAll())
            parameters.getRubyInstancesToInstallInto().add(new RubyDescription(rubyInstance));
        
        List<RailsInstance> railsInstances = new ArrayList<RailsInstance>(RailsInstancesManager
                .getRailsInstances());
        if (!railsInstances.isEmpty()) {
            RailsRecommender railsRecommender = new RailsRecommender();
            railsRecommender.sortRailsFromBestToWorst(railsInstances);
            RailsInstance bestRailsInstance = railsInstances.get(0);
            parameters.setLatestRails(new RailsDescription(bestRailsInstance));
            for (RailsInstance item : railsInstances)
                parameters.getSpecificRailsVersions().add(new RailsDescription(item));
        }
        parameters.setRecommendedChoice(determineChoiceToRecommend(parameters));
        parameters.setInitialChoice(previousChoice);
        synchronized (this) {
            if (dialog == null)
                return;
            dialog.setParameters(parameters);
        }
    }
    
    private RecommendedChoice determineChoiceToRecommend(RailsChooserParameters parameters) {
        RecommendedChoice recommendedChoice = RecommendedChoice.CHOOSE_MANUALLY;
        if (parameters.isInstallAvailable())
            recommendedChoice = RecommendedChoice.INSTALL_RAILS;
        if (parameters.isLatestRailsAvailable())
            recommendedChoice = RecommendedChoice.LATEST_RAILS;
        return recommendedChoice;
    }
    
    private RailsInstance getRailsInstanceByDescription(final IRailsDescription rails) {
        return ((RailsDescription) rails).getRailsInstance();
    }
    
}
