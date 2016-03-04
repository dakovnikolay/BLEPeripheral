package jp.co.deliv.android.geocoding;

public class RouteStep {

	private GeoLocation startLocation = new GeoLocation();
	private GeoLocation endLocation = new GeoLocation();
	
	public GeoLocation getStartLocation() {
		return startLocation;
	}
	
	public void setStartLocation(GeoLocation startLocation) {
		this.startLocation = startLocation;
	}
	
	public GeoLocation getEndLocation() {
		return endLocation;
	}
	
	public void setEndLocation(GeoLocation endLocation) {
		this.endLocation = endLocation;
	}
}
