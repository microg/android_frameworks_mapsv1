/*
 * Copyright 2013-2016 microG Project Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.maps;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import org.microg.annotation.OriginalApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@OriginalApi
public abstract class ItemizedOverlay<Item extends OverlayItem> extends Overlay implements Overlay.Snappable {
    private static final String TAG = ItemizedOverlay.class.getName();

    @OriginalApi
    protected int mLastFocusedIndex;

    private final Drawable defaultMarker;
    private final ArrayList<Item> internalList = new ArrayList<Item>();
    private int latSpanE6;
    private int lonSpanE6;
    private Item focusedItem;
    private GeoPoint center;
    private OnFocusChangeListener focusChangeListener;
    private boolean pendingFocusChangeEvent = false;
    private boolean drawFocusedItem = true;
    private Comparator<Item> latitudeComparator = new Comparator<Item>() {
        @Override
        public int compare(Item lhs, Item rhs) {
            return lhs.getPoint().getLatitudeE6() - rhs.getPoint().getLatitudeE6();
        }
    };
    private Comparator<Item> drawComparator = new Comparator<Item>() {
        @Override
        public int compare(Item lhs, Item rhs) {
            return getIndexToDraw(lhs.index) - getIndexToDraw(rhs.index);
        }
    };

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

        List<Item> sortedList = new ArrayList<Item>();
        for (int i = 0; i < internalList.size(); i++) {
            sortedList.add(getItem(i));
        }
        Collections.sort(sortedList, drawComparator);

        Point point = new Point();
        for (Item item : sortedList) {
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
        if (!internalList.isEmpty()) {
            return center;
        } else {
            return null;
        }
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
        return internalList.get(drawingOrder).sortedIndex;
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
        return mLastFocusedIndex;
    }

    @OriginalApi
    protected void setLastFocusedIndex(int lastFocusedIndex) {
        mLastFocusedIndex = lastFocusedIndex;
    }

    @OriginalApi
    public int getLatSpanE6() {
        return latSpanE6;
    }

    @OriginalApi
    public int getLonSpanE6() {
        return lonSpanE6;
    }

    @OriginalApi
    protected boolean hitTest(Item item, Drawable marker, int hitX, int hitY) {
        return marker.getBounds().contains(hitX, hitY);
    }

    @OriginalApi
    public Item nextFocus(boolean forwards) {
        if (forwards) {
            return mLastFocusedIndex >= internalList.size() - 1 ? null : internalList.get(mLastFocusedIndex + 1);
        } else {
            return mLastFocusedIndex <= 0 ? null : internalList.get(mLastFocusedIndex - 1);
        }
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
        List<Item> hitItems = new ArrayList<Item>();
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
                hitItems.add(item);
            }
        }
        if (!hitItems.isEmpty()) {
            Item item = hitItems.get(0);
            for (Item hitItem : hitItems) {
                if (hitItem.getPoint().getLatitude() > item.getPoint().getLatitude())
                    item = hitItem;
            }
            Log.d(TAG, "Hit " + hitItems.size() + " items, most likely " + item);
            boolean result = onTap(item.index);
            if (focusedItem != item) {
                focusedItem = item;
                mLastFocusedIndex = item.index;
                if (focusChangeListener != null) {
                    focusChangeListener.onFocusChanged(this, item);
                }
            }
            return result;
        } else {
            focusedItem = null;
            return false;
        }
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
            int minLatE6 = 90000000, maxLatE6 = -90000000, minLonE6 = 180000000, maxLonE6 = -180000000;
            for (int i = 0; i < size; i++) {
                Item item = createItem(i);
                item.index = i;
                minLatE6 = Math.min(minLatE6, item.getPoint().getLatitudeE6());
                maxLatE6 = Math.max(maxLatE6, item.getPoint().getLatitudeE6());
                minLonE6 = Math.min(minLonE6, item.getPoint().getLongitudeE6());
                minLonE6 = Math.min(minLonE6, item.getPoint().getLongitudeE6());
                internalList.add(item);
            }
            List<Item> sortedList = new ArrayList<Item>(internalList);
            Collections.sort(sortedList, latitudeComparator);
            for (int i = 0; i < sortedList.size(); i++) {
                sortedList.get(i).sortedIndex = i;
            }
            latSpanE6 = maxLatE6 - minLatE6;
            lonSpanE6 = maxLonE6 - minLonE6;
            center = new GeoPoint(minLatE6 + latSpanE6 / 2, minLonE6 + lonSpanE6 / 2);
        }
        if (assignedMapView != null && assignedMapView.getHandler() != null) {
            assignedMapView.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    assignedMapView.invalidate();
                }
            });
        }
    }

    @OriginalApi
    public void setDrawFocusedItem(boolean drawFocusedItem) {
        this.drawFocusedItem = drawFocusedItem;
    }

    @OriginalApi
    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        this.focusChangeListener = l;
    }

    @OriginalApi
    public abstract int size();

    @OriginalApi
    public interface OnFocusChangeListener {
        @OriginalApi
        void onFocusChanged(ItemizedOverlay overlay, OverlayItem newFocus);
    }
}
