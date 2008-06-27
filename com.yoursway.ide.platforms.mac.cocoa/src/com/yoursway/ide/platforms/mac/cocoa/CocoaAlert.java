package com.yoursway.ide.platforms.mac.cocoa;

import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.Callback;
import org.eclipse.swt.internal.cocoa.NSAlert;
import org.eclipse.swt.internal.cocoa.NSString;
import org.eclipse.swt.internal.cocoa.NSWindow;
import org.eclipse.swt.internal.cocoa.OS;
import org.eclipse.swt.widgets.Shell;

public abstract class CocoaAlert {

	private static final int sel_alertDidEnd_returnCode_contextInfo_ = OS
			.sel_registerName("alertDidEnd:returnCode:contextInfo:");
	private final Shell parent;
	private NSAlert alert;

	private static void initClass() {
		Callback callback = new Callback(CocoaAlert.class, "delegateProc", 5);
		int proc = callback.getAddress();
		if (proc == 0)
			SWT.error(SWT.ERROR_NO_MORE_CALLBACKS);

		String className = "SWTAlertDelegate";
		int cls = OS.objc_allocateClassPair(OS.class_NSObject, className, 0);
		OS.class_addIvar(cls, "tag", OS.PTR_SIZEOF, (byte) (Math.log(OS.PTR_SIZEOF) / Math.log(2)),
				"i");
		OS.class_addMethod(cls, sel_alertDidEnd_returnCode_contextInfo_, proc, "@:@ii");
		OS.objc_registerClassPair(cls);
	}

	static {
		initClass();
	}

	public CocoaAlert(Shell parent) {
		this.parent = parent;
		alert = (NSAlert) new NSAlert().alloc().init();
	}

	public void setMessageText(String text) {
		alert.setMessageText(NSString.stringWith(text));
	}

	public void setInformativeText(String text) {
		alert.setInformativeText(NSString.stringWith(text));
	}

	protected void addButton(String title) {
		alert.addButtonWithTitle(NSString.stringWith(title));
	}

	public void openModal() {
		finished(openModalReturnButton());
	}

	public int openModalReturnButton() {
		return alert.runModal() - OS.NSAlertFirstButtonReturn;
	}

	public void open() {
		if (parent == null)
			throw new NullPointerException("parent shell is null");

		SWTAlertDelegate delegate = (SWTAlertDelegate) new SWTAlertDelegate().alloc().init();
		int ref = OS.NewGlobalRef(this);
		if (ref == 0)
			SWT.error(SWT.ERROR_NO_HANDLES);
		delegate.setTag(ref);

		alert.beginSheetModalForWindow(parent.view.window(), delegate,
				sel_alertDidEnd_returnCode_contextInfo_, 0);
	}

	static int delegateProc(int id, int sel, int arg0, int arg1, int arg2) {
		SWTAlertDelegate delegate = new SWTAlertDelegate(id);
		int ref = delegate.tag();
		CocoaAlert cocoaAlert = (CocoaAlert) OS.JNIGetObject(ref);
		OS.DeleteGlobalRef(ref);
		cocoaAlert.finished(arg1 - OS.NSAlertFirstButtonReturn);
		return 0;
	}

	protected abstract void finished(int button);

	public void dismiss() {
		new NSWindow(alert.window().id).close();
	}

}
