package jp.co.deliv.android.imasugu.sugucafe;

import jp.co.deliv.android.location.DeviceLocation;
import jp.co.deliv.android.location.LocationDisabledAlert;
import jp.co.deliv.android.location.LocationListenerWithTimeout;
import jp.co.deliv.android.location.ProviderNotFoundException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * このアクティビティは、スプラッシュ画像を表示してネットワークから位置情報を取得しておき、 ShopListViewアクティビティに制御を移す。
 * 
 * @author DLV4002
 */
public class SuguCafeTopView extends Activity {

	/**
	 * ユーザの位置情報（緯度・経度）
	 */
	private DeviceLocation deviceLocation = null;
	private ProgressDialog progressDialog = null;
	private LocationListenerWithTimeout locationListener = new LocationListenerWithTimeout() {

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onProviderDisabled(String provider) {
		}

		/**
		 * 位置情報が変化した際に呼び出される
		 */
		public void onLocationChanged(Location location) {

			// 位置情報の取得処理を解除
			deviceLocation.stop();

			// 取得した位置情報から緯度経度を取得
			ApplicationData appContext = (ApplicationData) getApplicationContext();
			appContext.setUserLatitude(location.getLatitude());
			appContext.setUserLongitude(location.getLongitude());
			Intent intent = new Intent(SuguCafeTopView.this,
					SuguCafeTabMenuActivity.class);
			appContext.setSearchCondition("");
			appContext.setUpdateLocation(0);
			startActivity(intent);
			progressDialog.dismiss();

			/*
			 * 位置情報が更新されたため、店舗リストを更新する
			 */
		}

		/**
		 * 位置情報取得でタイムアウトが発生したときの処理
		 */
		public void onTimeout(DeviceLocation location, Location latestLocation) {
			if (location.gpsEnabled()) {
				location.gpsDisabled();
				try {
					location.start();
				} catch (Exception e) {
					LocationDisabledAlert alert = new LocationDisabledAlert(
							SuguCafeTopView.this);
					progressDialog.dismiss();
					alert.show();
				}
			} else {
				LocationDisabledAlert alert = new LocationDisabledAlert(
						SuguCafeTopView.this);
				progressDialog.dismiss();
				alert.show();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.topview);

		// Manifestファイルに記述されているバージョン名を取得する
		Resources res = getResources();

		String packageName = getPackageName();
		PackageInfo packageInfo = null;
		String version = null;
		try {
			packageInfo = getPackageManager().getPackageInfo(packageName,
					PackageManager.GET_META_DATA);
			version = packageInfo.versionName;
		} catch (NameNotFoundException e) {
			packageInfo = null;
			version = "-----";
		}

		// アプリケーション名、バージョン情報を表示
		TextView applicationName = (TextView) findViewById(R.id.application_name);
		applicationName.setText(res.getString(R.string.app_name) + " "
				+ version);
	}

	@Override
	protected void onPause() {
		// ロケーションサービスをストップする
		if (deviceLocation != null)
			deviceLocation.stop();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	/**
	 * move to map with user location 
	 * @param v
	 */
	public void btnSeachAround_onClick(View v) {
		ApplicationData appContext = (ApplicationData) getApplicationContext();
		appContext.setTypeSearch(SuguCafeConst.SEARCH_CAFE_ARROUND);
		createProgressDialog();
	}

	/**
	 * Comment : move to map without user location
	 * @param v
	 */
	public void btnNoSeach_OnClick(View v) {
		// createProgressDialog();
		ApplicationData appContext = (ApplicationData) getApplicationContext();
		// 目的地でカフェる
		appContext.setTypeSearch(SuguCafeConst.SEARCH_CAFE_WITH_ADDRESS);
		appContext.setSearchCondition("");
		appContext.setUpdateLocation(0);
		Intent intent = new Intent(SuguCafeTopView.this,
				SuguCafeTabMenuActivity.class);
		startActivity(intent);

	}

	/**
	 * Comment : create Progress dialog and start GPS
	 */
	private void createProgressDialog() {

		if (progressDialog != null) {
			progressDialog = null;
		}

		// ProgressDialog（プログレスバー）の設定
		progressDialog = new ProgressDialog(this);

		// キャンセル設定
		progressDialog.setIndeterminate(false);

		// プログレスバーのスタイルをセット
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

		// 表示するメッセージをセット
		Resources res = getResources();
		progressDialog.setMessage(res.getText(R.string.msg_waiting));

		// プログレスバー表示
		progressDialog.show();
		deviceLocation = new DeviceLocation(
				(LocationManager) getSystemService(LOCATION_SERVICE),
				locationListener);
		// deviceLocation.enableGps(true);
		deviceLocation.setTimeout(SuguCafeConst.GPS_DETECT_TIME_OUT);
		try {
			deviceLocation.start();
		} catch (ProviderNotFoundException e) {
			Log.e("DeviceLoc", e.getMessage());
		}
	}
}
