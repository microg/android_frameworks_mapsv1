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
