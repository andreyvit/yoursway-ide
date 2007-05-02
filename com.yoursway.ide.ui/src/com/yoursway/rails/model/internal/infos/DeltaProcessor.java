package com.yoursway.rails.model.internal.infos;


public class DeltaProcessor {
    //    
    //    public void reconcile(RailsDeltaBuilder deltaBuilder, IResourceDelta delta) {
    //        controllersFolder = null;
    //        open();
    //        IResourceDelta folderDelta = delta.findMember(new Path("app/controllers"));
    //        if (folderDelta == null)
    //            return;
    //        switch (folderDelta.getKind()) {
    //        case IResourceDelta.ADDED:
    //        case IResourceDelta.REMOVED:
    //            refresh();
    //            break;
    //        case IResourceDelta.CHANGED:
    //            reconcileChildDeltas(deltaBuilder, delta);
    //            break;
    //        }
    //    }
    //    
    //    private void reconcileChildDeltas(RailsDeltaBuilder deltaBuilder, IResourceDelta parentDelta) {
    //        for (IResourceDelta delta : parentDelta.getAffectedChildren()) {
    //            IResource resource = delta.getResource();
    //            switch (delta.getKind()) {
    //            case IResourceDelta.ADDED:
    //                switch (resource.getType()) {
    //                case IResource.FILE:
    //                    if (canAddControllerFor((IFile) resource))
    //                        addController((IFile) resource);
    //                    break;
    //                case IResource.FOLDER:
    //                    locateControllersInFolder((IFolder) resource);
    //                    break;
    //                }
    //                break;
    //            case IResourceDelta.REMOVED:
    //                switch (resource.getType()) {
    //                case IResource.FILE:
    //                    IRailsController controller = findControllerFor((IFile) resource);
    //                    if (controller != null)
    //                        removeController(deltaBuilder, controller, (IFile) resource);
    //                    break;
    //                case IResource.FOLDER:
    //                    locateControllersInFolder((IFolder) resource);
    //                    break;
    //                }
    //                break;
    //            case IResourceDelta.CHANGED:
    //                switch (resource.getType()) {
    //                case IResource.FILE:
    //                    RailsController controller = findControllerFor((IFile) resource);
    //                    if (controller != null)
    //                        controller.reconcile(deltaBuilder, delta);
    //                    break;
    //                case IResource.FOLDER:
    //                    reconcileChildDeltas(deltaBuilder, delta);
    //                    break;
    //                }
    //                break;
    //            }
    //        }
    //    }
    
}
