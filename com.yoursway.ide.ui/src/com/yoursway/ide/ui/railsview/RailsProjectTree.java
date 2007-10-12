package com.yoursway.ide.ui.railsview;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.yoursway.ide.ui.railsview.presentation.ElementPresenterFactory;
import com.yoursway.ide.ui.railsview.presentation.IContextMenuContext;
import com.yoursway.ide.ui.railsview.presentation.IElementPresenter;
import com.yoursway.ide.ui.railsview.presentation.IPresenterFactory;
import com.yoursway.ide.ui.railsview.presentation.IPresenterOwner;
import com.yoursway.ide.ui.railsview.presentation.IProvidesTreeItem;
import com.yoursway.ide.ui.railsview.presentation.PresentersBasedProvider;
import com.yoursway.rails.core.projects.RailsProject;

public class RailsProjectTree implements IPresenterOwner {

	private final IPresenterFactory presenterFactory = new ElementPresenterFactory(
			this);

	private final PresentersBasedProvider infoProvider = new PresentersBasedProvider(
			presenterFactory);

	private final Tree tree;

	private final PublicMorozovTreeViewer viewer;

	private RailsProject currentRailsProject;

	private final MenuManager contextMenuManager;

	private final IRailsProjectTreeOwner owner;

	public RailsProjectTree(Composite parent, FormToolkit formToolkit,
			IRailsProjectTreeOwner owner) {
		this.owner = owner;
		tree = formToolkit.createTree(parent, SWT.MULTI | SWT.VERTICAL
				| SWT.V_SCROLL);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		viewer = new PublicMorozovTreeViewer(tree);
		viewer.setContentProvider(infoProvider);
		viewer.setLabelProvider(infoProvider);
		viewer.expandToLevel(null, 1);

		// configureGradientColors();

		contextMenuManager = new MenuManager("#PopupMenu");
		contextMenuManager.setRemoveAllWhenShown(true);
		contextMenuManager.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				fillContextMenu(manager);
			}
		});
		tree.setMenu(contextMenuManager.createContextMenu(tree));

		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				handleDoubleClick(event);
			}
		});

		tree.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.F2 && e.stateMask == 0)
					doRename(((StructuredSelection) viewer.getSelection())
							.getFirstElement());
			}

			public void keyReleased(KeyEvent e) {
			}

		});

		viewer.addTreeListener(new ITreeViewerListener() {

			public void treeCollapsed(final TreeExpansionEvent event) {
				PlatformUI.getWorkbench().getDisplay().asyncExec(
						new Runnable() {
							public void run() {
								viewer.refresh(event.getElement());
							}
						});
			}

			public void treeExpanded(final TreeExpansionEvent event) {
				PlatformUI.getWorkbench().getDisplay().asyncExec(
						new Runnable() {
							public void run() {
								viewer.refresh(event.getElement());
							}
						});
			}
		});

		tree.addListener(SWT.MeasureItem, new Listener() {
			public void handleEvent(Event event) {
				TreeItem item = (TreeItem) event.item;
				Object data = item.getData();
				if (data != null)
					infoProvider.measureItem(item, data, event);
			}
		});
		tree.addListener(SWT.EraseItem, new Listener() {
			public void handleEvent(Event event) {
				TreeItem item = (TreeItem) event.item;
				Object data = item.getData();
				if (data != null)
					infoProvider.eraseItem(item, data, event);
			}
		});
		tree.addListener(SWT.PaintItem, new Listener() {
			public void handleEvent(Event event) {
				TreeItem item = (TreeItem) event.item;
				Object data = item.getData();
				if (data != null)
					infoProvider.paintItem(item, data, event);
			}
		});
		LocalSelectionTransfer transfer = LocalSelectionTransfer.getTransfer();
		viewer.addDragSupport(DND.DROP_MOVE | DND.DROP_COPY,
				new Transfer[] { transfer }, new DragSourceListener() {

					public void dragFinished(DragSourceEvent event) {
					}

					public void dragSetData(DragSourceEvent event) {
					}

					public void dragStart(DragSourceEvent event) {
						LocalSelectionTransfer.getTransfer().setSelection(
								viewer.getSelection());
					}

				});
		viewer.addDropSupport(DND.DROP_MOVE | DND.DROP_COPY,
				new Transfer[] { transfer }, new DropTargetListener() {

					public void dragEnter(DropTargetEvent event) {
					}

					public void dragLeave(DropTargetEvent event) {
					}

					public void dragOperationChanged(DropTargetEvent event) {
					}

					public void dragOver(DropTargetEvent event) {
					}

					public void drop(DropTargetEvent event) {
					}

					public void dropAccept(DropTargetEvent event) {

						ISelection selection = LocalSelectionTransfer
								.getTransfer().getSelection();
						MessageDialog.openInformation(null, "Medved",
								"Preved! " + selection + "\n" + event.widget
										+ "\n" + event.item.getData());
					}

				});
	}

	protected void handleDoubleClick(DoubleClickEvent event) {
		ISelection selection = viewer.getSelection();
		Object obj = ((IStructuredSelection) selection).getFirstElement();
		if (obj != null) {
			final TreeItem treeItem = viewer.getCorrespondingWidget(obj);
			presenterFactory.createPresenter(obj).handleDoubleClick(
					new IProvidesTreeItem() {

						public TreeItem getTreeItem() {
							return treeItem;
						}

					});
		}
	}

	protected void fillContextMenu(final IMenuManager manager) {
		ISelection selection = viewer.getSelection();
		final Object obj = ((IStructuredSelection) selection).getFirstElement();
		if (obj != null) {
			final IElementPresenter presenter = presenterFactory
					.createPresenter(obj);
			manager.add(new Action("Rename") {

				@Override
				public void run() {
					doRename(presenter);
				}

			});

			final TreeItem treeItem = viewer.getCorrespondingWidget(obj);

			IContextMenuContext context = new IContextMenuContext() {

				public IMenuManager getMenuManager() {
					return manager;
				}

				public TreeItem getTreeItem() {
					return treeItem;
				}

			};
			presenter.fillContextMenu(context);
		}
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	public void setVisibleProject(RailsProject project) {
		currentRailsProject = project;
		viewer.setInput(currentRailsProject);
	}

	public void refresh() {
		if (currentRailsProject != null)
			viewer.refresh(currentRailsProject);
	}

	public MenuManager getContextMenuManager() {
		return contextMenuManager;
	}

	public ISelectionProvider getSelectionProvider() {
		return viewer;
	}

	public IWorkbenchPage getWorkbenchPage() {
		return owner.getWorkbenchPage();
	}

	public void setFocus() {
		tree.setFocus();
	}

	private void doRename(Object obj) {
		final IElementPresenter presenter = presenterFactory
				.createPresenter(obj);
		TreeItem[] selItems = tree.getSelection();
		TreeItem treeItem = selItems[0];

	}

	public Tree getTree() {
		return tree;
	}

	public TreeViewer getTreeViewer() {
		return viewer;
	}

}
