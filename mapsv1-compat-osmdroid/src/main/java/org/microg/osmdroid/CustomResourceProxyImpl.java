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

package org.microg.osmdroid;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;

import com.google.android.maps.MapView;

import org.osmdroid.ResourceProxy;

public class CustomResourceProxyImpl implements ResourceProxy {
    private static final String TAG = "MapResProxy";

    private static Bitmap EMPTY_BITMAP;

    private final Resources resources;
    private final DisplayMetrics displayMetrics;

    public CustomResourceProxyImpl() {
        this(null, null);
    }

    public CustomResourceProxyImpl(Context context) {
        this(context.getResources(), null);
    }

    public CustomResourceProxyImpl(Resources resources, DisplayMetrics displayMetrics) {
        this.resources = resources != null ? resources : MapView.DEFAULT_CONTEXT != null ? MapView.DEFAULT_CONTEXT.getResources() : null;
        this.displayMetrics = displayMetrics != null ? displayMetrics : this.resources != null ? this.resources.getDisplayMetrics() : generateDisplayMetrics();
    }

    private static DisplayMetrics generateDisplayMetrics() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        displayMetrics.setToDefaults();
        return displayMetrics;
    }

    @Override
    public String getString(string pResId) {
        switch (pResId) {
            case unknown:
                return "Unknown";
            case format_distance_meters:
                return "%s m";
            case format_distance_kilometers:
                return "%s km";
            case format_distance_miles:
                return "%s mi";
            case format_distance_nautical_miles:
                return "%s nm";
            case format_distance_feet:
                return "%s ft";
            case online_mode:
                return "Online mode";
            case offline_mode:
                return "Offline mode";
            case my_location:
                return "My location";
            case compass:
                return "Compass";
            case map_mode:
                return "Map mode";

            default:
                Log.w(TAG, "Unknown resource string: " + pResId);
                return "";
        }
    }

    @Override
    public String getString(string pResId, Object... formatArgs) {
        return String.format(getString(pResId), formatArgs);
    }

    @Override
    public Bitmap getBitmap(bitmap pResId) {
        try {
            switch (pResId) {
                case ic_menu_compass:
                    return BitmapFactory.decodeResource(resources, android.R.drawable.ic_menu_compass);
                case ic_menu_mapmode:
                    return BitmapFactory.decodeResource(resources, android.R.drawable.ic_menu_mapmode);
                case ic_menu_mylocation:
                    return BitmapFactory.decodeResource(resources, android.R.drawable.ic_menu_mylocation);
                case next:
                    return BitmapFactory.decodeResource(resources, android.R.drawable.ic_media_next);
                case previous:
                    return BitmapFactory.decodeResource(resources, android.R.drawable.ic_media_previous);
                case center:
                case direction_arrow:
                case ic_menu_offline:
                case marker_default:
                case marker_default_focused_base:
                case navto_small:
                case person:
                case unknown:
                default:
                    Log.w(TAG, "Unknown resource bitmap: " + pResId);
                    return generateEmptyBitmap();
            }
        } catch (Exception e) {
            Log.w(TAG, "Error loading resource bitmap: " + pResId, e);
            return generateEmptyBitmap();
        }
    }

    private static Bitmap generateEmptyBitmap() {
        if (EMPTY_BITMAP == null) {
            EMPTY_BITMAP = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        }
        return EMPTY_BITMAP;
    }

    @Override
    public Drawable getDrawable(bitmap pResId) {
        return new BitmapDrawable(getBitmap(pResId));
    }

    @Override
    public float getDisplayMetricsDensity() {
        return getDisplayMetrics().density;
    }

    @Override
    public DisplayMetrics getDisplayMetrics() {
        return displayMetrics;
    }
}
