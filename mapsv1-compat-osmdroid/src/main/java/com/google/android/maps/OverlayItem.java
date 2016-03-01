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

import android.graphics.drawable.Drawable;

import org.microg.annotation.OriginalApi;
import org.microg.internal.R;

@OriginalApi
public class OverlayItem {
    @OriginalApi
    public static int ITEM_STATE_FOCUSED_MASK = 4;
    @OriginalApi
    public static int ITEM_STATE_PRESSED_MASK = 1;
    @OriginalApi
    public static int ITEM_STATE_SELECTED_MASK = 2;

    @OriginalApi
    protected Drawable mMarker;
    @OriginalApi
    protected GeoPoint mPoint;
    @OriginalApi
    protected String mSnippet;
    @OriginalApi
    protected String mTitle;

    int index;
    int sortedIndex;

    @OriginalApi
    public OverlayItem(GeoPoint point, String title, String snippet) {
        mPoint = point;
        mTitle = title;
        mSnippet = snippet;
    }

    private static int unbitset(int bitset, int mask, int value) {
        return (bitset & mask) == mask ? value : -value;
    }

    @OriginalApi
    public static void setState(Drawable drawable, int stateBitset) {
        drawable.setState(new int[]{
                unbitset(stateBitset, ITEM_STATE_FOCUSED_MASK, R.attr.state_focused),
                unbitset(stateBitset, ITEM_STATE_SELECTED_MASK, R.attr.state_selected),
                unbitset(stateBitset, ITEM_STATE_PRESSED_MASK, R.attr.state_pressed)
        });
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
