package org.microg.osmdroid;

import android.content.Context;
import org.osmdroid.tileprovider.IMapTileProviderCallback;
import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.tileprovider.MapTileProviderArray;
import org.osmdroid.tileprovider.modules.INetworkAvailablityCheck;
import org.osmdroid.tileprovider.modules.MapTileDownloader;
import org.osmdroid.tileprovider.modules.NetworkAvailabliltyCheck;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver;

/**
 * This version differs from the original in that it uses the Safe classes instead of the default ones.
 */
public class SafeMapTileProviderBasic extends MapTileProviderArray implements IMapTileProviderCallback {

	// private static final Logger logger = LoggerFactory.getLogger(MapTileProviderBasic.class);

	/**
	 * Creates a {@link SafeMapTileProviderBasic}.
	 */
	public SafeMapTileProviderBasic(final Context pContext) {
		this(pContext, TileSourceFactory.DEFAULT_TILE_SOURCE);
	}

	/**
	 * Creates a {@link SafeMapTileProviderBasic}.
	 */
	public SafeMapTileProviderBasic(final Context pContext, final ITileSource pTileSource) {
		this(pContext, new SimpleRegisterReceiver(pContext), new NetworkAvailabliltyCheck(pContext),
				pTileSource);
	}

	/**
	 * Creates a {@link SafeMapTileProviderBasic}.
	 */
	public SafeMapTileProviderBasic(Context context, final IRegisterReceiver pRegisterReceiver,
	                                final INetworkAvailablityCheck aNetworkAvailablityCheck, final ITileSource pTileSource) {
		super(pTileSource, pRegisterReceiver);

		final SafeTileWriter tileWriter = new SafeTileWriter(context);

		final SafeMapTileFilesystemProvider fileSystemProvider = new SafeMapTileFilesystemProvider(context,
				pRegisterReceiver, pTileSource);
		mTileProviderList.add(fileSystemProvider);

		final MapTileDownloader downloaderProvider = new MapTileDownloader(pTileSource, tileWriter,
				aNetworkAvailablityCheck);
		mTileProviderList.add(downloaderProvider);
	}
}
