package com.google.android.maps;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import org.microg.annotation.OriginalApi;

import java.util.ArrayList;

@OriginalApi
public abstract class ItemizedOverlay<Item extends OverlayItem> extends Overlay implements Overlay.Snappable {
	private static final String TAG = ItemizedOverlay.class.getName();

	@OriginalApi
	protected int mLastFocusedIndex;

	private final Drawable defaultMarker;
	private final ArrayList<Item> internalList = new ArrayList<Item>();
	private Item focusedItem;
	private OnFocusChangeListener focusChangeListener;
	private boolean pendingFocusChangeEvent = false;
	private boolean drawFocusedItem = true;

	@OriginalApi
	public ItemizedOverlay(Drawable defaultMarker) {
		this.defaultMarker = defaultMarker;
	}

	@OriginalApi
	protected static Drawable boundCenter(Drawable ballon) {
		int width = ballon.getIntrinsicWidth();
		int halfWidth = width / 2;
		int height = ballon.getIntrinsicHeight();
		int halfHeight = height / 2;
		ballon.setBounds(-halfWidth, -halfHeight, width - halfWidth, height - halfHeight);
		return ballon;
	}

	@OriginalApi
	protected static Drawable boundCenterBottom(Drawable ballon) {
		int width = ballon.getIntrinsicWidth();
		int halfWidth = width / 2;
		int height = ballon.getIntrinsicHeight();
		ballon.setBounds(-halfWidth, 1 - height, width - halfWidth, 1);
		return ballon;
	}

	@OriginalApi
	protected abstract Item createItem(int i);

	@OriginalApi
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		if (pendingFocusChangeEvent && focusChangeListener != null) {
			focusChangeListener.onFocusChanged(this, focusedItem);
		}
		pendingFocusChangeEvent = false;

		Point point = new Point();
		for (int i = internalList.size() - 1; i >= 0; i--) {
			Item item = getItem(i);
			if (item != null) {
				int state = (drawFocusedItem && (focusedItem == item) ? OverlayItem.ITEM_STATE_FOCUSED_MASK : 0);
				Drawable marker = item.getMarker(state);
				if (marker == null) {
					marker = defaultMarker;
					OverlayItem.setState(marker, state);
				}
				mapView.getProjection().toPixels(item.getPoint(), point);

				Overlay.drawAt(canvas, marker, point.x, point.y, shadow);
			}
		}
	}

	@OriginalApi
	public GeoPoint getCenter() {
		Log.w(TAG, "Incomplete implementation of getCenter()");
		return null; // TODO
	}

	@OriginalApi
	public Item getFocus() {
		return focusedItem;
	}

	@OriginalApi
	public void setFocus(Item item) {
		pendingFocusChangeEvent = item != focusedItem;
		focusedItem = item;
	}

	@OriginalApi
	protected int getIndexToDraw(int drawingOrder) {
		Log.w(TAG, "Incomplete implementation of getIndexToDraw()");
		return 0; // TODO
	}

	@OriginalApi
	public Item getItem(int position) {
		synchronized (internalList) {
			if (internalList.size() > position) {
				return internalList.get(position);
			} else {
				return null;
			}
		}
	}

	@OriginalApi
	public int getLastFocusedIndex() {
		Log.w(TAG, "Possibly incomplete implementation of getLastFocusedIndex()");
		return mLastFocusedIndex; // TODO
	}

	@OriginalApi
	protected void setLastFocusedIndex(int lastFocusedIndex) {
		Log.w(TAG, "Possibly incomplete implementation of setLastFocusedIndex()");
		mLastFocusedIndex = lastFocusedIndex; // TODO
	}

	@OriginalApi
	public int getLatSpanE6() {
		Log.w(TAG, "Incomplete implementation of getLatSpanE6()");
		return 0; // TODO
	}

	@OriginalApi
	public int getLonSpanE6() {
		Log.w(TAG, "Incomplete implementation of getLonSpanE6()");
		return 0; // TODO
	}

	@OriginalApi
	protected boolean hitTest(Item item, Drawable marker, int hitX, int hitY) {
		return marker.getBounds().contains(hitX, hitY);
	}

	@OriginalApi
	public Item nextFocus(boolean forwards) {
		Log.w(TAG, "Incomplete implementation of nextFocus()");
		return null; // TODO
	}

	@OriginalApi
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event, MapView mapView) {
		Log.w(TAG, "Incomplete implementation of onKeyUp()");
		return super.onKeyUp(keyCode, event, mapView); // TODO
	}

	@OriginalApi
	@Override
	public boolean onSnapToItem(int x, int y, Point snapPoint, MapView mapView) {
		Log.w(TAG, "Incomplete implementation of onSnapToItem()");
		return false; // TODO
	}

	@OriginalApi
	@Override
	public boolean onTap(GeoPoint p, MapView mapView) {
		Point touchPoint = mapView.getProjection().toPixels(p, null);
		Point point = new Point();
		for (int i = internalList.size() - 1; i >= 0; i--) {
			final Item item = getItem(i);
			final int state = (drawFocusedItem && (focusedItem == item) ? OverlayItem.ITEM_STATE_FOCUSED_MASK : 0);
			Drawable marker = item.getMarker(state);
			if (marker == null) {
				marker = defaultMarker;
				OverlayItem.setState(marker, state);
			}
			mapView.getProjection().toPixels(item.getPoint(), point);
			if (hitTest(item, marker, touchPoint.x - point.x, touchPoint.y - point.y)) {
				boolean result = onTap(i);
				// TODO things like focus and select here...
				return result;
			}
		}
		return false;
	}

	@OriginalApi
	protected boolean onTap(int index) {
		return false;
	}

	@OriginalApi
	@Override
	public boolean onTouchEvent(MotionEvent e, MapView mapView) {
		Log.w(TAG, "Incomplete implementation of onTouchEvent()");
		return super.onTouchEvent(e, mapView); // TODO
	}

	@OriginalApi
	@Override
	public boolean onTrackballEvent(MotionEvent event, MapView mapView) {
		Log.w(TAG, "Incomplete implementation of onTrackballEvent()");
		return super.onTrackballEvent(event, mapView); // TODO
	}

	@OriginalApi
	protected void populate() {
		synchronized (internalList) {
			int size = size();
			internalList.clear();
			internalList.ensureCapacity(size);
			for (int i = 0; i < size; i++) {
				internalList.add(createItem(i));
			}
		}
	}

	@OriginalApi
	public void setDrawFocusedItem(boolean drawFocusedItem) {
		Log.w(TAG, "Possibly incomplete implementation of setDrawFocusedItem()");
		this.drawFocusedItem = drawFocusedItem; // TODO
	}

	@OriginalApi
	public void setOnFocusChangeListener(OnFocusChangeListener l) {
		Log.w(TAG, "Possibly incomplete implementation of setOnFocusChangeListener()");
		this.focusChangeListener = l; // TODO
	}

	@OriginalApi
	public abstract int size();

	@OriginalApi
	public interface OnFocusChangeListener {
		@OriginalApi
		void onFocusChanged(ItemizedOverlay overlay, OverlayItem newFocus);
	}
}
