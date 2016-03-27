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
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.MotionEvent;

import org.microg.annotation.OriginalApi;
import org.microg.osmdroid.CustomResourceProxyImpl;

import java.util.ArrayList;
import java.util.List;

@OriginalApi
public class Overlay extends org.osmdroid.views.overlay.Overlay {

    @OriginalApi
    protected static float SHADOW_X_SKEW = -0.9F;
    @OriginalApi
    protected static float SHADOW_Y_SCALE = 0.5F;
    MapView assignedMapView;

    @OriginalApi
    public Overlay() {
        super(MapView.DEFAULT_CONTEXT == null ? new CustomResourceProxyImpl() : new CustomResourceProxyImpl(MapView.DEFAULT_CONTEXT));
    }

    @OriginalApi
    protected static void drawAt(Canvas canvas, Drawable drawable, int x, int y, boolean shadow) {
        if (shadow) {
            drawable.setColorFilter(Color.argb(128, 0, 0, 0), PorterDuff.Mode.SRC_IN);
        }
        canvas.save();
        canvas.translate(x, y);
        if (shadow) {
            canvas.skew(SHADOW_X_SKEW, 0);
            canvas.scale(1, SHADOW_Y_SCALE);
        }
        drawable.draw(canvas);
        if (shadow) {
            drawable.clearColorFilter();
        }
        canvas.restore();
    }

    public static List<Overlay> unwrap(List<org.osmdroid.views.overlay.Overlay> overlays) {
        List<Overlay> result = new ArrayList<Overlay>();
        for (org.osmdroid.views.overlay.Overlay overlay : overlays) {
            Overlay unwrap = Overlay.unwrap(overlay);
            if (unwrap != null) {
                result.add(unwrap);
            }
        }
        return result;
    }

    public static Overlay unwrap(org.osmdroid.views.overlay.Overlay overlay) {
        if (overlay instanceof Overlay) {
            return (Overlay) overlay;
        } else {
            return null;
        }
    }

    @Override
    protected void draw(Canvas c, org.osmdroid.views.MapView osmv, boolean shadow) {
        draw(c, osmv instanceof MapView.WrappedMapView ? ((MapView.WrappedMapView) osmv).getOriginal() : null, shadow);
    }

    @OriginalApi
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
        // To be overridden
    }

    @OriginalApi
    public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
        draw(canvas, mapView, shadow);
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event, org.osmdroid.views.MapView mapView) {
        if (mapView instanceof MapView.WrappedMapView)
            return onKeyDown(keyCode, event, ((MapView.WrappedMapView) mapView).getOriginal());
        return false;
    }

    @OriginalApi
    public boolean onKeyDown(int keyCode, KeyEvent event, MapView mapView) {
        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event, org.osmdroid.views.MapView mapView) {
        if (mapView instanceof MapView.WrappedMapView)
            return onKeyUp(keyCode, event, ((MapView.WrappedMapView) mapView).getOriginal());
        return false;
    }

    @OriginalApi
    public boolean onKeyUp(int keyCode, KeyEvent event, MapView mapView) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e, org.osmdroid.views.MapView mapView) {
        if (mapView instanceof MapView.WrappedMapView)
            return onTap(new GeoPoint(mapView.getProjection().fromPixels((int) e.getX(), (int) e.getY())), ((MapView.WrappedMapView) mapView).getOriginal());
        return false;
    }

    @OriginalApi
    public boolean onTap(GeoPoint p, MapView mapView) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event, org.osmdroid.views.MapView mapView) {
        if (mapView instanceof MapView.WrappedMapView)
            return onTouchEvent(event, ((MapView.WrappedMapView) mapView).getOriginal());
        return false;
    }

    @OriginalApi
    public boolean onTouchEvent(MotionEvent e, MapView mapView) {
        return false;
    }

    @Override
    public boolean onTrackballEvent(MotionEvent event, org.osmdroid.views.MapView mapView) {
        if (mapView instanceof MapView.WrappedMapView)
            return onTrackballEvent(event, ((MapView.WrappedMapView) mapView).getOriginal());
        return false;
    }

    @OriginalApi
    public boolean onTrackballEvent(MotionEvent event, MapView mapView) {
        return false;
    }

    @OriginalApi
    public interface Snappable {
        boolean onSnapToItem(int x, int y, Point snapPoint, MapView mapView);
    }
}
