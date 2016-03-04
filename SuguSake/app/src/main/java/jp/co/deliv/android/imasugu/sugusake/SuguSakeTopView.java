package jp.co.deliv.android.imasugu.sugusake;

import jp.co.deliv.android.location.DeviceLocation;
import jp.co.deliv.android.location.LocationDisabledAlert;
import jp.co.deliv.android.location.LocationListenerWithTimeout;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

/**
 * このアクティビティは、スプラッシュ画像を表示してネットワークから位置情報を取得しておき、
 * ShopListViewアクティビティに制御を移す。
 * @author DLV4002
 */
public class SuguSakeTopView extends Activity {

	/**
	 * ユーザの位置情報をネットワークプロバイダから取得した際の緯度
	 */
	private double userLatitude;
	
	/**
	 * ユーザの位置情報をネットワークプロバイダから取得した際の経度
	 */
	private double userLongitude;
	
	private DeviceLocation deviceLocation = null;
	private LocationListenerWithTimeout locationListener = new LocationListenerWithTimeout() {
		
		public void onStatusChanged(String provider, int status, Bundle extras) { }
		public void onProviderEnabled(String provider) { }
		public void onProviderDisabled(String provider) { }

		/**
		 * 位置情報が変化した際に呼び出される
		 */
		public void onLocationChanged(Location location) {

			// 位置情報の取得を停止する。
			deviceLocation.stop();

			// 位置情報を取得する
			userLatitude = location.getLatitude();
			userLongitude = location.getLongitude();
			
			// スプラッシュウインドウを終了しShopListViewアクティビティに遷移する
			Handler hdl = new Handler();
			hdl.postDelayed(new SplashHandler(), 200);
		}
		
		/**
		 * 位置情報取得でタイムアウトが発生したら、GPSによる取得の場合GPSを無効にして再度取得を試みる。
		 * GPS無効の状態でタイムアウトが発生した場合、エラーを表示する。
		 */
		public void onTimeout(DeviceLocation location, Location latestLocation) {
			if(location.gpsEnabled()) {
				location.gpsDisabled();
				try{
					location.start();
				}catch(Exception e){
					LocationDisabledAlert alert = new LocationDisabledAlert(SuguSakeTopView.this);
					alert.show();
				}
			} else {
				LocationDisabledAlert alert = new LocationDisabledAlert(SuguSakeTopView.this);
				alert.show();
			}
		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.topview);

		// Manifestファイルに記述されているバージョン名を取得する
		String packageName = getPackageName();
		PackageInfo packageInfo = null;
		String version = null;
		try {
			packageInfo = getPackageManager().getPackageInfo(packageName, PackageManager.GET_META_DATA);
			version = packageInfo.versionName;
		} catch (NameNotFoundException e) {
			packageInfo = null;
			version = "-----";
		}
		
		// アプリケーション名、バージョン情報を表示
		Resources res = getResources();
		TextView applicationName = (TextView)findViewById(R.id.application_name);
		applicationName.setText(res.getString(R.string.app_name) + " " + version);
		
		// 位置情報取得クラスを初期化
		deviceLocation = new DeviceLocation((LocationManager)getSystemService(LOCATION_SERVICE),
											locationListener);
		// 位置情報の取得タイムアウトを10秒に
		deviceLocation.setTimeout(10000);
	}

	@Override
	protected void onPause() {
		
		// アクティビティがバックグラウンドに移行したため位置情報取得サービスからの受け取りを解除
		// リスナを解除する
		deviceLocation.stop();
		
		super.onPause();
	}

	@Override
	protected void onResume() {
		
		/*
		 * 位置情報の更新
		 */		
		try {
			deviceLocation.start();
		} catch (Exception e) {
			LocationDisabledAlert alert = new LocationDisabledAlert(SuguSakeTopView.this);
			alert.show();
		}
		super.onResume();
	}
	
	class SplashHandler implements Runnable {
		public void run() {
			/*
			 * Intentに緯度経度をつめてShopLitViewを表示する
			 */
			Intent intent = new Intent(SuguSakeTopView.this, ShopListView.class);
			intent.putExtra("USER_LATITUDE", userLatitude);
			intent.putExtra("USER_LONGITUDE", userLongitude);
			startActivity(intent);
			SuguSakeTopView.this.finish();
		}
	}
}
