package com.yoursway.ide.preferences;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;

import org.eclipse.jface.window.Window;
import org.eclipse.osgi.framework.internal.core.BundleURLConnection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;

import com.yoursway.ide.ui.Activator;

public class PreferencesDialog extends Window {
    
    private final static class ApplyColorSchemeListener extends SelectionAdapter {
        private final ColorScheme colorScheme;
        
        public ApplyColorSchemeListener(ColorScheme colorScheme) {
            this.colorScheme = colorScheme;
        }
        
        @Override
        public void widgetSelected(SelectionEvent e) {
            ColorSchemeManager.apply(colorScheme);
        }
    }
    
    protected PreferencesDialog(Shell parentShell) {
        super(parentShell);
    }
    
    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("YourSway IDE Preferences");
    }
    
    //    private static final List<ColorScheme> colorSchemes = new ArrayList<ColorScheme>();
    
    //    
    //    private static void add(String name) {
    //        colorSchemes.add(new ColorScheme(name));
    //    }
    
    @Override
    protected Control createContents(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout());
        
        Group group = new Group(composite, SWT.NONE);
        group.setText("Color Scheme");
        group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        group.setLayout(new GridLayout());
        
        for (ColorScheme colorScheme : ColorSchemeManager.getColorSchemes()) {
            Button applyButton = new Button(group, SWT.NONE);
            applyButton.setText(colorScheme.getName());
            applyButton.addSelectionListener(new ApplyColorSchemeListener(colorScheme));
        }
        
        Link link = new Link(group, SWT.NONE);
        link.setText("You can <a>save current settings</a> as a new color scheme.");
        link.addSelectionListener(new SelectionAdapter() {
            @SuppressWarnings("restriction")
            @Override
            public void widgetSelected(SelectionEvent e) {
                Bundle bundle = Activator.getDefault().getBundle();
                String location = bundle.getLocation();
                File localFolder = null;
                try {
                    URL entry = bundle.getEntry("resources/color_schemes");
                    BundleURLConnection conn = (BundleURLConnection) entry.openConnection();
                    URL localURL = conn.getLocalURL();
                    String path = localURL.getPath();
                    localFolder = new File(path);
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                FileDialog fileDialog = new FileDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                        .getShell(), SWT.SAVE);
                if (localFolder != null && localFolder.exists())
                    fileDialog.setFilterPath(localFolder.getPath() + "/");
                fileDialog.setFileName("new_scheme.properties");
                String result = fileDialog.open();
                if (result == null)
                    return;
                File resultFile = new File(result);
                String newName = resultFile.getName();
                if (resultFile.exists()) {
                    ColorScheme cs = null;
                    try {
                        cs = ColorSchemeManager.loadColorScheme(new FileInputStream(resultFile));
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }
                    if (cs != null)
                        newName = cs.getName();
                }
                ColorScheme currentScheme = ColorSchemeManager.getCurrentScheme();
                Properties properties = new Properties();
                properties.putAll(currentScheme.getSettings());
                properties.put("name", newName);
                try {
                    
                    OutputStream os = new FileOutputStream(result);
                    properties.store(os, "Mega Comments");
                    os.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                
            }
        });
        
        return composite;
    }
    
    public static void show() {
        PreferencesDialog dialog = new PreferencesDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                .getShell());
        dialog.open();
    }
    
}
