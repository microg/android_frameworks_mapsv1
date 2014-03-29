package org.microg.internal;

import android.util.Log;

import java.lang.reflect.Field;

/**
 * This class is used to access com.android.internal.R
 * <p/>
 * It is used the same way as the original it uses reflection to retrieve the data.
 */
public class R {
	private static final String TAG = R.class.getName();

	private static Field getField(String type, String name) {
		try {
			Class cls = Class.forName("com.android.internal.R." + type);
			return cls.getField(name);
		} catch (Exception e) {
			Log.w(TAG, "Could not retrieve com.android.internal.R." + type + "." + name, e);
			return null;
		}
	}

	private static Object get(String type, String name) {
		try {
			Field field = getField(type, name);
			return field == null ? null : field.get(null);
		} catch (Exception e) {
			Log.w(TAG, "Could not retrieve com.android.internal.R." + type + "." + name, e);
			return null;
		}
	}

	private static int getInt(String type, String name) {
		try {
			Field field = getField(type, name);
			return field == null ? -1 : field.getInt(null);
		} catch (Exception e) {
			Log.w(TAG, "Could not retrieve com.android.internal.R." + type + "." + name, e);
			return -1;
		}
	}

	public static final class attr {
		private static final String TYPE = "attr";
		public static final int mapViewStyle = getInt(TYPE, "mapViewStyle");
	}

	public static final class styleable {
		private static final String TYPE = "styleable";
		public static final int[] MapView = (int[]) get(TYPE, "MapView");
		public static final int MapView_apiKey = getInt(TYPE, "MapView_apiKey");
	}
}
