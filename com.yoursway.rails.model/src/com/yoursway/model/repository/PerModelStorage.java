/**
 * 
 */
package com.yoursway.model.repository;

import java.util.LinkedList;

import com.yoursway.model.resource.internal.ISnapshotBuilder;
import com.yoursway.model.timeline.PointInTime;

class PerModelStorage {
    
    private class Tuple {
        PointInTime point;
        ISnapshot snapshot;
        
        public Tuple(PointInTime point, ISnapshot snapshot) {
            this.point = point;
            this.snapshot = snapshot;
        }
        
    }
    
    private class FakeProxySnapshot implements ISnapshot {
        
        private ISnapshot fullSnapshot;
        
        private final Object flag = new Object();
        
        public FakeProxySnapshot() {
            fullSnapshot = null;
        }
        
        public ISnapshot getFullSnapshot() {
            synchronized (flag) {
                while (fullSnapshot == null) {
                    try {
                        flag.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            return fullSnapshot;
        }
        
        public void setFullSnapshot(ISnapshot snapshot) {
            fullSnapshot = snapshot;
            synchronized (flag) {
                flag.notifyAll();
            }
        }
        
    }
    
    private final LinkedList<Tuple> snapshots = new LinkedList<Tuple>();
    
    public PerModelStorage(Class<?> modelRoot) {
    }
    
    public ISnapshot getLast(PointInTime point) {
        ISnapshot snapshot = findSnapshot(point, true);
        if (snapshot instanceof FakeProxySnapshot) {
            FakeProxySnapshot fakeProxySnapshot = (FakeProxySnapshot) snapshot;
            return fakeProxySnapshot.getFullSnapshot();
        }
        return snapshot;
    }
    
    public ISnapshot getLastAccessible(PointInTime point) {
        return findSnapshot(point, false);
    }
    
    private ISnapshot findSnapshot(PointInTime point, boolean allowNotComplete) {
        ISnapshot winner = null;
        synchronized (snapshots) { //TODO, use binary search
            for (Tuple t : snapshots) {
                if (!allowNotComplete && t.snapshot instanceof FakeProxySnapshot)
                    break;
                if (t.point.compareTo(point) <= 0)
                    winner = t.snapshot;
                else
                    break;
            }
        }
        return winner;
    }
    
    synchronized public void pushSnapshot(PointInTime point, ISnapshotBuilder snapshotBuilder) {
        FakeProxySnapshot proxySnapshot = new FakeProxySnapshot();
        synchronized (snapshots) {
            snapshots.add(new Tuple(point, proxySnapshot));
        }
        // this may execute long
        ISnapshot snapshot = snapshotBuilder.buildSnapshot();
        if (snapshot == null)
            throw new RuntimeException("SnapshotBuilder returned null snapshot!");
        synchronized (snapshots) {
            snapshots.removeLast();
            snapshots.add(new Tuple(point, snapshot));
            proxySnapshot.setFullSnapshot(snapshot);
        }
    }
}