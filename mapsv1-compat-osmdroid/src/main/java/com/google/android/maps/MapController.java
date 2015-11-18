package com.google.android.maps;

import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import org.microg.annotation.OriginalApi;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;

@OriginalApi
public class MapController implements IMapController, View.OnKeyListener {
	private final IMapController wrapped;

	public MapController(IMapController wrapped) {
		this.wrapped = wrapped;
	}

	@OriginalApi
	public void animateTo(GeoPoint point) {
		wrapped.animateTo(point);
	}

	@Override
	public void animateTo(IGeoPoint geoPoint) {
		wrapped.animateTo(geoPoint);
	}

	@OriginalApi
	public void animateTo(GeoPoint point, Message message) {
		wrapped.animateTo(point);
		// TODO: delay message
		if (message != null) {
			message.sendToTarget();
		}
	}

	@OriginalApi
	public void animateTo(GeoPoint point, Runnable runnable) {
		wrapped.animateTo(point);
		// TODO: delay runnable
		runnable.run();
	}

	@OriginalApi
	public boolean onKey(View view, int keyCode, KeyEvent event) {
		// TODO: handle onKey
		return false;
	}

	@OriginalApi
	@Override
	public void scrollBy(int x, int y) {
		wrapped.scrollBy(x, y);
	}

	@OriginalApi
	public void setCenter(GeoPoint point) {
		wrapped.setCenter(point);
	}

	@Override
	public void setCenter(IGeoPoint point) {
		wrapped.setCenter(point);
	}

	@OriginalApi
	@Override
	public int setZoom(int zoomLevel) {
		return wrapped.setZoom(zoomLevel);
	}

	@OriginalApi
	@Override
	public void stopAnimation(boolean jumpToFinish) {
		wrapped.stopAnimation(jumpToFinish);
	}

	@OriginalApi
	@Override
	public void stopPanning() {
		wrapped.stopPanning();
	}

	@OriginalApi
	@Override
	public boolean zoomIn() {
		return wrapped.zoomIn();
	}

	@OriginalApi
	@Override
	public boolean zoomInFixing(int xPixel, int yPixel) {
		return wrapped.zoomInFixing(xPixel, yPixel);
	}

	@OriginalApi
	@Override
	public boolean zoomOut() {
		return wrapped.zoomOut();
	}

	@OriginalApi
	@Override
	public boolean zoomOutFixing(int xPixel, int yPixel) {
		return wrapped.zoomOutFixing(xPixel, yPixel);
	}

	@OriginalApi
	@Override
	public void zoomToSpan(int latSpanE6, int lonSpanE6) {
		wrapped.zoomToSpan(latSpanE6, lonSpanE6);
	}

}
