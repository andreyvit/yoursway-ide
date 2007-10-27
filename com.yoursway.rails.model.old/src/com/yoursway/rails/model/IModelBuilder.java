package com.yoursway.rails.model;

public interface IModelBuilder {

	ISnapshot buildSnapshot(PointInTime pit);
	
}
