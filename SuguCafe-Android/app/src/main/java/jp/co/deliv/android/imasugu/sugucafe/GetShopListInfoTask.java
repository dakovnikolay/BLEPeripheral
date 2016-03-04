package jp.co.deliv.android.imasugu.sugucafe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.Overlay;

import jp.co.deliv.android.geocoding.GeoCodingUtils;
import jp.co.deliv.android.http.HttpClient;
import jp.co.deliv.android.http.NetworkErrorAlertDialog;
import jp.co.deliv.android.imasugu.ShopInfo;
import jp.co.deliv.android.imasugu.customize.CustomItemizedOverlay;
import jp.co.deliv.android.imasugu.customize.CustomOverlayItem;
import jp.co.deliv.android.imasugu.webapi.ShopInfoAPIPaser;
import jp.co.deliv.android.imasugu.webapi.gurunavi.GuruNaviAPIParser;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MotionEvent;

/**
 * get and show data on map view
 * 
 * @author 120007HTT
 * 
 */
public class GetShopListInfoTask extends
		AsyncTask<String, Void, List<ShopInfo>> {
	/*
	 * バックグラウンドタスクの呼び出し元Activity
	 */
	private MapActivity activity = null;

	/**
	 * object to control Balloon
	 */
	private CustomItemizedOverlay<CustomOverlayItem> itemizedOverlay;

	/**
	 * picture of user and location
	 */
	private Drawable drwUser;
	private Drawable drwLocation = null;
	/*
	 * バックグラウンドタスク実行中に表示するプログレスダイアログ
	 */
	private ProgressDialog progressDialog = null;
	/*
	 * ListViewに関連付けられているAdapterクラス
	 */
	private TapControlledMapView mapview = null;

	/*
	 * ユーザの位置情報
	 */
	private double userLatitude;
	private double userLongitude;

	/**
	 * ユーザの位置情報をセットしておく。ここでセットする位置情報は、お店との距離計算に使用される。
	 * 
	 * @param latitude
	 *            緯度
	 * @param longitude
	 *            経度
	 */
	public void setUserLocation(double latitude, double longitude) {
		this.userLatitude = latitude;
		this.userLongitude = longitude;
	}

	/**
	 * 処理中に表示するプログレスダイアログの設定
	 * 
	 * @param progressDialog
	 */
	public void setProgressDialog(ProgressDialog progressDialog) {
		this.progressDialog = progressDialog;
	}

	/**
	 * BackgroundTaskクラス（AsyncTaskクラスの派生クラス）
	 * 
	 * @param activity
	 */
	public GetShopListInfoTask(MapActivity activity,
			TapControlledMapView mapview, Drawable drwLocation, Drawable user) {
		this.activity = activity;
		this.mapview = mapview;
		this.drwLocation = drwLocation;
		this.drwUser = user;
	}

	/**
	 * バックグラウンド処理の前処理
	 */
	@Override
	protected void onPreExecute() {

		/*
		 * 進捗状況を表すプログレスバーの設定
		 */
		if (progressDialog == null) {
			// ProgressDialog（プログレスバー）の設定
			progressDialog = new ProgressDialog(activity);

			// キャンセル設定
			progressDialog.setIndeterminate(false);

			// プログレスバーのスタイルをセット
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

			// 表示するメッセージをセット
			Resources res = activity.getResources();
			progressDialog
					.setMessage(res.getText(R.string.msg_info_find_store));

			// プログレスバー表示
			progressDialog.show();
		}
	}

	/**
	 * 引数で渡されるURLにアクセスし、取得できたXMLパースしてTimeLineItemListを作成する このメソッドが非同期で実行される。
	 */
	@Override
	protected List<ShopInfo> doInBackground(String... params) {

		String url = params[0]; // URLを取得する

		// ネットワークの接続状態を確認する
		if (!HttpClient.isNetworkConnected(activity)) {
			// 接続されていない場合は、以降の処理を行わない
			return null;
		}

		// XMLを取得する
		ShopInfoAPIPaser xmlParser = null;
		xmlParser = (ShopInfoAPIPaser) new GuruNaviAPIParser(url);

		// XMLを解析してShopInfoオブジェクトのリストにパースする
		List<ShopInfo> result = xmlParser.parse();

		// エラーが発生してデータが取得できなかったときはnullを返却する
		if (result == null) {
			return null;
		}

		/*
		 * 取得したお店情報の位置情報とユーザの位置情報から距離を計算する
		 */
		for (ShopInfo shop : result) {
			double shopLat = shop.getLatitude();
			double shopLng = shop.getLongitude();
			double distance = GeoCodingUtils.getDistance(this.userLatitude,
					this.userLongitude, shopLat, shopLng);
			shop.setDistance(distance);
		}

		/*
		 * ユーザの位置情報からの距離に応じて並び替えを行う
		 */
		Collections.sort(result, new Comparator<ShopInfo>() {
			/*
			 * ShopInfoクラスを距離の昇順に並び替える
			 */
			public int compare(ShopInfo shop1, ShopInfo shop2) {
				double d1 = shop1.getDistance();
				double d2 = shop2.getDistance();
				if (d1 == d2) {
					return 0;
				}
				if (d1 < d2) {
					return -1;
				}
				return 1;
			}
		});

		return result;
	}

	/**
	 * バックグラウンド処理の後処理 この処理はUIスレッドに戻っているはず。
	 */
	@Override
	protected void onPostExecute(List<ShopInfo> result) {

		// プログレスダイアログ終了
				try {
					this.progressDialog.dismiss();
				} catch (Exception e) {
					Log.d("GetShopListTask", "プログレスダイアログ終了で例外：" + e.getMessage());
					return;
				}

				/**
				 * バックグラウンドの処理結果をUIに表示
				 */
				GeoPoint gp_location = null;
				GeoPoint gp_user = null;
				// clear all overlays and layout on map
				mapview.removeAllViewsInLayout();
				mapview.getOverlays().clear();

				// show user position
				gp_user = new GeoPoint((int) (userLatitude * 1E6),
						(int) (userLongitude * 1E6));
				MapItem userLocationItem = new MapItem(userLatitude, userLongitude,
						this.drwUser);
				List<Overlay> mapOverlays = mapview.getOverlays();
				if (!mapOverlays.contains(userLocationItem)) {
					mapOverlays.add(userLocationItem);
				}
				mapview.getController().animateTo(gp_user);

				itemizedOverlay = new CustomItemizedOverlay<CustomOverlayItem>(
						this.drwLocation, mapview, activity);
				mapview.setOnSingleTapListener(new OnSingleTapListener() {

					public boolean onSingleTap(MotionEvent e) {
						itemizedOverlay.hideAllBalloons();
						return false;
					}
				});
				
				ApplicationData appContext = (ApplicationData) activity.getApplicationContext(); //application context
				List<ShopInfo> saveResult = new ArrayList<ShopInfo>(); // for saving result data of shop
				
				if (result != null) {
					/*
					 * XMLをパースした結果のShopInfoリストをアダプタにセットする
					 */
					HashSet<String> shopIdSet = new HashSet<String>();
					if (result.size() > 0) {

						gp_location = null;

						for (ShopInfo item : result) {
							if (!shopIdSet.contains(item.getId())) {
								saveResult.add(item); // add to save result
								shopIdSet.add(item.getId());// 同じ店舗の管理リスト
								gp_location = new GeoPoint(
										(int) (item.getLatitude() * 1E6),
										(int) (item.getLongitude() * 1E6));
								// set data to show
								CustomOverlayItem overlayItem = new CustomOverlayItem(
										gp_location, item.getCategory(),
										item.getName(), item.getDistance(),
										item.getOpenHour(), item.getMobileCouponFlg(),
										item.getShopImage1Url(),
										item.getShopImage2Url());
								overlayItem.setLatitude(userLatitude);
								overlayItem.setLongitude(userLongitude);
								overlayItem.setId(item.getId());
								overlayItem.setAddress(item.getAddress());
								overlayItem.setAccess(item.getAccess());
								overlayItem.setTel(item.getTel());
								overlayItem.setHoliday(item.getHoliday());
								overlayItem.setPrShort(item.getPrShort());
								overlayItem.setPrLong(item.getPrLong());
								overlayItem.setBudget(item.getBudget());
								overlayItem.setShopUrlMobile(item.getShopUrlMobile());
								overlayItem.setCouponUrlMobile(item
										.getCouponUrlMobile());
								overlayItem.setShopLatitude(item.getLatitude());
								overlayItem.setShoplongitude(item.getLongitude());
								// add data to control itemize overlay
								itemizedOverlay.addOverlay(overlayItem);
							}
						}
						shopIdSet.clear();
						shopIdSet = null;
						mapOverlays = mapview.getOverlays();

						// add itemize on mapview
						mapOverlays.add(itemizedOverlay);
					}
					appContext.setShopResult(saveResult); // save result
					mapview.invalidate();
					mapview.getController().setZoom(17);
				} else {
					NetworkErrorAlertDialog alert = new NetworkErrorAlertDialog(
							this.activity);
					alert.show();
				}
	}
}
