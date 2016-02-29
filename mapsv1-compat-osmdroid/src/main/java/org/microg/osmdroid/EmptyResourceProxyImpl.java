package org.microg.osmdroid;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import org.osmdroid.ResourceProxy;

public class EmptyResourceProxyImpl implements ResourceProxy {
	@Override
	public String getString(string pResId) {
		return null;
	}

	@Override
	public String getString(string pResId, Object... formatArgs) {
		return null;
	}

	@Override
	public Bitmap getBitmap(bitmap pResId) {
		return null;
	}

	@Override
	public Drawable getDrawable(bitmap pResId) {
		return null;
	}

	@Override
	public float getDisplayMetricsDensity() {
		return 0;
	}

	@Override
	public DisplayMetrics getDisplayMetrics() {
		return new DisplayMetrics();
	}
}
