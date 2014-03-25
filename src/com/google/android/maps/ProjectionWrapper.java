package com.google.android.maps;

import android.graphics.Point;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IProjection;

public class ProjectionWrapper implements Projection {
	private final IProjection wrapped;

	public ProjectionWrapper(IProjection wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public Point toPixels(IGeoPoint in, Point out) {
		return wrapped.toPixels(in, out);
	}

	@Override
	public GeoPoint fromPixels(int x, int y) {
		return new GeoPoint(wrapped.fromPixels(x, y));
	}

	@Override
	public float metersToEquatorPixels(float meters) {
		return wrapped.metersToEquatorPixels(meters);
	}

	@Override
	public IGeoPoint getNorthEast() {
		return wrapped.getNorthEast();
	}

	@Override
	public IGeoPoint getSouthWest() {
		return wrapped.getSouthWest();
	}

	@Override
	public Point toPixels(GeoPoint in, Point out) {
		return wrapped.toPixels(in, out);
	}
}
