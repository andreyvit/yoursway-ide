package com.yoursway.experiments.birdseye.component;

import org.eclipse.swt.graphics.Point;

import com.yoursway.experiments.birdseye.model.Leaf;
import com.yoursway.experiments.birdseye.model.Node;

public class EmptyNode extends Node {

	@Override
	public Leaf pick(Point point) {
		return null;
	}

	@Override
	public double size() {
		return 1;
	}

}
