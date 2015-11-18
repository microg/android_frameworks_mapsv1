package com.google.android.maps;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;
import android.widget.ZoomButtonsController;
import android.widget.ZoomControls;
import org.microg.annotation.OriginalApi;
import org.microg.internal.R;
import org.microg.osmdroid.SafeMapTileProviderBasic;
import org.microg.osmdroid.SafeNetworkAvailabilityCheck;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.api.IMapView;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver;

import java.util.List;

/*
 * TODO:
 * - Possibly bad trackball support, but no way to test for me
 * - We don't draw the reticle, but it's for trackball only afaik.
 * - We can't show traffic and satellite maps the same moment...
 */

@OriginalApi
public class MapView extends ViewGroup implements IMapView {
	private static final String TAG = MapView.class.getName();
	private static final String KEY_CENTER_LATITUDE = MapView.class.getName() + ".centerLatitude";
	private static final String KEY_CENTER_LONGITUDE = MapView.class.getName() + ".centerLongitude";
	private static final String KEY_ZOOM_DISPLAYED = MapView.class.getName() + ".zoomDisplayed";
	private static final String KEY_ZOOM_LEVEL = MapView.class.getName() + ".zoomLevel";

	// TODO: We should read these from a setting, users might want to change it...
	private static final ITileSource DEFAULT = TileSourceFactory.MAPNIK;
	private static final ITileSource TRAFFIC = TileSourceFactory.PUBLIC_TRANSPORT;
	private static final ITileSource SATELLITE = TileSourceFactory.MAPQUESTAERIAL;

	private GestureDetector gestureDetector;
	private Handler handler = new Handler();
	private MapController mapController;
	private ReticleDrawMode reticleDrawMode;
	private final WrappedMapView wrapped;
	private boolean zoomControlsEnabled = true;
	private Runnable zoomControlsHideCallback;
	private ZoomButtonsController zoomButtonsController;
	private ZoomControls zoomControls;

	private boolean satellite = false;
	private boolean streetView = false;
	private boolean traffic = false;

	@OriginalApi
	public MapView(Context context, AttributeSet attrs) {
		this(context, attrs, R.attr.mapViewStyle);
	}

	@OriginalApi
	public MapView(Context context, AttributeSet attrs, int defStyle) {
		this(context, attrs, defStyle, null);
	}

	@OriginalApi
	public MapView(Context context, String apiKey) {
		this(context, null, R.attr.mapViewStyle, apiKey);
	}

	public MapView(Context context, AttributeSet attrs, int defStyle, String apiKey) {
		super(context, attrs, defStyle);
		wrapped = new WrappedMapView(context, attrs);
		mapController = new MapController(wrapped.getController());
		addView(wrapped);

		// Warn the developer that his usage of MapView will not work with Google's implementation.

		if (attrs != null) {
			try {
				TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MapView);
				if (array != null) {
					if (apiKey == null) {
						apiKey = array.getString(R.styleable.MapView_apiKey);
					}
					array.recycle();
				}
			} catch (Exception e) {
				// This might fail, if we can't access the internal R or it's modified
				Log.w(TAG, e);
			}
		}

		if (apiKey == null) {
			Log.w(TAG, "MapViews must specify an API Key to be compatible with Google's implementation.");
		}
		if (!(context instanceof MapActivity)) {
			Log.w(TAG, "MapViews must only be created inside instances of MapActivity to be compatible with Google's implementation.");
		}

		// Set startup location as suggested from resources
		try {
			int[] latlonE6 = getResources().getIntArray(R.array.maps_starting_lat_lng);
			getController().setCenter(new GeoPoint(latlonE6[0], latlonE6[1]));
			getController().setZoom(getResources().getIntArray(R.array.maps_starting_zoom)[0]);
		} catch (Exception e) {
			// This might fail, if we can't access the internal R or it's modified
			Log.w(TAG, e);
		}

		// We detect gestures non-exclusively. osmdroid uses the same gesture detection,
		// but we can't depend on their implementation as it's not accessible from this context.
		gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
			@Override
			public boolean onDown(MotionEvent e) {
				displayZoomControls(false);
				return false;
			}

			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
				displayZoomControls(false);
				return false;
			}

            @Override
            public void onLongPress(MotionEvent e) {
                MapView.this.performLongClick();
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return MapView.this.performClick();
            }
        });
        //gestureDetector.setIsLongpressEnabled(false);
	}

	@OriginalApi
	public boolean canCoverCenter() {
		Log.w(TAG, "Incomplete implementation of canCoverCenter()");
		return true; // TODO
	}

	@OriginalApi
	protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
		return p instanceof LayoutParams;
	}

	@OriginalApi
	public void displayZoomControls(boolean takeFocus) {
		if (zoomControlsEnabled) {
			if ((zoomButtonsController != null) && (!zoomButtonsController.isVisible())) {
				zoomButtonsController.setFocusable(takeFocus);
				zoomButtonsController.setVisible(true);
			}
			if (zoomControls != null) {
				if (zoomControls.getVisibility() == View.GONE) {
					zoomControls.show();
				}
				if (takeFocus) {
					zoomControls.requestFocus();
				}
				handler.removeCallbacks(zoomControlsHideCallback);
				handler.postDelayed(zoomControlsHideCallback, ViewConfiguration.getZoomControlsTimeout());
			}
		}
	}

	@OriginalApi
	protected LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, new GeoPoint(0, 0), LayoutParams.CENTER);
	}

	@OriginalApi
	@Override
	public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new LayoutParams(getContext(), attrs);
	}

	@OriginalApi
	@Override
	protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
		return new LayoutParams(p);
	}

	@OriginalApi
	@Override
	public MapController getController() {
		return mapController;
	}

	@OriginalApi
	@Override
	public int getLatitudeSpan() {
		return wrapped.getLatitudeSpan();
	}

	@OriginalApi
	@Override
	public int getLongitudeSpan() {
		return wrapped.getLongitudeSpan();
	}

	@OriginalApi
	@Override
	public GeoPoint getMapCenter() {
		return new GeoPoint(wrapped.getMapCenter());
	}

	@OriginalApi
	@Override
	public int getMaxZoomLevel() {
		return wrapped.getMaxZoomLevel();
	}

	@OriginalApi
	public List<Overlay> getOverlays() {
		return new OverlayList(wrapped.getOverlays());
	}

	@OriginalApi
	@Override
	public Projection getProjection() {
		return new ProjectionWrapper(wrapped.getProjection());
	}

	public IMapView getWrapped() {
		return wrapped;
	}

	@OriginalApi
	public ZoomButtonsController getZoomButtonsController() {
		return zoomButtonsController;
	}

	@OriginalApi
	@Deprecated
	public View getZoomControls() {
		if (zoomControls == null) {
			zoomControls = new ZoomControls(getContext());
			zoomControls.setZoomSpeed(2000);
			zoomControls.setOnZoomInClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					wrapped.getController().zoomIn();
				}
			});
			zoomControls.setOnZoomOutClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					wrapped.getController().zoomOut();
				}
			});
			zoomControls.setVisibility(View.GONE);
			zoomControlsHideCallback = new Runnable() {
				@Override
				public void run() {
					if (!zoomControls.hasFocus()) {
						zoomControls.hide();
					} else {
						handler.removeCallbacks(zoomControlsHideCallback);
						handler.postDelayed(zoomControlsHideCallback, ViewConfiguration.getZoomControlsTimeout());
					}
				}
			};
		}
		return zoomControls;
	}

	@OriginalApi
	@Override
	public int getZoomLevel() {
		return wrapped.getZoomLevel();
	}

	@OriginalApi
	public boolean isSatellite() {
		return satellite;
	}

	@OriginalApi
	public void setSatellite(boolean on) {
		satellite = on;
		wrapped.updateTileSource();
	}

	@OriginalApi
	public boolean isStreetView() {
		return streetView;
	}

	@OriginalApi
	public void setStreetView(boolean on) {
		if (on) setTraffic(false);
		streetView = on;
	}

	@OriginalApi
	public boolean isTraffic() {
		return traffic;
	}

	@OriginalApi
	public void setTraffic(boolean on) {
		if (on) setStreetView(false);
		traffic = true;
	}

	@OriginalApi
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
        // Fix for bug in ZoomButtonsController
        if (zoomButtonsController != null) zoomButtonsController.setVisible(false);
	}

	@OriginalApi
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	@OriginalApi
	@Override
	public void onFocusChanged(boolean hasFocus, int direction, Rect unused) {
		super.onFocusChanged(hasFocus, direction, unused);
	}

	@OriginalApi
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	@OriginalApi
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return super.onKeyUp(keyCode, event);
	}

	@OriginalApi
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (wrapped instanceof org.osmdroid.views.MapView) {
			wrapped.layout(l, t, r, b);
		}
	}

	@OriginalApi
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@OriginalApi
	public void onRestoreInstanceState(Bundle state) {
		if (state != null) {
			int latE6 = state.getInt(KEY_CENTER_LATITUDE, Integer.MAX_VALUE);
			int lonE6 = state.getInt(KEY_CENTER_LONGITUDE, Integer.MAX_VALUE);
			if (latE6 != Integer.MAX_VALUE && lonE6 != Integer.MAX_VALUE) {
				getController().setCenter(new GeoPoint(latE6, lonE6));
			}
			int zoom = state.getInt(KEY_ZOOM_LEVEL, Integer.MAX_VALUE);
			if (zoom != Integer.MAX_VALUE) {
				getController().setZoom(zoom);
			}
			if (state.getInt(KEY_ZOOM_DISPLAYED, 0) != 0) {
				displayZoomControls(false);
			}
		}
	}

	@OriginalApi
	public void onSaveInstanceState(Bundle state) {
		// We use the same way to store information in the bundle as Google does,
		// also this is not documented, there are a number of apps known to rely on it.

		state.putInt(KEY_CENTER_LATITUDE, getMapCenter().getLatitudeE6());
		state.putInt(KEY_CENTER_LONGITUDE, getMapCenter().getLongitudeE6());
		state.putInt(KEY_ZOOM_LEVEL, getZoomLevel());
		state.putInt(KEY_ZOOM_DISPLAYED, (zoomButtonsController != null) && (zoomButtonsController.isVisible()) || ((zoomControls != null) && (zoomControls.getVisibility() == View.VISIBLE)) ? 1 : 0);
	}

	@OriginalApi
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@OriginalApi
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// We only consume the touch event if it is used by the zoom button.
		// The gestures detected by us are also relevant for the underlying MapView!
		gestureDetector.onTouchEvent(event);
		return (zoomButtonsController != null && zoomButtonsController.isVisible() && zoomButtonsController.onTouch(this, event));
	}

	@OriginalApi
	@Override
	public boolean onTrackballEvent(MotionEvent event) {
		Log.w(TAG, "Incomplete implementation of onTrackballEvent()");
		// TODO we do not support trackball well, but they're kind of deprecated anyway?!
		return false;
	}

	@OriginalApi
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
	}

	@OriginalApi
	public void preLoad() {
		Log.w(TAG, "Incomplete implementation of preLoad()");
		// TODO not sure what this actually does...
	}

	@OriginalApi
	public void setBuiltInZoomControls(boolean on) {
		zoomControlsEnabled = on;
		if (zoomButtonsController == null) {
			zoomButtonsController = new ZoomButtonsController(this);
			zoomButtonsController.setZoomSpeed(2000);
			zoomButtonsController.setOnZoomListener(new ZoomButtonsController.OnZoomListener() {
				@Override
				public void onVisibilityChanged(boolean visible) {
					if (visible) {
						zoomButtonsController.setZoomInEnabled(wrapped.getZoomLevel() < wrapped.getMaxZoomLevel());
						zoomButtonsController.setZoomOutEnabled(wrapped.getZoomLevel() > 1);
					} else {
						zoomButtonsController.setFocusable(false);
					}
				}

				@Override
				public void onZoom(boolean zoomIn) {
					if (zoomIn) {
						wrapped.getController().zoomIn();
					} else {
						wrapped.getController().zoomOut();
					}
				}
			});
		}
	}

	@OriginalApi
	public void setReticleDrawMode(ReticleDrawMode mode) {
		reticleDrawMode = mode;
	}

	@OriginalApi
	public static enum ReticleDrawMode {
		DRAW_RETICLE_OVER,
		DRAW_RETICLE_UNDER,
		DRAW_RETICLE_NEVER
	}

	@OriginalApi
	public static class LayoutParams extends ViewGroup.LayoutParams {
		@OriginalApi
		public static int BOTTOM = 80;
		@OriginalApi
		public static int BOTTOM_CENTER = 81;
		@OriginalApi
		public static int CENTER = 17;
		@OriginalApi
		public static int CENTER_HORIZONTAL = 1;
		@OriginalApi
		public static int CENTER_VERTICAL = 16;
		@OriginalApi
		public static int LEFT = 3;
		@OriginalApi
		public static int RIGHT = 5;
		@OriginalApi
		public static int TOP = 48;
		@OriginalApi
		public static int TOP_LEFT = 51;
		@OriginalApi
		public static int MODE_MAP = 0;
		@OriginalApi
		public static int MODE_VIEW = 1;

		@OriginalApi
		public int alignment;
		@OriginalApi
		public int mode;
		@OriginalApi
		public GeoPoint point;
		@OriginalApi
		public int x;
		@OriginalApi
		public int y;

		@OriginalApi
		public LayoutParams(Context c, AttributeSet attrs) {
			super(c, attrs);
		}

		@OriginalApi
		public LayoutParams(int width, int height, GeoPoint point, int alignment) {
			this(width, height, point, 0, 0, alignment);
		}

		@OriginalApi
		public LayoutParams(int width, int height, GeoPoint point, int x, int y, int alignment) {
			super(width, height);
			this.point = point;
			this.x = x;
			this.y = y;
			this.alignment = alignment;

			mode = MODE_MAP;
		}

		@OriginalApi
		public LayoutParams(int width, int height, int x, int y, int alignment) {
			super(width, height);
			this.x = x;
			this.y = y;
			this.alignment = alignment;

			mode = MODE_VIEW;
		}

		@OriginalApi
		public LayoutParams(ViewGroup.LayoutParams source) {
			super(source);

			if (source instanceof LayoutParams) {
				alignment = ((LayoutParams) source).alignment;
				mode = ((LayoutParams) source).mode;
				point = ((LayoutParams) source).point;
				x = ((LayoutParams) source).x;
				y = ((LayoutParams) source).y;
			} else if (source instanceof org.osmdroid.views.MapView.LayoutParams) {
				alignment = ((org.osmdroid.views.MapView.LayoutParams) source).alignment;
				x = ((org.osmdroid.views.MapView.LayoutParams) source).offsetX;
				y = ((org.osmdroid.views.MapView.LayoutParams) source).offsetY;
				point = new GeoPoint(((org.osmdroid.views.MapView.LayoutParams) source).geoPoint);
				if (point != null) {
					mode = MODE_MAP;
				} else {
					mode = MODE_VIEW;
				}
			} else {
				mode = MODE_VIEW;
				alignment = TOP_LEFT;
			}
		}

		/**
		 * Converts the specified size to a readable String.
		 * <p/>
		 * Is a hidden API method from {@link android.view.ViewGroup}.
		 *
		 * @param size the size to convert
		 * @return a String instance representing the supplied size
		 */
		protected static String sizeToString(int size) {
			if (size == WRAP_CONTENT) {
				return "wrap-content";
			}
			if (size == MATCH_PARENT) {
				return "match-parent";
			}
			return String.valueOf(size);
		}

		@OriginalApi
		public String debug(String output) {
			// We use the same output format as the original Google implementation, although i doubt anybody parses it.
			return output + "MapView.LayoutParams={" +
					"width=" + sizeToString(this.width) +
					", height=" + sizeToString(this.height) +
					" mode=" + this.mode +
					" lat=" + this.point.getLatitudeE6() +
					" lng=" + this.point.getLongitudeE6() +
					" x= " + this.x +
					" y= " + this.y +
					" alignment=" + this.alignment +
					"}";
		}
	}

	public class WrappedMapView extends org.osmdroid.views.MapView {

		public WrappedMapView(Context context, AttributeSet attrs) {
			super(context, 256, new DefaultResourceProxyImpl(context), new SafeMapTileProviderBasic(context,
					new SimpleRegisterReceiver(context), new SafeNetworkAvailabilityCheck(context),
					TileSourceFactory.DEFAULT_TILE_SOURCE), null, attrs);
			setMultiTouchControls(true);
			updateTileSource();
		}

		public void updateTileSource() {
			if (satellite) {
				setTileSource(SATELLITE);
			} else if (traffic) {
				setTileSource(TRAFFIC);
			} else {
				setTileSource(DEFAULT);
			}
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			return MapView.this.onTouchEvent(event) || super.onTouchEvent(event);
		}

		@Override
		public boolean onTrackballEvent(MotionEvent event) {
			return MapView.this.onTrackballEvent(event) || super.onTrackballEvent(event);
		}

		@Override
		public boolean onKeyUp(int keyCode, KeyEvent event) {
			return MapView.this.onKeyUp(keyCode, event) || super.onKeyUp(keyCode, event);
		}

		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			return MapView.this.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
		}

		public MapView getOriginal() {
			return MapView.this;
		}
	}
}
