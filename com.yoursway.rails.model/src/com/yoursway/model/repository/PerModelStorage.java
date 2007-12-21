/**
 * 
 */
package com.yoursway.model.repository;

import java.util.LinkedList;

import com.yoursway.model.resource.internal.ISnapshotBuilder;
import com.yoursway.model.timeline.PointInTime;
import com.yoursway.model.tracking.IMapSnapshot;

class PerModelStorage {
    
    private class Tuple {
        PointInTime point;
        IMapSnapshot snapshot;
        
        public Tuple(PointInTime point, IMapSnapshot snapshot) {
            super();
            this.point = point;
            this.snapshot = snapshot;
        }
        
    }
    
    private class ProxySnapshot implements IMapSnapshot, ISnapshotInProgress {
        
        private IMapSnapshot fullSnapshot;
        
        private final Object flag = new Object();
        
        public ProxySnapshot() {
            fullSnapshot = null;
        }
        
        public <T> T get(IHandle<T> handle) {
            synchronized (flag) {
                while (fullSnapshot == null) {
                    try {
                        flag.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            return fullSnapshot.get(handle);
        }
        
        public void setFullSnapshot(IMapSnapshot snapshot) {
            fullSnapshot = snapshot;
            synchronized (flag) {
                flag.notifyAll();
            }
        }
        
        public void setPointInTime(PointInTime pit) {
            throw new UnsupportedOperationException();
        }
        
    }
    
    private final LinkedList<Tuple> snapshots = new LinkedList<Tuple>();
    
    public PerModelStorage(Class<?> modelRoot) {
    }
    
    public IMapSnapshot getLast(PointInTime point) {
        return findSnapshot(point, true);
    }
    
    private IMapSnapshot findSnapshot(PointInTime point, boolean allowNotComplete) {
        IMapSnapshot winner = null;
        synchronized (snapshots) { //TODO, insert binary search
            for (Tuple t : snapshots) {
                if (!allowNotComplete && t.snapshot instanceof ISnapshotInProgress)
                    break;
                if (t.point.compareTo(point) <= 0)
                    winner = t.snapshot;
                else
                    break;
            }
        }
        return winner;
    }
    
    public IMapSnapshot getLastAccessible(PointInTime point) {
        return findSnapshot(point, false);
    }
    
    synchronized public void pushSnapshot(PointInTime point, ISnapshotBuilder snapshotBuilder) {
        ProxySnapshot proxySnapshot = new ProxySnapshot();
        synchronized (snapshots) {
            snapshots.add(new Tuple(point, proxySnapshot));
        }
        // this may execute long
        IMapSnapshot snapshot = snapshotBuilder.getSnapshot();
        if (snapshot == null)
            throw new RuntimeException("SnapshotBuilder returned null snapshot!");
        synchronized (snapshots) {
            snapshots.removeLast();
            snapshots.add(new Tuple(point, snapshot));
            proxySnapshot.setFullSnapshot(snapshot);
        }
    }
}