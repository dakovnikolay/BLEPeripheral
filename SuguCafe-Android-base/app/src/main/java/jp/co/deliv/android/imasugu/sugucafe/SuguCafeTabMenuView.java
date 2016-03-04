package jp.co.deliv.android.imasugu.sugucafe;

import jp.co.deliv.android.location.DeviceLocation;
import jp.co.deliv.android.location.LocationDisabledAlert;
import jp.co.deliv.android.location.LocationListenerWithTimeout;
import jp.co.deliv.android.location.ProviderNotFoundException;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

/**
 * Tab control
 * 
 * @author 120007HTT
 * 
 */
public class SuguCafeTabMenuView extends TabActivity {

	/**
	 * handle create menu option
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// 文字列取得のためのリソースを取得

		// お店情報更新メニューを追加
		TabHost tabHost = getTabHost();
		int index = tabHost.getCurrentTab();

		menu.add(Menu.NONE, SuguCafeConst.MENU_ID_SHOPLIST_UPDATE, Menu.NONE,
				getText(R.string.tab_name_update_location));
		// if tab is map view , it will show menu to link to list . if tab is
		// list view , it will show menu to link to map view
		if (index == 1) {
			menu.add(Menu.NONE, SuguCafeConst.MENU_ID_Move_To_Map, Menu.NONE,
					getText(R.string.tab_name_map));
		} else {
			menu.add(Menu.NONE, SuguCafeConst.MENU_ID_Move_To_List, Menu.NONE,
					getText(R.string.tab_name_list));
		}

		// バージョン情報メニューを追加
		menu.add(Menu.NONE, SuguCafeConst.MENU_ID_ABOUT, Menu.NONE,
				getText(R.string.tab_name_info));

		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * handle click on item of menu option
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		boolean ret = true;
		TabHost tabHost = getTabHost();
		switch (item.getItemId()) {

		// お店情報更新メニュー選択時
		case SuguCafeConst.MENU_ID_SHOPLIST_UPDATE:
			// update GPS
			createProgressDialog(R.string.msg_waiting);
			deviceLocation = new DeviceLocation(
					(LocationManager) getSystemService(LOCATION_SERVICE),
					locationListener);
			deviceLocation.enableGps(true);
			deviceLocation.setTimeout(SuguCafeConst.GPS_DETECT_TIME_OUT);
			try {
				deviceLocation.start();
			} catch (ProviderNotFoundException e) {
				e.printStackTrace();
			}
			ret = true;
			break;

		// バージョン情報選択時
		case SuguCafeConst.MENU_ID_ABOUT:
			SuguCafeUtils.showAboutInformation(this);
			break;

		case SuguCafeConst.MENU_ID_Move_To_List:
			tabHost.setCurrentTab(1);
			break;
		case SuguCafeConst.MENU_ID_Move_To_Map:
			tabHost.setCurrentTab(0);
			// デフォルト処理
		default:
			ret = super.onOptionsItemSelected(item);
		}
		return ret;
	}

	/**
	 * device to get location
	 */
	private DeviceLocation deviceLocation = null;
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
			appContext.setSearchCondition("");

			// set update change
			appContext.UpdateLocation();
			progressDialog.dismiss();
			createProgressDialog(R.string.msg_info_find_store);
			updateShopListItem(location.getLatitude(), location.getLongitude());
			
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
							SuguCafeTabMenuView.this);
					alert.show();
					progressDialog.dismiss();
				}
			} else {
				LocationDisabledAlert alert = new LocationDisabledAlert(
						SuguCafeTabMenuView.this);
				alert.show();
				progressDialog.dismiss();
			}
		}
	};

	/*
	 * バックグラウンドタスク実行中に表示するプログレスダイアログ
	 */
	private ProgressDialog progressDialog = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_menu_view);
		TabHost tabHost = getTabHost();

		// Search with input tab
		Intent intentSeach;
		intentSeach = new Intent(this, ShopMapSearchView.class);
		TabSpec tabSeach = tabHost
				.newTabSpec("NoSeach")
				.setContent(intentSeach)
				.setIndicator(
						prepareTabView(getString(R.string.tab_name_map), 0,
								SuguCafeConst.TAB_PERFORM_TEXT_ONLY));
		// List tab
		Intent intentList = new Intent(this, ShopListSearchView.class);
		TabSpec tabList = tabHost
				.newTabSpec("List")
				.setContent(intentList)
				.setIndicator(
						prepareTabView(getString(R.string.tab_name_list), 0,
								SuguCafeConst.TAB_PERFORM_TEXT_ONLY));
		// add all tabs
		tabHost.addTab(tabSeach);
		tabHost.addTab(tabList);

		// set Windows tab as default (zero based)
		tabHost.setCurrentTab(0);
	}

	/**
	 * Comment : modify each perform of tab in tabHost
	 * 
	 * @param text
	 * @param resId
	 * @param check
	 * @return view
	 */
	private View prepareTabView(String text, int resId, int check) {
		View view = LayoutInflater.from(this).inflate(R.layout.tabs, null);
		ImageView iv_Tab = (ImageView) view.findViewById(R.id.img_tab);
		TextView tv_TabName = (TextView) view.findViewById(R.id.txt_tab_name);
		// perform image only
		if (check == SuguCafeConst.TAB_PERFORM_IMAGE_ONLY) {
			iv_Tab.setImageResource(resId);
			tv_TabName.setVisibility(View.GONE);
		}
		// perform text only
		if (check == SuguCafeConst.TAB_PERFORM_TEXT_ONLY) {
			tv_TabName.setText(text);
			iv_Tab.setVisibility(View.GONE);
		}
		// perform image and text
		if (check == SuguCafeConst.TAB_PERFORM_TEXT_AND_IMAGE) {
			tv_TabName.setText(text);
			iv_Tab.setImageResource(resId);
		}

		return view;
	}

	/**
	 * Create Progress Dialog and start GPS to get location
	 */
	private void createProgressDialog(int idMessage) {

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
		progressDialog.setMessage(res.getText(idMessage));

		// プログレスバー表示
		progressDialog.show();
		
	}
	/**
	 * Get shop list
	 * 
	 * @param latitude
	 * @param longitude
	 */
	private void updateShopListItem(double latitude, double longitude) {

		// WEBサービスを呼び出してショップリストを取得
		GetShopListInTab task = new GetShopListInTab(this);
		// ダイアログのセット
		task.setProgressDialog(progressDialog);
		// ユーザの位置からの距離に応じたお店の情報を取得し、リストにセットされる
		task.setUserLocation(latitude, longitude);
		task.execute(createGNaviApiUrl(latitude, longitude));
	}

	/**
	 * 緯度経度を引数としてぐるナビにアクセスするためのURLを組み立てる
	 * 
	 * @param latitude
	 *            　緯度
	 * @param longitude
	 *            　軽度
	 * @return　ぐるナビWEBサービスのレストランAPIアクセスURL
	 */
	private String createGNaviApiUrl(double latitude, double longitude) {
		String tmp = "latitude=" + latitude + "&longitude=" + longitude;
		String url;
		url = SuguCafeConst.GNAVI_REST_API_URL + "?"
				+ SuguCafeConst.GNAVI_API_KEY + "&" + tmp + "&"
				+ SuguCafeConst.GNAVI_API_OPTION;
		Log.d("SHOP_LIST_VIEW", url);
		return url;
	}
}
