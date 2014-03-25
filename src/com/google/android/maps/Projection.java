package com.google.android.maps;

import android.graphics.Point;
import org.microg.annotation.OriginalApi;
import org.osmdroid.api.IProjection;

public interface Projection extends IProjection {
	@OriginalApi
	GeoPoint fromPixels(int x, int y);

	@OriginalApi
	float metersToEquatorPixels(float meters);

	@OriginalApi
	Point toPixels(GeoPoint in, Point out);
}
