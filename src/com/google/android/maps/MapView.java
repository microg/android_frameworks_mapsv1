package com.google.android.maps;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.*;
import android.widget.ZoomButtonsController;
import android.widget.ZoomControls;
import org.microg.annotation.OriginalApi;
import org.microg.osmdroid.SafeMapTileProviderBasic;
import org.microg.osmdroid.SafeNetworkAvailabilityCheck;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.api.IMapView;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver;

import java.util.Collections;
import java.util.List;

@OriginalApi
public class MapView extends ViewGroup implements IMapView {
	private static final String TAG = MapView.class.getName();
	private final IMapView wrapped;

	private GestureDetector gestureDetector;
	private Handler handler;
	private boolean zoomControlsEnabled = true;
	private Runnable zoomControlsHideCallback;
	private ZoomButtonsController zoomButtonsController;
	private ZoomControls zoomControls;

	@OriginalApi
	public MapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		wrapped = setupWrapped(context, attrs);
		setup();
	}

	@OriginalApi
	public MapView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		wrapped = setupWrapped(context, attrs);
		setup();
	}

	@OriginalApi
	public MapView(Context context, String apiKey) {
		super(context);
		wrapped = setupWrapped(context, null);
		setup();
		if (apiKey == null) {
			Log.w(TAG, "MapViews specify an API Key to be compatible with Google's implementation.");
		}
		if (!(context instanceof MapActivity)) {
			Log.w(TAG, "MapViews should only be created inside instances of MapActivity to be compatible with Google's implementation.");
		}
	}

	private void setup() {
		if (wrapped instanceof View) {
			addView((View) wrapped);
		}
		handler = new Handler();
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
		});
		gestureDetector.setIsLongpressEnabled(false);
	}

	private IMapView setupWrapped(Context context, AttributeSet attrs) {
		return new WrappedMapView(context, attrs);
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
		Log.w(TAG, "Possibly incomplete implementation of displayZoomControls()");
	}

	@OriginalApi
	protected LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(super.generateDefaultLayoutParams());
	}

	@OriginalApi
	@Override
	public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
		return super.generateLayoutParams(attrs); // TODO
	}

	@OriginalApi
	@Override
	protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
		return super.generateLayoutParams(p); // TODO
	}

	@OriginalApi
	@Override
	public MapController getController() {
		return new MapController(wrapped.getController());
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
		if (wrapped instanceof org.osmdroid.views.MapView) {
			return new OverlayList(((org.osmdroid.views.MapView) wrapped).getOverlays());
		} else {
			return new OverlayList(Collections.<org.osmdroid.views.overlay.Overlay>emptyList());
		}
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
		Log.w(TAG, "Possibly incomplete implementation of getZoomButtonsController()");
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
		Log.w(TAG, "Possibly incomplete implementation of getZoomControls()");
		return zoomControls;
	}

	@OriginalApi
	@Override
	public int getZoomLevel() {
		return wrapped.getZoomLevel();
	}

	@OriginalApi
	public boolean isSatellite() {
		return false; // TODO
	}

	@OriginalApi
	public void setSatellite(boolean on) {
		// TODO
	}

	@OriginalApi
	public boolean isStreetView() {
		return false; // TODO
	}

	@OriginalApi
	public void setStreetView(boolean on) {
		// TODO
	}

	@OriginalApi
	public boolean isTraffic() {
		return false; // TODO
	}

	@OriginalApi
	public void setTraffic(boolean on) {
		// TODO
	}

	@OriginalApi
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
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
			((org.osmdroid.views.MapView) wrapped).layout(l, t, r, b);
		}
	}

	@OriginalApi
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@OriginalApi
	public void onRestoreInstanceState(Bundle state) {
		Log.w(TAG, "Incomplete implementation of onRestoreInstanceState()");
		// TODO
	}

	@OriginalApi
	public void onSaveInstanceState(Bundle state) {
		Log.w(TAG, "Incomplete implementation of onSaveInstanceState()");
		// TODO
	}

	@OriginalApi
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@OriginalApi
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		gestureDetector.onTouchEvent(event);
		Log.w(TAG, "Possibly incomplete implementation of onTouchEvent()");
		return (zoomButtonsController != null && zoomButtonsController.isVisible() && zoomButtonsController.onTouch(this, event));
	}

	@OriginalApi
	@Override
	public boolean onTrackballEvent(MotionEvent event) {
		// TODO
		Log.w(TAG, "Incomplete implementation of onTouchEvent()");
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
		// TODO
	}

	@OriginalApi
	public void setBuiltInZoomControls(boolean on) {
		zoomControlsEnabled = on;
		Log.w(TAG, "Possibly incomplete implementation of setBuiltInZoomControls()");
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
		Log.w(TAG, "Incomplete implementation of setReticleDrawMode()");
		// TODO
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
