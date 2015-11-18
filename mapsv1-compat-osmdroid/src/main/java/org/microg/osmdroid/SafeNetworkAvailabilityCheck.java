package org.microg.osmdroid;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import org.osmdroid.tileprovider.modules.NetworkAvailabliltyCheck;

/**
 * A straightforward network check implementation, based on the original implementation,
 * that assumes to be online if the permission to check is not given.
 */
public class SafeNetworkAvailabilityCheck extends NetworkAvailabliltyCheck {

	private final boolean hasAccessNetworkStatePermission;
	private final boolean hasChangeNetworkStatePermission;

	public SafeNetworkAvailabilityCheck(Context context) {
		super(context);
		hasAccessNetworkStatePermission = context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED;
		hasChangeNetworkStatePermission = context.checkCallingOrSelfPermission(Manifest.permission.CHANGE_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED;
	}

	@Override
	public boolean getNetworkAvailable() {
		return !hasAccessNetworkStatePermission || super.getNetworkAvailable();
	}

	@Override
	public boolean getWiFiNetworkAvailable() {
		return !hasAccessNetworkStatePermission || super.getWiFiNetworkAvailable();
	}

	@Override
	public boolean getCellularDataNetworkAvailable() {
		return !hasAccessNetworkStatePermission || super.getCellularDataNetworkAvailable();
	}

	@Override
	public boolean getRouteToPathExists(int hostAddress) {
		return !hasChangeNetworkStatePermission || super.getRouteToPathExists(hostAddress);
	}
}
