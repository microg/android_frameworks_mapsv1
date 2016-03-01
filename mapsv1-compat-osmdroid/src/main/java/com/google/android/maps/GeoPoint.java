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

import android.location.Location;

import org.microg.annotation.OriginalApi;
import org.osmdroid.api.IGeoPoint;

@OriginalApi
public class GeoPoint implements IGeoPoint {
    private final IGeoPoint wrapped;

    @OriginalApi
    public GeoPoint(int latitudeE6, int longitudeE6) {
        this(new org.osmdroid.util.GeoPoint(latitudeE6, longitudeE6));
    }

    public GeoPoint(IGeoPoint wrapped) {
        this.wrapped = wrapped;
    }

    public GeoPoint(Location location) {
        this((int) (location.getLatitude() * 1e6), (int) (location.getLongitude() * 1e6));
    }

    @OriginalApi
    @Override
    public boolean equals(Object other) {
        return other == this || (other instanceof GeoPoint) && wrapped.equals(((GeoPoint) other).wrapped);
    }

    @Override
    public double getLatitude() {
        return wrapped.getLatitude();
    }

    @OriginalApi
    @Override
    public int getLatitudeE6() {
        return wrapped.getLatitudeE6();
    }

    @Override
    public double getLongitude() {
        return wrapped.getLongitude();
    }

    @OriginalApi
    @Override
    public int getLongitudeE6() {
        return wrapped.getLongitudeE6();
    }

    @OriginalApi
    @Override
    public int hashCode() {
        return wrapped.hashCode();
    }

    @OriginalApi
    @Override
    public String toString() {
        return getLatitudeE6() + "," + getLongitudeE6();
    }

    public org.osmdroid.util.GeoPoint toOsmdroid() {
        if (wrapped instanceof org.osmdroid.util.GeoPoint) {
            return (org.osmdroid.util.GeoPoint) wrapped;
        } else {
            return new org.osmdroid.util.GeoPoint(wrapped.getLatitudeE6(), wrapped.getLongitudeE6());
        }
    }
}
