package jp.co.deliv.android.imasugu.sugusake;

import java.util.ArrayList;
import java.util.List;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import jp.co.deliv.android.imasugu.ShopInfo;
import jp.co.deliv.android.imasugu.sugusake.R;
import jp.co.deliv.android.location.DeviceLocation;
import jp.co.deliv.android.location.LocationDisabledAlert;
import jp.co.deliv.android.location.LocationListenerWithTimeout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * イマすぐシリーズ：すぐカフェ
 * ショップリストビュー
 * 現在位置を受け取り、その位置周辺のカフェを検索し、リストビューに表示するアクティビティ
 * @author Atsushi
 */
public class ShopListView extends Activity {
	
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
	
	private DeviceLocation deviceLocation = null;
	private ProgressDialog progressDialog = null;
	
	/**
	 * 位置情報取得の際にシステムから呼び出されるリスナ。
	 */
	private LocationListenerWithTimeout locationListener = new LocationListenerWithTimeout() {
		
		public void onStatusChanged(String provider, int status, Bundle extras) { }
		public void onProviderEnabled(String provider) { }
		public void onProviderDisabled(String provider) { }

		/**
		 * 位置情報が変化した際に呼び出される
		 */
		public void onLocationChanged(Location location) {

			// 位置情報の取得処理を解除
			deviceLocation.stop();
			
			// 取得した位置情報から緯度経度を取得
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			
			/*
			 * 位置情報が更新されたため、店舗リストを更新する
			 */
        	updateShopListViewItem();	                    	
		}
		
		/**
		 * 位置情報取得でタイムアウトが発生したときの処理
		 */
		public void onTimeout(DeviceLocation location, Location latestLocation) {
			if(location.gpsEnabled()) {
				location.gpsDisabled();
				try{
					location.start();
				}catch(Exception e){
					LocationDisabledAlert alert = new LocationDisabledAlert(ShopListView.this);
					alert.show();
				}
			} else {
				LocationDisabledAlert alert = new LocationDisabledAlert(ShopListView.this);
				alert.show();
			}
		}
	};
		
	/**
	 * 緯度経度を引数としてぐるナビにアクセスするためのURLを組み立てる
	 * @param latitude　緯度
	 * @param longitude　軽度
	 * @return　ぐるナビWEBサービスのレストランAPIアクセスURL
	 */
	private String createGNaviApiUrl(double latitude, double longitude, String option) {
		String tmp = "latitude=" + latitude + "&longitude=" + longitude;
		String url = SuguSakeConst.GNAVI_REST_API_URL
					+ "?"
					+ SuguSakeConst.GNAVI_API_KEY
					+ "&" + tmp
					+ "&" + option;
		Log.d("SHOP_LIST_VIEW", url);
		return  url;
	}	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoplistview);

        /*
         * 呼び出しもとから緯度経度を受け取る
         */
        Intent intent = getIntent();
    	latitude = intent.getDoubleExtra("USER_LATITUDE", SuguSakeConst.DEF_LATITUDE);
    	longitude = intent.getDoubleExtra("USER_LONGITUDE", SuguSakeConst.DEF_LONGITUDE);
        
        /*
         * 空のリストでListViewを初期化する。
         */
        
        // 取得したリストからAdapterを作成
        List<ShopInfo> items = new ArrayList<ShopInfo>();
        shopListAdapter = new ShopListAdapter(this, R.layout.shoplistview_row, items);
        
        // ListViewを取得
    	ListView listView = (ListView)findViewById(R.id.shopListView);
    	
        // ListViewにAdapterをセットする
        listView.setAdapter(shopListAdapter);

        /*
         * 検索結果が０件だったときのビューをセット 
         */
        View emptyView = findViewById(R.id.noshop);
        listView.setEmptyView(emptyView);
        
        // ListViewがクリックされたときに呼び出されるリスナーを登録
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

        	/**
        	 * リストビューに表示されているお店を選択した際に呼び出されるクリックリスナー
        	 */
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ListView listView = (ListView)parent;
				ShopInfo shopInfo = (ShopInfo)listView.getItemAtPosition(position);

				/*
				 * お店詳細情報を表示する画面に遷移する
				 */
				Intent intent = new Intent(ShopListView.this, ShopDetailView.class);
				intent.putExtra("USER_LATITUDE", latitude);
				intent.putExtra("USER_LONGITUDE", longitude);
				intent.putExtra(SuguSakeConst.SHOP_ID, shopInfo.getId());
				intent.putExtra(SuguSakeConst.SHOP_NAME, shopInfo.getName());
				intent.putExtra(SuguSakeConst.SHOP_ADDRESS, shopInfo.getAddress());
				intent.putExtra(SuguSakeConst.SHOP_ACCESS, shopInfo.getAccess());
				intent.putExtra(SuguSakeConst.SHOP_TEL, shopInfo.getTel());
				intent.putExtra(SuguSakeConst.SHOP_OPENHOUR, shopInfo.getOpenHour());
				intent.putExtra(SuguSakeConst.SHOP_HOLIDAY, shopInfo.getHoliday());
				intent.putExtra(SuguSakeConst.SHOP_CATEGORY, shopInfo.getCategory());
				intent.putExtra(SuguSakeConst.SHOP_PR_SHORT, shopInfo.getPrShort());
				intent.putExtra(SuguSakeConst.SHOP_PR_LONG, shopInfo.getPrLong());
				intent.putExtra(SuguSakeConst.SHOP_BUDGET, shopInfo.getBudget());
				intent.putExtra(SuguSakeConst.SHOP_IMAGE_1, shopInfo.getShopImage1Url());
				intent.putExtra(SuguSakeConst.SHOP_IMAGE_2, shopInfo.getShopImage2Url());
				intent.putExtra(SuguSakeConst.SHOP_MOBILE_URL, shopInfo.getShopUrlMobile());
				intent.putExtra(SuguSakeConst.SHOP_MOBILE_COUPON_FLG, shopInfo.getMobileCouponFlg());
				intent.putExtra(SuguSakeConst.SHOP_COUPON_URL, shopInfo.getCouponUrlMobile());
				intent.putExtra(SuguSakeConst.SHOP_LATITUDE, shopInfo.getLatitude());
				intent.putExtra(SuguSakeConst.SHOP_LONGITUDE, shopInfo.getLongitude());
				intent.putExtra(SuguSakeConst.SHOP_DISTANCE, shopInfo.getDistance());
				intent.putExtra(SuguSakeConst.SHOP_EQUIPMENT, shopInfo.getEquipment());
				
				// ShopDetailViewを開始する
				startActivity(intent);
			}
        	
		});
        
        // 位置情報取得設定（タイムアウトは10秒）
        deviceLocation = new DeviceLocation((LocationManager)getSystemService(LOCATION_SERVICE),
        									locationListener);
        deviceLocation.setTimeout(10000);
        
        /*
         * 広告表示設定
         */
        adView = new AdView(this,
        					AdSize.BANNER,
        					(String)getResources().getText(R.string.admob_publisherid));
        adView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        LinearLayout adLayout = (LinearLayout)findViewById(R.id.adLayout);
        adLayout.addView(adView);
        
        // 広告リクエスト
        adRequest = new AdRequest();
        adRequest.addTestDevice(AdRequest.TEST_EMULATOR);
        adRequest.addTestDevice("726A9369B1F4DA6C5BB8DC78AEB38358");	// HTC Desire

        // ダイアログの表示
        createProgressDialog();
        
        /*
         * リストビューに ShopList を取得して表示する。
         */
		updateShopListViewItem();
    }
    
    @Override
	protected void onPause() {
		
		// アクティビティがバックグラウンドに移行したため位置情報取得サービスからの受け取りを解除
		// リスナを解除する
    	deviceLocation.stop();
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		/*
		 * 広告削除
		 */
		if(adView != null) {
			adView.destroy();
			adView = null;
		}
		super.onDestroy();
	}

	/**
     * 店舗リストの更新
     */
    private void updateShopListViewItem() {

    	/*
    	 * このタイミングで広告をリクエスト
    	 */
    	if(adView != null && adRequest != null) {
    		adView.loadAd(adRequest);
    	}
    	
    	// WEBサービスを呼び出してショップリストを取得
    	if (shopListAdapter!=null) shopListAdapter.clear();
    	GetShopListTask task = new GetShopListTask(this, shopListAdapter);
    	
    	// ユーザの位置情報をセット
    	task.setUserLocation(this.latitude, this.longitude);
    	
    	// ダイアログのセット
    	task.setProgressDialog(progressDialog);
    	
    	// ユーザの位置からの距離に応じたお店の情報を取得し、リストにセットされる
    	// 下記の大業態コードを取得します
    	// CTG610 	和風居酒屋
    	// CTG620 	洋風居酒屋
    	// CTG630 	アジア・無国籍居酒屋
    	// CTG640 	ダイニングバー・ビアレストラン
    	// CTG650 	バー・パブ
    	task.execute(createGNaviApiUrl(this.latitude, this.longitude,SuguSakeConst.GNAVI_API_OPTION_1),
    				 createGNaviApiUrl(this.latitude, this.longitude,SuguSakeConst.GNAVI_API_OPTION_2),
    				 createGNaviApiUrl(this.latitude, this.longitude,SuguSakeConst.GNAVI_API_OPTION_3),
    				 createGNaviApiUrl(this.latitude, this.longitude,SuguSakeConst.GNAVI_API_OPTION_4),
    				 createGNaviApiUrl(this.latitude, this.longitude,SuguSakeConst.GNAVI_API_OPTION_5)
    				);
    	
    }
    
    /**
     * オプションメニューの作成（メニュー表示時に一度だけ呼び出される）
     */
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	
    	// 文字列取得のためのリソースを取得
    	Resources res = getResources();
    	
    	// お店情報更新メニューを追加
    	menu.add(Menu.NONE, SuguSakeConst.MENU_ID_SHOPLIST_UPDATE, Menu.NONE, res.getText(R.string.menu_shoplist_update))
    	.setIcon(android.R.drawable.ic_menu_rotate);

    	// バージョン情報メニューを追加
    	menu.add(Menu.NONE, SuguSakeConst.MENU_ID_ABOUT, Menu.NONE, res.getText(R.string.menu_about))
    	.setIcon(android.R.drawable.ic_menu_info_details);
    	
		return super.onCreateOptionsMenu(menu);
	}

    /**
     * オプションメニューのメニューが選択された際に呼び出される
     */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		boolean ret = true;
		switch(item.getItemId()) {
		
		// お店情報更新メニュー選択時
		case SuguSakeConst.MENU_ID_SHOPLIST_UPDATE :

			// ダイアログの表示
			createProgressDialog();

			// 一旦位置情報を更新し店舗リストを更新する
			updateUserLocation();			
			ret = true;
			break;
				
		// バージョン情報選択時
		case SuguSakeConst.MENU_ID_ABOUT:
			SuguSakeUtils.showAboutInformation(this);
			break;
			
		// デフォルト処理
		default:
			ret = super.onOptionsItemSelected(item); 
		}
		return ret;
	}
	
	/**
	 * ユーザの位置情報をGPSを使用して更新し、ShopListViewクラスのユーザ位置情報インスタンス変数に
	 * 緯度経度をそれぞれセットする。
	 * このメソッドは、メニューから位置情報更新を選択した際に呼び出される。画面起動時は、WIFIなどの
	 * ネットワークからの位置情報を取得するのみ。
	 */
	private void updateUserLocation() {
		
		// 位置情報の更新を開始
		try {
			deviceLocation.start();
		} catch (Exception e) {
			LocationDisabledAlert alert = new LocationDisabledAlert(ShopListView.this);
			alert.show();
		}
	}

	private void createProgressDialog() {
		
		if(progressDialog != null) {
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
		progressDialog.setMessage(res.getText(R.string.msg_info_find_store));

		// プログレスバー表示
		progressDialog.show();		
	}
}