package jp.co.deliv.android.imasugu.sugucafe;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jp.co.deliv.android.imasugu.ShopInfo;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.google.android.maps.GeoPoint;

/**
 * list view for search address
 * 
 * @author 120007HTT
 * 
 */
public class ShopListSearchView extends Activity {

	/**
	 * 広告（admob）
	 */
	private AdView adView = null;
	private AdRequest adRequest = null;

	/**
	 * ListViewにセットするアダプタ
	 */
	private ShopListAdapter shopListAdapter = null;

	/**
	 * ユーザの位置情報（緯度・経度）
	 */
	private double latitude;
	private double longitude;

	private ProgressDialog progressDialog = null;

	/**
	 * search condition ,
	 */
	private String searchCondition;
	private String strTmp;

	/**
	 * check update of location
	 */
	private int update;
	private int updateTmp;

	/**
	 * location of address
	 */
	private GeoPoint pointAddress;

	/**
	 * is address ok
	 */
	private Boolean isAdressOk = false;

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

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shoplistview);

		// initial data
		ApplicationData appContext = (ApplicationData) getApplicationContext();
		latitude = appContext.getUserLatitude();
		longitude = appContext.getUserLongitude();
		strTmp = "";
		updateTmp = 0;

		TextView txt_noshop = (TextView) findViewById(R.id.application_name);
		txt_noshop.setText(getString(R.string.msg_info_noshop_dest));

		adView = new AdView(this, AdSize.BANNER, (String) getResources()
				.getText(R.string.admob_publisherid));
		adView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		LinearLayout adLayout = (LinearLayout) findViewById(R.id.adLayout);
		adLayout.addView(adView);

		// 広告リクエスト
		adRequest = new AdRequest();
		adRequest.addTestDevice(AdRequest.TEST_EMULATOR);
		adRequest.addTestDevice("59A3C630685B6B1AEBF8AF2A027FBF35");

		// set on click clear
		ImageButton btn_clear = (ImageButton) findViewById(R.id.btn_clear);
		btn_clear.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				EditText txt_seach = (EditText) findViewById(R.id.txt_seach);
				txt_seach.setText("");
			}
		});
		// set EditText hint
		EditText txt_seach = (EditText) findViewById(R.id.txt_seach);
		if (appContext.getTypeSearch() == SuguCafeConst.SEARCH_CAFE_ARROUND) {
			txt_seach.setHint(getString(R.string.you_are_here_search));
		} else {
			txt_seach.setHint(getString(R.string.search_with_address));
		}
		txt_seach.setText(searchCondition);
		// handle on key click in soft keyboard
		txt_seach.setOnKeyListener(new OnKeyListener() {

			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER
						&& event.getAction() == KeyEvent.ACTION_DOWN) {
					EditText txt_seach = (EditText) findViewById(R.id.txt_seach);
					InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					mgr.hideSoftInputFromWindow(txt_seach.getWindowToken(), 0);
					Search();
					return true;
				}
				return false;
			}
		});
		// set on click search
		Button btn_seach = (Button) findViewById(R.id.btn_seach);

		/**
		 * click on button search
		 */
		btn_seach.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Search();

			}
		});

		List<ShopInfo> items = new ArrayList<ShopInfo>();
		shopListAdapter = new ShopListAdapter(this, R.layout.shoplistview_row,
				items);

		// ListViewを取得
		ListView listView = (ListView) findViewById(R.id.shopListView);

		// ListViewにAdapterをセットする
		listView.setAdapter(shopListAdapter);

		/*
		 * 検索結果が０件だったときのビューをセット
		 */
		View emptyView = findViewById(R.id.noshop);
		listView.setEmptyView(emptyView);
		/**
		 * touch on empty view will close soft keyboard
		 */
		emptyView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				EditText txt_seach = (EditText) findViewById(R.id.txt_seach);
				InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				mgr.hideSoftInputFromWindow(txt_seach.getWindowToken(), 0);
				return false;
			}
		});

		// ListViewがクリックされたときに呼び出されるリスナーを登録
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			/**
			 * リストビューに表示されているお店を選択した際に呼び出されるクリックリスナー
			 */
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ListView listView = (ListView) parent;
				ShopInfo shopInfo = (ShopInfo) listView
						.getItemAtPosition(position);

				/*
				 * お店詳細情報を表示する画面に遷移する
				 */
				Intent intent = new Intent(ShopListSearchView.this,
						ShopDetailView.class);
				intent.putExtra("USER_LATITUDE", latitude);
				intent.putExtra("USER_LONGITUDE", longitude);
				intent.putExtra(SuguCafeConst.SHOP_ID, shopInfo.getId());
				intent.putExtra(SuguCafeConst.SHOP_NAME, shopInfo.getName());
				intent.putExtra(SuguCafeConst.SHOP_ADDRESS,
						shopInfo.getAddress());
				intent.putExtra(SuguCafeConst.SHOP_ACCESS, shopInfo.getAccess());
				intent.putExtra(SuguCafeConst.SHOP_TEL, shopInfo.getTel());
				intent.putExtra(SuguCafeConst.SHOP_OPENHOUR,
						shopInfo.getOpenHour());
				intent.putExtra(SuguCafeConst.SHOP_HOLIDAY,
						shopInfo.getHoliday());
				intent.putExtra(SuguCafeConst.SHOP_CATEGORY,
						shopInfo.getCategory());
				intent.putExtra(SuguCafeConst.SHOP_PR_SHORT,
						shopInfo.getPrShort());
				intent.putExtra(SuguCafeConst.SHOP_PR_LONG,
						shopInfo.getPrLong());
				intent.putExtra(SuguCafeConst.SHOP_BUDGET, shopInfo.getBudget());
				intent.putExtra(SuguCafeConst.SHOP_IMAGE_1,
						shopInfo.getShopImage1Url());
				intent.putExtra(SuguCafeConst.SHOP_IMAGE_2,
						shopInfo.getShopImage2Url());
				intent.putExtra(SuguCafeConst.SHOP_MOBILE_URL,
						shopInfo.getShopUrlMobile());
				intent.putExtra(SuguCafeConst.SHOP_MOBILE_COUPON_FLG,
						shopInfo.getMobileCouponFlg());
				intent.putExtra(SuguCafeConst.SHOP_COUPON_URL,
						shopInfo.getCouponUrlMobile());
				intent.putExtra(SuguCafeConst.SHOP_LATITUDE,
						shopInfo.getLatitude());
				intent.putExtra(SuguCafeConst.SHOP_LONGITUDE,
						shopInfo.getLongitude());
				intent.putExtra(SuguCafeConst.SHOP_DISTANCE,
						shopInfo.getDistance());

				// ShopDetailViewを開始する
				startActivity(intent);
			}

		});

		/**
		 * when touch on list view , soft keyboard will close
		 */
		listView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				EditText txt_seach = (EditText) findViewById(R.id.txt_seach);
				InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				mgr.hideSoftInputFromWindow(txt_seach.getWindowToken(), 0);
				return false;
			}
		});
		if (adView != null && adRequest != null) {
			adView.loadAd(adRequest);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ShowData();
	}

	@Override
	protected void onPause() {

		// アクティビティがバックグラウンドに移行したため位置情報取得サービスからの受け取りを解除
		// リスナを解除する
		// deviceLocation.stop();
		EditText txt_seach = (EditText) findViewById(R.id.txt_seach);
		InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.hideSoftInputFromWindow(txt_seach.getWindowToken(), 0);
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		/*
		 * 広告削除
		 */
		if (adView != null) {
			adView.destroy();
			adView = null;
		}
		super.onDestroy();
	}

	/**
	 * 店舗リストの更新
	 */
	private void updateShopListViewItem() {
		// WEBサービスを呼び出してショップリストを取得
		GetShopListTask task = new GetShopListTask(this, shopListAdapter);

		// ユーザの位置情報をセット

		// ダイアログのセット
		task.setProgressDialog(progressDialog);
		
		// ユーザの位置からの距離に応じたお店の情報を取得し、リストにセットされる
		if (searchCondition == "") {
			task.setUserLocation(this.latitude, this.longitude);
			task.execute(createGNaviApiUrl(this.latitude, this.longitude));
		} else {
			task.setUserLocation(pointAddress.getLatitudeE6() / 1E6,
					pointAddress.getLongitudeE6() / 1E6);
			task.execute(createGNaviApiUrl(pointAddress.getLatitudeE6() / 1E6,
					pointAddress.getLongitudeE6() / 1E6));
		}
	}

	/**
	 * Comment : create Progress dialog
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
	 * Comment : get Latitude and Longitude from address
	 * 
	 * @param address
	 */
	private void getFromLocation(String address) {
		isAdressOk = false;
		Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
		try {
			List<Address> addresses = geoCoder.getFromLocationName(address, 1);
			if (addresses.size() > 0) {
				pointAddress = new GeoPoint((int) (addresses.get(0)
						.getLatitude() * 1E6), (int) (addresses.get(0)
						.getLongitude() * 1E6));
				isAdressOk = true;

			} else {
				new AlertDialog.Builder(this)
						.setTitle("")
						.setMessage(getString(R.string.msg_invalid_address))
						.setPositiveButton(getString(android.R.string.ok),
								new DialogInterface.OnClickListener() {
									public void onClick(
											final DialogInterface dialog,
											final int which) {
									}
								}).create().show();
			}
		} catch (Exception ee) {
			Log.d("Bug", ee.toString());
			new AlertDialog.Builder(this)
					.setTitle(getString(R.string.msg_network_err_title))
					.setMessage(getString(R.string.msg_network_err_message))
					.setPositiveButton(
							getString(R.string.msg_network_err_button_text),
							new DialogInterface.OnClickListener() {
								public void onClick(
										final DialogInterface dialog,
										final int which) {
								}
							}).create().show();
		}
	}

	/**
	 * Comment : do Search
	 */
	private void Search() {
		EditText txt_seach = (EditText) findViewById(R.id.txt_seach);
		ApplicationData appContext = (ApplicationData) getApplicationContext();
		InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.hideSoftInputFromWindow(txt_seach.getWindowToken(), 0);
		if (txt_seach.getText().toString().length() > 0) {
			getFromLocation(txt_seach.getText().toString());
			if (isAdressOk == true) {
				isAdressOk = false;
				searchCondition = txt_seach.getText().toString();
				appContext.setSearchCondition(searchCondition);
				appContext.setUserLatitude(pointAddress.getLatitudeE6() / 1E6);
				appContext
						.setUserLongitude(pointAddress.getLongitudeE6() / 1E6);
				strTmp = searchCondition;
				createProgressDialog(R.string.msg_info_find_store_dest);
				updateShopListViewItem();
			} else {
				// msg if address is not ok
			}

		}
	}
	
	/**
	 * show data
	 */
	private void ShowData() {
		ApplicationData appContext = (ApplicationData) getApplicationContext();
		searchCondition = appContext.getSearchCondition();
		EditText txt_seach = (EditText) findViewById(R.id.txt_seach);
		txt_seach.setText(searchCondition);
		update = appContext.getUpdateLocation();
		if (update != updateTmp||searchCondition !=strTmp) {
			updateTmp = update;
			strTmp = searchCondition;
			Log.d("get data", "List View");
			shopListAdapter.clear();
			List<ShopInfo> saveResult = appContext.getShopResult();
			for (ShopInfo item : saveResult) {
				shopListAdapter.add(item);
			}
		}
	}
}
