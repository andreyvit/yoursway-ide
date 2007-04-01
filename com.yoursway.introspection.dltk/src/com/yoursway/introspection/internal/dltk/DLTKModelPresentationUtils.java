package com.yoursway.introspection.internal.dltk;

import org.eclipse.dltk.core.IModelElementDelta;

public class DLTKModelPresentationUtils {

    public static void appendDeltaFlags(StringBuilder result, IModelElementDelta delta) {
        int flags = delta.getFlags();
        if ((flags & IModelElementDelta.F_ADDED_TO_BUILDPATH) != 0)
            result.append(' ').append("F_ADDED_TO_BUILDPATH");
        if ((flags & IModelElementDelta.F_ARCHIVE_CONTENT_CHANGED) != 0)
            result.append(' ').append("F_ARCHIVE_CONTENT_CHANGED");
        if ((flags & IModelElementDelta.F_BUILDPATH_CHANGED) != 0)
            result.append(' ').append("F_BUILDPATH_CHANGED");
        if ((flags & IModelElementDelta.F_CHILDREN) != 0)
            result.append(' ').append("F_CHILDREN");
        if ((flags & IModelElementDelta.F_CLOSED) != 0)
            result.append(' ').append("F_CLOSED");
        if ((flags & IModelElementDelta.F_CONTENT) != 0)
            result.append(' ').append("F_CONTENT");
        if ((flags & IModelElementDelta.F_FINE_GRAINED) != 0)
            result.append(' ').append("F_FINE_GRAINED");
        if ((flags & IModelElementDelta.F_MODIFIERS) != 0)
            result.append(' ').append("F_MODIFIERS");
        if ((flags & IModelElementDelta.F_MOVED_FROM) != 0)
            result.append(' ').append("F_MOVED_FROM");
        if ((flags & IModelElementDelta.F_MOVED_TO) != 0)
            result.append(' ').append("F_MOVED_TO");
        if ((flags & IModelElementDelta.F_OPENED) != 0)
            result.append(' ').append("F_OPENED");
        if ((flags & IModelElementDelta.F_PRIMARY_RESOURCE) != 0)
            result.append(' ').append("F_PRIMARY_RESOURCE");
        if ((flags & IModelElementDelta.F_PRIMARY_WORKING_COPY) != 0)
            result.append(' ').append("F_PRIMARY_WORKING_COPY");
        if ((flags & IModelElementDelta.F_REMOVED_FROM_BUILDPATH) != 0)
            result.append(' ').append("F_REMOVED_FROM_BUILDPATH");
        if ((flags & IModelElementDelta.F_REORDER) != 0)
            result.append(' ').append("F_REORDER");
        if ((flags & IModelElementDelta.F_SUPER_TYPES) != 0)
            result.append(' ').append("F_SUPER_TYPES");
    }

    public static String deltaKindToString(IModelElementDelta delta) {
        switch (delta.getKind()) {
        case IModelElementDelta.ADDED:
            return "ADDED";
        case IModelElementDelta.REMOVED:
            return "REMOVED";
        case IModelElementDelta.CHANGED:
            return "CHANGED";
        default:
            return "UNKNOWN";
        }
    }
    
}
