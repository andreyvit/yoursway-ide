package com.yoursway.rails.chooser.ui.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;

public class ChooseRailsDialog extends Dialog {
    
    private Combo installRailsInto;
    private Combo specificRailsCombo;
    private Link addRubyLink;
    private Button installRailsButton;
    private Button specificRailsButton;
    private Button latestRailsButton;
    private Composite latestRailsComposite;
    private Label latestRailsLabel;
    private IRailsChooserParameters parameters;
    private Composite installRailsComposite;
    private Composite specificRailsComposite;
    private Font boldFont;
    private Font normalFont;
    private IChoice choice;
    private SelectionListener buttonSelectionListener;
    private SelectionListener otherSelectionListener;
    
    boolean useParametersToSetOnOpen = true;
    private IRailsChooserParameters parametersToSetOnOpen;
    
    public ChooseRailsDialog(Shell parentShell) {
        super(parentShell);
    }
    
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        final GridLayout gridLayout_6 = new GridLayout();
        gridLayout_6.marginHeight = 0;
        gridLayout_6.marginWidth = 10;
        gridLayout_6.marginTop = 10;
        container.setLayout(gridLayout_6);
        
        createRadioGroup(container);
        createBottomLink(container);
        
        normalFont = JFaceResources.getFontRegistry().get("");
        boldFont = JFaceResources.getFontRegistry().getBold("");
        
        return container;
    }
    
    @Override
    protected Control createContents(Composite parent) {
        final Control result = super.createContents(parent);
        fillParametersOnOpen();
        return result;
    }
    
    private synchronized void fillParametersOnOpen() {
        useParametersToSetOnOpen = false;
        if (parametersToSetOnOpen != null) {
            setParametersInUiThread(parametersToSetOnOpen);
            IChoice initialChoice = parametersToSetOnOpen.getInitialChoice();
            if (initialChoice != null) {
                setCurrentChoiceTo(initialChoice);
            }
            parametersToSetOnOpen = null;
        }
    }
    
    private void setCurrentChoiceTo(IChoice initialChoice) {
        if (initialChoice instanceof LatestRailsChoice) {
            selectButton(latestRailsButton);
        } else if (initialChoice instanceof SpecificRailsChoice) {
            SpecificRailsChoice choice = (SpecificRailsChoice) initialChoice;
            IRailsDescription railsDescription = choice.getRails();
            int index = parameters.getSpecificRailsVersions().indexOf(railsDescription);
            if (index >= 0) {
                selectButton(specificRailsButton);
                specificRailsCombo.select(index);
            }
        }
        updateChoice();
    }
    
    private void createRadioGroup(Composite container) {
        buttonSelectionListener = new SelectionListener() {
            
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
                okPressed();
            }
            
            public void widgetSelected(SelectionEvent e) {
                handleButtonSelected((Button) e.widget);
            }
            
        };
        otherSelectionListener = new SelectionAdapter() {
            
            @Override
            public void widgetSelected(SelectionEvent e) {
                updateChoice();
            }
            
        };
        
        Label label = new Label(container, SWT.NONE);
        label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        label.setText("Which Rails installation would you like to use?");
        
        final Group group = new Group(container, SWT.NONE);
        group.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        final GridLayout radioGroup = new GridLayout();
        radioGroup.marginRight = 10;
        radioGroup.verticalSpacing = 10;
        group.setLayout(radioGroup);
        
        createLatestRailsOption(group);
        createSpecificRailsOption(group);
        createInstallRailsOption(group);
        if (false)
            createEdgeRailsOption(group);
    }
    
    private void createBottomLink(Composite container) {
        final Composite composite = new Composite(container, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        composite.setLayout(createOptionCompositeLayout());
        
        addRubyLink = new Link(composite, SWT.NONE);
        addRubyLink.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        addRubyLink
                .setText("You can <a href=\"aaa\">add your Ruby installation</a> if it wasn't found automatically.");
        addRubyLink.addSelectionListener(new SelectionAdapter() {
            
            @Override
            public void widgetSelected(SelectionEvent e) {
                // TODO: ask for Ruby instance here and try to add it
                updateChoice();
            }
            
        });
    }
    
    private void createEdgeRailsOption(final Group group) {
        final Composite edgeRailsComposite = new Composite(group, SWT.NONE);
        edgeRailsComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        edgeRailsComposite.setLayout(createOptionCompositeLayout());
        
        final Button useEdgeButton = new Button(edgeRailsComposite, SWT.RADIO);
        final GridData gd_useEdgeButton = new GridData(SWT.FILL, SWT.CENTER, true, false);
        useEdgeButton.setLayoutData(gd_useEdgeButton);
        useEdgeButton.setText("Use edge Rails");
        useEdgeButton.addSelectionListener(buttonSelectionListener);
    }
    
    private void createInstallRailsOption(final Group group) {
        installRailsComposite = new Composite(group, SWT.NONE);
        installRailsComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.marginWidth = 0;
        gridLayout_1.marginHeight = 0;
        installRailsComposite.setLayout(gridLayout_1);
        
        installRailsButton = new Button(installRailsComposite, SWT.RADIO);
        final GridData gd_installRailsButton = new GridData(SWT.FILL, SWT.CENTER, true, false);
        installRailsButton.setLayoutData(gd_installRailsButton);
        installRailsButton.setText("Install latest Rails 1.2.3");
        installRailsButton.addSelectionListener(buttonSelectionListener);
        
        final Composite composite_3_1 = new Composite(installRailsComposite, SWT.NONE);
        final GridData gd_composite_3_1 = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gd_composite_3_1.horizontalIndent = 30;
        composite_3_1.setLayoutData(gd_composite_3_1);
        composite_3_1.setLayout(createTwoColumnOptionCompositeLayout());
        
        final Label useLabel_1 = new Label(composite_3_1, SWT.NONE);
        useLabel_1.setText("into");
        
        installRailsInto = new Combo(composite_3_1, SWT.READ_ONLY);
        final GridData gd_combo_2 = new GridData(SWT.FILL, SWT.CENTER, true, false);
        installRailsInto.setLayoutData(gd_combo_2);
        installRailsInto.addSelectionListener(otherSelectionListener);
    }
    
    private void createSpecificRailsOption(final Group group) {
        specificRailsComposite = new Composite(group, SWT.NONE);
        specificRailsComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        final GridLayout gridLayout = createOptionCompositeLayout();
        specificRailsComposite.setLayout(gridLayout);
        
        specificRailsButton = new Button(specificRailsComposite, SWT.RADIO);
        final GridData gd_chooseRailsButton = new GridData(SWT.FILL, SWT.CENTER, true, false);
        specificRailsButton.setLayoutData(gd_chooseRailsButton);
        specificRailsButton.setText("Choose another Rails version");
        specificRailsButton.addSelectionListener(buttonSelectionListener);
        
        final Composite composite_3 = new Composite(specificRailsComposite, SWT.NONE);
        final GridData gd_composite_3 = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gd_composite_3.horizontalIndent = 30;
        composite_3.setLayoutData(gd_composite_3);
        final GridLayout gridLayout_2 = new GridLayout();
        gridLayout_2.marginWidth = 0;
        gridLayout_2.marginHeight = 0;
        gridLayout_2.numColumns = 2;
        composite_3.setLayout(gridLayout_2);
        
        final Label useLabel = new Label(composite_3, SWT.NONE);
        useLabel.setText("Use");
        
        specificRailsCombo = new Combo(composite_3, SWT.READ_ONLY);
        specificRailsCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        specificRailsCombo.addSelectionListener(otherSelectionListener);
    }
    
    private void createLatestRailsOption(final Group group) {
        latestRailsComposite = new Composite(group, SWT.NONE);
        latestRailsComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        final GridLayout optionLayout = createOptionCompositeLayout();
        latestRailsComposite.setLayout(optionLayout);
        
        latestRailsButton = new Button(latestRailsComposite, SWT.RADIO);
        final GridData gd_useLatestButton = new GridData(SWT.FILL, SWT.CENTER, true, false);
        latestRailsButton.setLayoutData(gd_useLatestButton);
        latestRailsButton.setText("Recommended: Use latest Rails found");
        latestRailsButton.addSelectionListener(buttonSelectionListener);
        
        latestRailsLabel = new Label(latestRailsComposite, SWT.NONE);
        final GridData gd_rails123UsingLabel = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gd_rails123UsingLabel.horizontalIndent = 30;
        latestRailsLabel.setLayoutData(gd_rails123UsingLabel);
        latestRailsLabel.setText("Rails 1.2.3, from Ruby 1.8.6 in /usr/local/bin");
    }
    
    protected void handleButtonSelected(Button selectedButton) {
        deselectOtherButtons(selectedButton);
        updateChoice();
    }
    
    private void selectButton(Button button) {
        button.setSelection(true);
        deselectOtherButtons(button);
    }
    
    private void deselectOtherButtons(Button selectedButton) {
        Button[] buttons = new Button[] { latestRailsButton, specificRailsButton, installRailsButton };
        for (Button button : buttons)
            if (button != selectedButton && button.getSelection())
                button.setSelection(false);
    }
    
    private GridLayout createOptionCompositeLayout() {
        final GridLayout gridLayout = new GridLayout();
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        return gridLayout;
    }
    
    private GridLayout createTwoColumnOptionCompositeLayout() {
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        return gridLayout;
    }
    
    @Override
    protected Control createButtonBar(Composite parent) {
        Composite composite = (Composite) super.createButtonBar(parent);
        GridLayout layout = (GridLayout) composite.getLayout();
        layout.makeColumnsEqualWidth = false;
        return composite;
    }
    
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
        Button okButton = createButton(parent, IDialogConstants.OK_ID, "Choose latest Rails", true);
        //        GridData data = new GridData(SWT.FILL, SWT.CENTER);
        //        int widthHint = convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
        //        Point minSize = okButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
        //        data.widthHint = Math.max(widthHint, minSize.x);
        //        okButton.setLayoutData(data);
    }
    
    @Override
    protected Point getInitialSize() {
        Point minSize = getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT);
        return new Point(513, minSize.y);
    }
    
    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Choose Rails");
    }
    
    public synchronized void setParameters(final IRailsChooserParameters parameters) {
        choice = null;
        if (useParametersToSetOnOpen)
            parametersToSetOnOpen = parameters;
        else
            Display.getDefault().asyncExec(new Runnable() {
                
                public void run() {
                    setParametersInUiThread(parameters);
                }
                
            });
    }
    
    private void setParametersInUiThread(final IRailsChooserParameters parameters) {
        this.parameters = parameters;
        fillLatestVersion();
        fillSpecificVersions();
        if (parameters.getRecommendedChoice() == RecommendedChoice.CHOOSE_MANUALLY)
            decorateRecommended(addRubyLink);
        else
            decorateNotRecommended(addRubyLink);
        updateChoice();
    }
    
    private void fillLatestVersion() {
        if (availability(latestRailsComposite, latestRailsButton, isLatestRailsAvailable())) {
            final String recommendedText = "Recommended: Use latest Rails version found";
            final String normalText = "Use latest Rails version found";
            recommendation(latestRailsButton, RecommendedChoice.LATEST_RAILS, recommendedText, normalText);
            latestRailsLabel.setText(formatRailsDescription(parameters.getLatestRails()));
        }
    }
    
    private void recommendation(final Button button, final RecommendedChoice choice,
            final String recommendedText, final String normalText) {
        if (parameters.getRecommendedChoice() == choice) {
            button.setText(recommendedText);
            decorateRecommended(button);
        } else {
            button.setText(normalText);
            decorateNotRecommended(button);
        }
    }
    
    private boolean isLatestRailsAvailable() {
        return parameters.getLatestRails() != null;
    }
    
    private void decorateRecommended(Control control) {
        control.setFont(boldFont);
    }
    
    private void decorateNotRecommended(Control control) {
        control.setFont(normalFont);
    }
    
    private void fillSpecificVersions() {
        if (availability(specificRailsComposite, specificRailsButton, isSpecificRailsAvailable())) {
            List<String> items = new ArrayList<String>();
            for (IRailsDescription railsDescription : parameters.getSpecificRailsVersions())
                items.add(formatRailsDescription(railsDescription));
            specificRailsCombo.setItems(items.toArray(new String[items.size()]));
            specificRailsCombo.select(0);
        }
    }
    
    private boolean isSpecificRailsAvailable() {
        return !parameters.getSpecificRailsVersions().isEmpty();
    }
    
    private String formatRailsDescription(IRailsDescription latestRails) {
        return NLS.bind("Rails {0} from Ruby {1} in {2}", new Object[] { latestRails.getVersion(),
                latestRails.getRuby().getVersion(), latestRails.getRuby().getLocation().getParent() });
    }
    
    private synchronized void updateChoice() {
        if (latestRailsButton.getSelection())
            choice = new LatestRailsChoice(parameters.getLatestRails());
        else if (specificRailsButton.getSelection()) {
            final int selectionIndex = specificRailsCombo.getSelectionIndex();
            if (selectionIndex < 0)
                choice = null;
            else
                choice = new SpecificRailsChoice(parameters.getSpecificRailsVersions().get(selectionIndex));
        } else if (installRailsButton.getSelection())
            choice = new InstallChoice(null); // TODO
        else
            choice = null;
        final Button okButton = getButton(IDialogConstants.OK_ID);
        if (choice == null) {
            okButton.setText("Choose");
            okButton.setEnabled(false);
        } else {
            okButton.setText(choice.getOkButtonLabel());
            okButton.setEnabled(true);
        }
        //        GridData data = new GridData(SWT.FILL, SWT.CENTER);
        //        int widthHint = convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
        //        Point minSize = okButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
        //        data.widthHint = Math.max(widthHint, minSize.x);
        //        okButton.setLayoutData(data);
    }
    
    private boolean availability(Composite composite, Button button, boolean available) {
        if (!available && button.getSelection())
            button.setSelection(false);
        button.setEnabled(available);
        composite.setEnabled(available);
        for (Control child : composite.getChildren())
            if (child != button)
                child.setVisible(available);
        return available;
    }
    
    @Override
    protected void okPressed() {
        super.okPressed();
    }
    
    public IChoice getChoice() {
        return choice;
    }
    
}
