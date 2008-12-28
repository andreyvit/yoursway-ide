package com.yoursway.ide.application;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;

import com.yoursway.ide.application.view.impl.ApplicationCommands;
import com.yoursway.ide.application.view.impl.CommandExecutor;
import com.yoursway.ide.application.view.impl.MenuBuilder;
import com.yoursway.ide.application.view.impl.ApplicationCommands.NopCommand;

public class ApplicationMenu {

	private static final NopCommand NOP = new ApplicationCommands.NopCommand();
	private final Menu menu;

	public ApplicationMenu(Display display, CommandExecutor target) {
		this(new Shell(display), target);
	}

	public ApplicationMenu(Shell shell, CommandExecutor target) {
		if (target == null)
			throw new NullPointerException("target is null");
		MenuBuilder m = new MenuBuilder(shell, target);
		createFileMenu(m.submenu("File"));
		createEditMenu(m.submenu("Edit"));
		createHistoryMenu(m.submenu("History"));
		createAddMenu(m.submenu("Add"));
		createViewMenu(m.submenu("View"));
		createGoToMenu(m.submenu("Go To"));
		createRunMenu(m.submenu("Run"));
		createDebugMenu(m.submenu("Debug"));
		createWindowMenu(m.submenu("Window"));
		createHelpMenu(m.submenu("Help"));
		menu = m.getMenu();
	}

	private void createHelpMenu(MenuBuilder m) {
		m.item("YourSway IDE Help", "M1+?", NOP);
		m.item("Using YS IDE from Terminal", NOP);
		m.separator();
		MenuBuilder m1 = m.submenu("Ruby Resources");
		m1.item("Ruby", NOP);
		m1.item("Ruby on Rails", NOP);
		MenuBuilder m2 = m.submenu("Python Resources");
		m2.item("Python", NOP);
		m2.item("Django", NOP);
		m2.item("Google App Engine", NOP);
		MenuBuilder m3 = m.submenu("PHP Resources");
		m3.item("PHP", NOP);
		m3.item("Cake PHP", NOP);
		m3.item("Comparison of PHP Web Frameworks", NOP);
		MenuBuilder m4 = m.submenu("Web Design Resources");
		m4.item("HTML Reference", NOP);
		m4.item("CSS Reference", NOP);
		m4.item("Introduction to Grid Layouts", NOP);
		m4.item("Blueprint CSS Framework", NOP);
		m.separator();
		m.item("Latest News on YourSway Web Site", NOP);
		m.item("Send Feedback…", NOP);
	}

	private void createWindowMenu(MenuBuilder m) {
		m.item("Minimize", "M1+M", NOP);
		m.item("Zoom", "M1+M4+Z", NOP);
		m.separator();
		m.item("New Window Of “builder” On Another Screen", "M1+M4+ALT+N", NOP);
		m.item("Next Window Of “builder”", "M4+`", NOP);
		m.item("Previous Window Of “builder”", "M4+SHIFT+`", NOP);
		m.separator();
		m.checkbox("Pin “app/views/admin/users/_details.rhtml”", "M1+K", NOP);
		m.separator();
		m.item("Bring All to Front", NOP);
	}

	private void createDebugMenu(MenuBuilder m) {
		m.item("Show Debugger", "M1+M4+ALT+D", NOP);
		m.separator();
		m.item("Step Into", "F5", NOP);
		m.item("Step Over", "F6", NOP);
		m.item("Step Out", "F7", NOP);
		m.separator();
		m.item("Resume", "F8", NOP);
		m.item("Run To Line", "SHIFT+F5", NOP);
		m.item("Restart", "M1+SHIFT+F7", NOP);
		m.separator();
		m.item("Toggle Breakpoint", "M1+SHIFT+B", NOP);
		m.separator();
		m.item("Inspect Variable", "M1+J", NOP);
	}

	private void createRunMenu(MenuBuilder m) {
		m.item("Run “builder/server” in Mongrel", "M1+ALT+1", NOP);
		createRailsEnvironmentsMenu(m.submenu("Environment"));
		m.checkbox("Enable Debugging And Breakpoints", NOP).setSelection(true);
		m.separator();

		m.item("Migrate “builder/server”'s Schema", NOP);
		createRakeTasksMenu(m.submenu("Other Rake Tasks"));
		m.separator();

		m.item("Run “builder/client/main.py” in Python", "M1+ALT+2", NOP);
		createCommandLineArgumentsMenu(m.submenu("Set Command Line Arguments"));
		createWorkingDirectoryMenu(m.submenu("Set Working Directory"),
				"builder/client");
		m.item("Change Environment Variables…", NOP);
		m.checkbox("Enable Debugging And Breakpoints", NOP).setSelection(true);
		m.separator();

		m.item("Run “builder/pinger/main.py” in Python", "M1+ALT+3", NOP);
		createCommandLineArgumentsMenu(m.submenu("Set Command Line Arguments"));
		createWorkingDirectoryMenu(m.submenu("Set Working Directory"),
				"builder/pinger");
		m.item("Change Environment Variables…", NOP);
		m.checkbox("Enable Debugging And Breakpoints", NOP).setSelection(true);
		m.separator();

		createRubyVersionsMenu(m.submenu("Use Ruby Version"));
		createRailsVersionsMenu(m.submenu("Use Rails Version"));
		createPythonVersionsMenu(m.submenu("Use Python Version"));
		createRubyWebServersMenu(m.submenu("Run Ruby Web Applications In"));
	}

	private void createRubyWebServersMenu(MenuBuilder m) {
		m.radio("Mongrel", NOP).setSelection(true);
		m.radio("WEBrick", NOP);
		m.radio("Thin", NOP);
		m.radio("Phusion Passenger", NOP);
	}

	private void createPythonVersionsMenu(MenuBuilder m) {
		m.radio("Python 2.5", NOP).setSelection(true);
		m.radio("Python 2.4", NOP);
	}

	private void createRailsVersionsMenu(MenuBuilder m) {
		m.radio("Rails 2.2", NOP).setSelection(true);
		m.radio("Rails 2.1.2", NOP);
		m.radio("Rails 2.1.1", NOP);
		m.separator();
		m.item("Install Edge Rails", NOP);
	}

	private void createRubyVersionsMenu(MenuBuilder m) {
		m.radio("Ruby 1.8.6 in /usr/bin", NOP).setSelection(true);
		m.radio("Ruby 1.8.5 in /usr/local/bin", NOP);
		m.radio("Ruby 1.9.0 in /opt/local/bin", NOP);
		m.radio("Ruby 1.7.3 in /sw/ruby/bin", NOP);
	}

	private void createWorkingDirectoryMenu(MenuBuilder m, String name) {
		m.radio("Script's Directory (" + name + ")", NOP).setSelection(true);
		m.separator();
		m.item("Other…", NOP);
	}

	private void createCommandLineArgumentsMenu(MenuBuilder m) {
		m.radio("None", NOP);
		m.separator();
		m.radio("-h builder.yoursway.com", NOP).setSelection(true);
		m.radio("-h localhost", NOP);
		m.separator();
		m.item("Other…", NOP);
	}

	private void createRakeTasksMenu(MenuBuilder m) {
		m.item("db:schema:dump", NOP);
		m.item("db:schema:load", NOP);
		m.item("routes", NOP);
	}

	private void createRailsEnvironmentsMenu(MenuBuilder m) {
		m.item("development", NOP);
		m.item("production", NOP);
		m.item("test", NOP);
	}

	private void createGoToMenu(MenuBuilder m) {
		m.item("Go To Line…", "M1+L", NOP);
		m.separator();
		m.item("Go To Declaration", "F3", NOP);
		m.item("Go To Super Implementation", "M4+S", NOP);
		m.item("Show All References to “foo”", "M1+R", NOP);
		m.separator();
		m.item("Open “foo.rb” in Single File Editor", "M1+E", NOP);
		m.separator();
		m.item("All Errors", "M1+ALT+M4+E", NOP);
		m.item("All Warnings", "M1+ALT+M4+W", NOP);
		m.item("All Test Failures", "M1+ALT+M4+F", NOP);
		m.separator();
		m.item("Project Editor", "M1+ALT+M4+P", NOP);
		m.item("URL Routing Editor", "M1+ALT+M4+U", NOP);
		m.item("Log Viewer", "M1+ALT+M4+L", NOP);
		m.item("Type Hierarchy", "M1+ALT+M4+T", NOP);
		m.separator();
		m.item("Older File In History", "ALT+`", NOP);
		m.item("Newer File In History", "ALT+SHIFT+`", NOP);
	}

	private void createViewMenu(MenuBuilder m) {
		m.checkbox("Soft Wrap Long Lines", "M1+ALT+W", NOP).setSelection(true);
		m.checkbox("Show Invisibles", "M1+ALT+I", NOP);
		m.separator();
		m.item("Bird's Eye View — hold to activate", "F1", NOP);
		m.item("Collapsed View — hold to activate", "F4", NOP);
		m.separator();
		m.item("Popup Type Hierarchy On Selected Item", "M1+T", NOP);
		m.separator();
		m.item("Switch HTML Source <-> Editable Preview", "M1+SHIFT+U", NOP);
		m.checkbox("Show Both Source And Editable Preview", "M1+ALT+SHIFT+U",
				NOP).setSelection(true);
		m.separator();
		m.radio("1 Full-Size Editor", "M1+SHIFT+1", NOP).setSelection(true);
		m.radio("2 Full-Size Editors Left To Right", "M1+SHIFT+2", NOP);
		m.radio("2 Full-Size Editors Top To Bottom", "M1+SHIFT+2 M1+SHIFT+2",
				NOP);
		m.radio("4 Full-Size Editors", "M1+SHIFT+4", NOP);
		m.separator();
		m.radio("Other Editors Are Small", "M1+SHIFT+E", NOP)
				.setSelection(true);
		m.radio("Other Editors Are Tiny", "M1+SHIFT+E", NOP);
	}

	private void createAddMenu(MenuBuilder m) {
		m.item("New File", "M1+N", NOP);
		m.separator();
		m.item("New Controller", "CTRL+ALT+SHIFT+C", NOP);
		m.item("New Action", "CTRL+ALT+SHIFT+A", NOP);
		m.item("New Model", "CTRL+ALT+SHIFT+M", NOP);
		m.item("New View", "CTRL+ALT+SHIFT+V", NOP);
		m.item("New Test", "CTRL+ALT+SHIFT+T", NOP);
	}

	private void createHistoryMenu(MenuBuilder m) {
		m.item("Undo", "M1+Z", NOP);
		m.item("Redo", "M1+SHIFT+Z", NOP);
		m.item("Show History", "M1+Y", NOP);
		m.separator();
		m.item("Review && Commit Changes", "M1+SHIFT+C", NOP);
		m.separator();
		createSwitchToBranchMenu(m.submenu("Switch to Branch"));
		createMergeChangesFromBranchMenu(m.submenu("Merge Changes from Branch"));
		m.separator();
		createPullMenu(m.submenu("Pull && Review Changes from"));
		createPullMenu(m.submenu("Push To"));
		m.separator();
		m.item("“git clone” as Project…", NOP);
	}

	private void createSwitchToBranchMenu(MenuBuilder m) {
		m.radio("master", NOP).setSelection(true);
		m.radio("ide-toolbar", NOP);
		m.radio("buttons-fix", NOP);
		m.separator();
		m.item("Create new branch from “master”…", "M1+ALT+B", NOP);
	}

	private void createMergeChangesFromBranchMenu(MenuBuilder m) {
		m.item("master", NOP);
		m.item("ide-toolbar", NOP);
		m.item("buttons-fix", NOP);
	}

	private void createPullMenu(MenuBuilder m) {
		m.item("origin", NOP);
		m.item("fourdman", NOP);
		m.item("leontiy", NOP);
		m.item("burchik", NOP);
	}

	void createFileMenu(final MenuBuilder m) {
		m.item("New", "M1+N", new ApplicationCommands.NewDocumentCommand());
		m.item("Open…", NOP);
		createRecentFilesMenu(m.submenu("Open Recent"));
		m.item("Close", "M1+W", new ApplicationCommands.CloseDocumentCommand());
		m.item("Close All", "M1+SHIFT+W", NOP);

		m.separator();

		m.item("Rename/Move", "M1+SHIFT+R", NOP);
		m.item("Duplicate", "M1+SHIFT+D", NOP);

		m.separator();

		m.item("Reveal “foo_controller.rb” in Finder", "M1+M4+R", NOP);

		m.separator();

		createNewProjectMenu(m);

		m.separator();

		m.item("Open Project...", "M1+M4+O",
				new ApplicationCommands.OpenProjectCommand());
		createRecentProjectsMenu(m.submenu("Open Recent Project"));
		m.item("Close Project", "M1+M4+W",
				new ApplicationCommands.CloseProjectCommand());

		m.separator();

		m.item("Save All Changes To Disk", "M1+S",
				new ApplicationCommands.SaveFileAsCommand());

		m.separator();

		m.item("Page Setup…", "M1+SHIFT+P", NOP);
		m.item("Print…", "M1+P", NOP);
	}

	void createEditMenu(final MenuBuilder m) {
		m.item("Cut", "M1+X", NOP);
		m.item("Copy", "M1+C", NOP);
		m.item("Paste", "M1+V", NOP);
		m.item("Paste Previous", "M1+SHIFT+V", NOP);

		m.separator();

		m.item("Delete Line", "M1+D", NOP);
		m.item("Edit Each Line In Selection", "M1+ALT+A", NOP);
		m.item("Comment/Uncomment Line", "M1+/", NOP);

		m.separator();

		m.item("Move Line Up", "M1+M4+ARROW_UP", NOP);
		m.item("Move Line Down", "M1+M4+ARROW_DOWN", NOP);
		m.item("Duplicate Line Above", "M1+ALT+M4+ARROW_UP", NOP);
		m.item("Duplicate Line Below", "M1+ALT+M4+ARROW_DOWN", NOP);

		m.separator();

		m.item("15 Lines Up / Previous Snippet", "M1+ARROW_UP", NOP);
		m.item("15 Lines Down / Next Snippet", "M1+ARROW_DOWN", NOP);

		m.separator();

		m.item("Select Word", "M4+W", NOP);
		m.item("Select All", "M1+A", NOP);

		m.separator();

		m.item("Indent", "M1+]", NOP);
		m.item("Dedent", "M1+[", NOP);
		m.item("Reindent Line", "M1+I", NOP);
		m.item("Reformat", "M1+SHIFT+F", NOP);
		m.checkbox("Reformat As You Type", "M1+SHIFT+ALT+F", NOP).setSelection(
				true);

		m.separator();

		m.item("Complete Word", "TAB", NOP);

		m.separator();

		m.item("Filter Through OS Command…", "M1+ALT+R", NOP);

		m.separator();

		m.item("File/Text Search & Replace", "M1+F", NOP);
		createFindMenu(m.submenu("Find"));
	}

	private void createFindMenu(MenuBuilder m) {
		m.item("Find Next", "M1+G", NOP);
		m.item("Find Previous", "M1+SHIFT+G", NOP);
		m.item("Use Selection for Find", "M1+E", NOP);
	}

	private void createNewProjectMenu(final MenuBuilder m) {
		MenuBuilder submenu = m.submenu("New Project", "M1+SHIFT+N");
		submenu.item("Rails Web Application", NOP);
		submenu.item("Rails Plugin", NOP);
		submenu.item("Ruby Application", NOP);
		submenu.separator();
		submenu.item("Django Web Application", NOP);
		submenu.item("Pylons Web Application (???)", NOP);
		submenu.item("Google App Engine Application", NOP);
		submenu.item("Python Application", NOP);
		submenu.separator();
		submenu.item("PHP Web Application", NOP);
		submenu.item("Cake PHP Web Application", NOP);
		submenu.separator();
		submenu.item("HTML/CSS Web Site", NOP);
	}

	private void createRecentFilesMenu(final MenuBuilder m) {
		m.item("~/TODO.txt", NOP);
		m.separator();
		m.item("Clear Menu", NOP);
	}

	private void createRecentProjectsMenu(final MenuBuilder m) {
		m.item("~/Projects/builder", NOP);
		m.item("~/Projects/site", NOP);
		m.separator();
		m.item("Clear Menu", NOP);
	}

	public Menu getMenu() {
		return menu;
	}

}
