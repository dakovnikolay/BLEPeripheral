package jp.co.deliv.android.imasugu.sugusake;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import jp.co.deliv.android.geocoding.GeoCodingUtils;
import jp.co.deliv.android.http.HttpClient;
import jp.co.deliv.android.http.NetworkErrorAlertDialog;
import jp.co.deliv.android.imasugu.ShopInfo;
import jp.co.deliv.android.imasugu.webapi.ShopInfoAPIPaser;
import jp.co.deliv.android.imasugu.webapi.gurunavi.GuruNaviAPIParser;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

/**
 * WEBAPIタイプ
 * @author DLV4002
 */
enum WEBAPI_TYPE {
	HOTPAPPER,		// ホットペッパーAPI
	GNAVI,			// ぐるナビAPI
}

/**
 * ホットペッパーAPIをコールしてXMLを取得、パース処理を行い、ShopInfoリストを作成するバックグラウンドタスク
 * @author DLV4002
 */
public class GetShopListTask extends AsyncTask<String, Void, List<ShopInfo>> {
	
	/*
	 * バックグラウンドタスクの呼び出し元Activity
	 */
	private Activity activity = null;

	/*
	 * バックグラウンドタスク実行中に表示するプログレスダイアログ
	 */
	private ProgressDialog progressDialog = null;
		
	/*
	 * ListViewに関連付けられているAdapterクラス
	 */
	private ShopListAdapter adapter = null;
	
	/*
	 * ユーザの位置情報
	 */
	private double userLatitude;
	private double userLongitude;

	/**
	 * ユーザの位置情報をセットしておく。ここでセットする位置情報は、お店との距離計算に使用される。
	 * @param latitude	緯度
	 * @param longitude	経度
	 */
	public void setUserLocation(double latitude, double longitude) {
		this.userLatitude = latitude;
		this.userLongitude = longitude;
	}
	
	/**
	 * 処理中に表示するプログレスダイアログの設定
	 * @param progressDialog
	 */
	public void setProgressDialog(ProgressDialog progressDialog) {
		this.progressDialog = progressDialog;
	}
	
	/**
	 * BackgroundTaskクラス（AsyncTaskクラスの派生クラス）
	 * @param activity
	 */
	public GetShopListTask(Activity activity, ShopListAdapter adapter) {
		this.activity = activity;
		this.adapter = adapter;
	}
	
	/**
	 * バックグラウンド処理の前処理
	 */
	@Override
	protected void onPreExecute() {
		
		/*
		 * 進捗状況を表すプログレスバーの設定
		 */
		if(progressDialog == null) {
			// ProgressDialog（プログレスバー）の設定
			progressDialog = new ProgressDialog(activity);
			
			// キャンセル設定
			progressDialog.setIndeterminate(false);
			
			// プログレスバーのスタイルをセット
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	
			// 表示するメッセージをセット
			Resources res = activity.getResources();
			progressDialog.setMessage(res.getText(R.string.msg_info_find_store));
	
			// プログレスバー表示
			progressDialog.show();
		}
	}
	
	/**
	 * 引数で渡されるURLにアクセスし、取得できたXMLパースしてTimeLineItemListを作成する
	 * このメソッドが非同期で実行される。
	 */
	@Override
	protected List<ShopInfo> doInBackground(String... params) {
		
		// ネットワークの接続状態を確認する
		if(!HttpClient.isNetworkConnected(activity)) {
			// 接続されていない場合は、以降の処理を行わない
			return null;
		}
		
		//すべてのショップ情報
		List<ShopInfo> allResult=null;
		
		//パラメータからURLリストをループしてデータを取得する
		for (String url : params){		
		
			// XMLを取得する
			ShopInfoAPIPaser xmlParser = null;		
			xmlParser = (ShopInfoAPIPaser)new GuruNaviAPIParser(url);
	
	    	// XMLを解析してShopInfoオブジェクトのリストにパースする
	    	List<ShopInfo> result = xmlParser.parse();
	    	
	    	//エラーが発生してデータが取得できなかったときは次urlを処理行く。
	    	if(result == null) {
				continue;
			}
	    	
	    	if (allResult==null){
	    		allResult = new ArrayList<ShopInfo>();	    		
	    	}
	    	//別データからすべてショップ情報にマージする
	    	allResult.addAll(result);
	    	
	    	//メモリをリリースする
	    	result.clear();
	    	result=null;
		}	
		
		// エラーが発生してデータが取得できなかったときはnullを返却する
		if (allResult == null){
			return null;
		}	
		
		/*
		 * 取得したお店情報の位置情報とユーザの位置情報から距離を計算する
		 */
		for(ShopInfo shop : allResult) {
			double shopLat = shop.getLatitude();
			double shopLng = shop.getLongitude();
			double distance = GeoCodingUtils.getDistance(this.userLatitude, this.userLongitude, shopLat, shopLng);
			shop.setDistance(distance);
		}
		
		/*
		 * ユーザの位置情報からの距離に応じて並び替えを行う
		 */
		Collections.sort(allResult, new Comparator<ShopInfo>() {
			/*
			 * ShopInfoクラスを距離の昇順に並び替える
			 */
			public int compare(ShopInfo shop1, ShopInfo shop2) {
				double d1 = shop1.getDistance();
				double d2 = shop2.getDistance();
				if(d1 == d2) {
					return 0;
				}
				if(d1 < d2) {
					return -1;
				}
				return 1;
			}
		});
		
    	return allResult;
	}

	/**
	 * バックグラウンド処理の後処理
	 * この処理はUIスレッドに戻っているはず。
	 */
	@Override
	protected void onPostExecute(List<ShopInfo> result) {
		
		// プログレスダイアログ終了
		try {
			this.progressDialog.dismiss();
		} catch(Exception e) {
			Log.d("GetShopListTask", "プログレスダイアログ終了で例外：" + e.getMessage());
			return;
		}
		
		/**
		 * バックグラウンドの処理結果をUIに表示
		 */
		if(result != null) {			
			/*
			 * XMLをパースした結果のShopInfoリストをアダプタにセットする
			 */
			HashSet<String> shopIdSet = new HashSet<String>();
            for(ShopInfo item : result) {
            	/*
            	 * 同じ店舗が複数出てきてしまうため、店舗データ取得後、同一IDの重複店舗は追加されない
            	 */
            	if (!shopIdSet.contains(item.getId())){
            		shopIdSet.add(item.getId());//同じ店舗の管理リスト
            		adapter.add(item);
            	}
            }
            shopIdSet.clear();
            shopIdSet=null;
		} else {
			NetworkErrorAlertDialog alert = new NetworkErrorAlertDialog(this.activity);
			alert.show();
		}
	}
	
}
