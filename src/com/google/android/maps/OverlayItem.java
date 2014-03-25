package com.google.android.maps;

import android.graphics.drawable.Drawable;
import org.microg.annotation.OriginalApi;

@OriginalApi
public class OverlayItem {
	@OriginalApi
	public static int ITEM_STATE_FOCUSED_MASK = 4;
	@OriginalApi
	public static int ITEM_STATE_PRESSED_MASK = 1;
	@OriginalApi
	public static int ITEM_STATE_SELECTED_MASK = 0;

	@OriginalApi
	protected Drawable mMarker;
	@OriginalApi
	protected GeoPoint mPoint;
	@OriginalApi
	protected String mSnippet;
	@OriginalApi
	protected String mTitle;

	@OriginalApi
	public OverlayItem(GeoPoint point, String title, String snippet) {
		mPoint = point;
		mTitle = title;
		mSnippet = snippet;
	}

	@OriginalApi
	public static void setState(Drawable drawable, int stateBitset) {
		// TODO
	}

	@OriginalApi
	public Drawable getMarker(int stateBitset) {
		if (mMarker != null) {
			setState(mMarker, stateBitset);
		}
		return mMarker;
	}

	@OriginalApi
	public void setMarker(Drawable marker) {
		mMarker = marker;
	}

	@OriginalApi
	public GeoPoint getPoint() {
		return mPoint;
	}

	@OriginalApi
	public String getSnippet() {
		return mSnippet;
	}

	@OriginalApi
	public String getTitle() {
		return mTitle;
	}

	@OriginalApi
	public String routableAddress() {
		return mPoint.getLatitudeE6() / 1e6F + ", " + mPoint.getLongitudeE6() / 1e6F;
	}

	public org.osmdroid.views.overlay.OverlayItem toOsmdroid() {
		return new WrappedOverlayItem();
	}

	public class WrappedOverlayItem extends org.osmdroid.views.overlay.OverlayItem {
		public WrappedOverlayItem() {
			super(OverlayItem.this.mTitle, OverlayItem.this.mSnippet, mPoint.toOsmdroid());
		}

		public OverlayItem getOriginal() {
			return OverlayItem.this;
		}
	}
}
