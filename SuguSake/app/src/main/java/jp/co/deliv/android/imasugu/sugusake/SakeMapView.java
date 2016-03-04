package jp.co.deliv.android.imasugu.sugusake;

import java.util.List;

import jp.co.deliv.android.imasugu.sugusake.R;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.content.Intent;
import android.os.Bundle;

public class SakeMapView extends MapActivity {

	/*
	 * 広尾駅の緯度経度
	 */
	private static final double HIRO_LATITUDE = 35.652937;
	private static final double HIRO_LONGITUDE = 139.722319;
	
	/*
	 * 渋谷駅の緯度経度
	 */
	private static final double SHIBUYA_LATITUDE = 35.658726;
	private static final double SHIBUYA_LONGITUDE = 139.701322;

	/*
	 * ユーザの位置情報
	 */
	private double userLatitude;
	private double userLongitude;
	
	/*
	 * 目的地（お店等）の位置情報
	 */
	private double destLatitude;
	private double destLongitude;
	
	private MapView mapView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.sakemapview);
	}
	
	@Override
	protected void onResume() {
		super.onResume();

		/*
		 * 呼び出し元の画面からユーザの位置情報と目的地の位置情報を取得する
		 */
		Intent intent = getIntent();
		userLatitude = intent.getDoubleExtra("USER_LATITUDE", HIRO_LATITUDE);
		userLongitude = intent.getDoubleExtra("USER_LONGITUDE", HIRO_LONGITUDE);
		destLatitude = intent.getDoubleExtra("DESTINATION_LATITUDE", SHIBUYA_LATITUDE);
		destLongitude = intent.getDoubleExtra("DESTINATION_LONGITUDE", SHIBUYA_LONGITUDE);
		
		// ２点間の中心を求める
		double lat, lng;
		lat = (userLatitude + destLatitude) / 2.0;
		lng = (userLongitude + destLongitude) / 2.0;
		
		// コントロールの取得
		mapView = (MapView)findViewById(R.id.mapView);
		
		// ズームレベル
		//mapView.getController().setZoom(DEFAULT_ZOOM_LEVEL);

		// ユーザ現在地と目的地の中心に移動
		GeoPoint pos = new GeoPoint((int)(lat * 1E6), (int)(lng * 1E6));
		mapView.getController().animateTo(pos);
		
		// ２点が表示されるようにズームレベルを設定する
		fitZoomLevel();
		
		// ユーザの現在位置とお店のオーバーレイアイテムを追加する
		MapItem userLocationItem = new MapItem(userLatitude, userLongitude, getResources().getDrawable(R.drawable.user));
		MapItem shopLocationItem = new MapItem(destLatitude, destLongitude, getResources().getDrawable(R.drawable.home));
		List<Overlay> mapOverlays = mapView.getOverlays();
		if(!mapOverlays.contains(userLocationItem)) {
			mapOverlays.add(userLocationItem);
		}
		if(!mapOverlays.contains(shopLocationItem)) {
			mapOverlays.add(shopLocationItem);
		}
		mapView.invalidate();
	}

	/**
	 * 緯度経度をもとに適切なズームレベルをセットする
	 */
	private void fitZoomLevel() {
		double latSpan = Math.abs(this.userLatitude - this.destLatitude);
		double lngSpan = Math.abs(this.userLongitude - this.destLongitude);
		
		// 必ず収まるように２０％増やす
		latSpan *= 1.2;
		lngSpan *= 1.2;
		
		// ズームレベルをセットする
		mapView.getController().zoomToSpan((int)Math.round(latSpan * 1E6), (int)Math.round(lngSpan * 1E6));
	}


	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
