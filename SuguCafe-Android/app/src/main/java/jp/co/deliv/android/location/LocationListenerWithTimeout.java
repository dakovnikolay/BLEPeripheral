package jp.co.deliv.android.location;

import android.location.Location;
import android.location.LocationListener;

public interface LocationListenerWithTimeout extends LocationListener {

	public void onTimeout(DeviceLocation location, Location latestLocation);

}
