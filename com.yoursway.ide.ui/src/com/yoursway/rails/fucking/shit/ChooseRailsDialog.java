package com.yoursway.rails.fucking.shit;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;

public class ChooseRailsDialog extends Dialog {

	private Combo combo_2;
	private Combo combo;

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

		final Group group = new Group(container, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		final GridLayout radioGroup = new GridLayout();
		radioGroup.marginRight = 10;
		radioGroup.verticalSpacing = 10;
		group.setLayout(radioGroup);

		final Composite composite = new Composite(group, SWT.NONE);
		composite
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		final GridLayout optionLayout = new GridLayout();
		optionLayout.marginHeight = 0;
		optionLayout.marginWidth = 0;
		composite.setLayout(optionLayout);

		final Button useLatestButton = new Button(composite, SWT.RADIO);
		final GridData gd_useLatestButton = new GridData();
		useLatestButton.setLayoutData(gd_useLatestButton);
		useLatestButton.setText("Recommended: Use latest Rails found");

		final Label rails123UsingLabel = new Label(composite, SWT.NONE);
		final GridData gd_rails123UsingLabel = new GridData();
		gd_rails123UsingLabel.horizontalIndent = 30;
		rails123UsingLabel.setLayoutData(gd_rails123UsingLabel);
		rails123UsingLabel
				.setText("Rails 1.2.3, from Ruby 1.8.6 in /usr/local/bin");

		final Composite composite_1 = new Composite(group, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));
		final GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		composite_1.setLayout(gridLayout);

		final Button chooseRailsButton = new Button(composite_1, SWT.RADIO);
		final GridData gd_chooseRailsButton = new GridData(SWT.FILL,
				SWT.CENTER, true, false);
		chooseRailsButton.setLayoutData(gd_chooseRailsButton);
		chooseRailsButton.setText("Choose another Rails version");

		final Composite composite_3 = new Composite(composite_1, SWT.NONE);
		final GridData gd_composite_3 = new GridData(SWT.FILL, SWT.CENTER,
				true, false);
		gd_composite_3.horizontalIndent = 30;
		composite_3.setLayoutData(gd_composite_3);
		final GridLayout gridLayout_2 = new GridLayout();
		gridLayout_2.marginWidth = 0;
		gridLayout_2.marginHeight = 0;
		gridLayout_2.numColumns = 2;
		composite_3.setLayout(gridLayout_2);

		final Label useLabel = new Label(composite_3, SWT.NONE);
		useLabel.setText("Use");

		combo = new Combo(composite_3, SWT.NONE);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		final Composite composite_2 = new Composite(group, SWT.NONE);
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));
		final GridLayout gridLayout_1 = new GridLayout();
		gridLayout_1.marginWidth = 0;
		gridLayout_1.marginHeight = 0;
		composite_2.setLayout(gridLayout_1);

		final Button installRailsButton = new Button(composite_2, SWT.RADIO);
		final GridData gd_installRailsButton = new GridData(SWT.FILL,
				SWT.CENTER, true, false);
		installRailsButton.setLayoutData(gd_installRailsButton);
		installRailsButton.setText("Install latest Rails 1.2.3");

		final Composite composite_3_1 = new Composite(composite_2, SWT.NONE);
		final GridData gd_composite_3_1 = new GridData(SWT.FILL, SWT.CENTER,
				true, false);
		gd_composite_3_1.horizontalIndent = 30;
		composite_3_1.setLayoutData(gd_composite_3_1);
		final GridLayout gridLayout_3 = new GridLayout();
		gridLayout_3.numColumns = 2;
		gridLayout_3.marginWidth = 0;
		gridLayout_3.marginHeight = 0;
		composite_3_1.setLayout(gridLayout_3);

		final Label useLabel_1 = new Label(composite_3_1, SWT.NONE);
		useLabel_1.setText("into");

		combo_2 = new Combo(composite_3_1, SWT.NONE);
		final GridData gd_combo_2 = new GridData(SWT.FILL, SWT.CENTER, true,
				false);
		combo_2.setLayoutData(gd_combo_2);

		final Composite composite_2_1 = new Composite(group, SWT.NONE);
		composite_2_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));
		final GridLayout gridLayout_4 = new GridLayout();
		gridLayout_4.marginWidth = 0;
		gridLayout_4.marginHeight = 0;
		composite_2_1.setLayout(gridLayout_4);

		final Button useEdgeButton = new Button(composite_2_1, SWT.RADIO);
		final GridData gd_useEdgeButton = new GridData(SWT.FILL, SWT.CENTER,
				true, false);
		useEdgeButton.setLayoutData(gd_useEdgeButton);
		useEdgeButton.setText("Use edge Rails");

		final Composite composite_4 = new Composite(container, SWT.NONE);
		composite_4
				.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true));
		composite_4.setLayout(new GridLayout());

		final Composite composite_5 = new Composite(container, SWT.NONE);
		composite_5.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));
		final GridLayout gridLayout_5 = new GridLayout();
		gridLayout_5.marginHeight = 0;
		gridLayout_5.marginWidth = 0;
		composite_5.setLayout(gridLayout_5);

		final Link chooseManuallyLink = new Link(composite_5, SWT.NONE);
		chooseManuallyLink
				.setText("You can <a href=\"aaa\">add your Ruby installation</a> if it wasn't found automatically.");
		return container;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
		createButton(parent, IDialogConstants.OK_ID, "Choose", true);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(513, 309);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Choose Rails");
	}

}
