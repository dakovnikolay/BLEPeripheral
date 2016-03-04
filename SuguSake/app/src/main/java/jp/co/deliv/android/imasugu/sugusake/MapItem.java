package jp.co.deliv.android.imasugu.sugusake;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * Googleマップ上に表示するオーバーレイアイテム
 * @author Atsushi
 *
 */
public class MapItem extends Overlay {

	private int latitudeE6;
	private int longitudeE6;
	private Drawable marker;
	
	public MapItem(double latitude, double longitude, Drawable marker) {
		this.latitudeE6 = (int)(Math.round(latitude * 1E6));
		this.longitudeE6 = (int)(Math.round(longitude * 1E6));
		this.marker = marker;
	}

	/**
	 * Map上に表示するアイテムの描画
	 * @see com.google.android.maps.Overlay#draw(android.graphics.Canvas, com.google.android.maps.MapView, boolean)
	 */
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		
		//緯度経度から画面上の位置を取得する
		GeoPoint point = new GeoPoint(this.latitudeE6, this.longitudeE6);
		Point pt = mapView.getProjection().toPixels(point, null);
		
		// アイコンの描画
		int w = marker.getIntrinsicWidth();
		int h = marker.getIntrinsicHeight();
		marker.setBounds(pt.x - w/2, pt.y - h/2, pt.x + w/2, pt.y + h/2);
		marker.draw(canvas);
	}
}
