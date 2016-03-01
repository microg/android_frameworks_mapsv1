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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import org.microg.annotation.OriginalApi;

@OriginalApi
public class MapActivity extends Activity {

    @OriginalApi
    public MapActivity() {
        super();
    }

    @OriginalApi
    protected boolean isLocationDisplayed() {
        return false;
    }

    @OriginalApi
    protected boolean isRouteDisplayed() {
        return false;
    }

    @OriginalApi
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OriginalApi
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OriginalApi
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @OriginalApi
    @Override
    protected void onPause() {
        super.onPause();
    }

    @OriginalApi
    @Override
    protected void onResume() {
        super.onResume();
    }
}
