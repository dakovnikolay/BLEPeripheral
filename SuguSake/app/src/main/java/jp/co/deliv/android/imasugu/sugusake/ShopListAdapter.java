package jp.co.deliv.android.imasugu.sugusake;

import java.util.List;

import jp.co.deliv.android.http.ImageDownloadTask;
import jp.co.deliv.android.imasugu.ShopInfo;
import jp.co.deliv.android.imasugu.sugusake.R;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * イマすぐシリーズ：すぐカフェ
 * ショップリストアクティビティのListViewに関連づけられるアダプタクラス
 * ShopInfoオブジェクトの文字情報をビューにセットし、画像データはバックグラウンドタスクとして実行し、
 * 画像データ取得後、イメージビューにセットする。
 * @author Atsushi
 */
public class ShopListAdapter extends ArrayAdapter<ShopInfo> {

	private LayoutInflater inflater = null;
	private List<ShopInfo> items = null;

	/**
	 * CafeShopListView に関連づける Adapterクラスのコンストラクタ
	 * @param context
	 * @param textViewResourceId
	 * @param objects
	 */
	public ShopListAdapter(Context context, int textViewResourceId, List<ShopInfo> objects) {
		super(context, textViewResourceId, objects);
		
		this.items = objects;
		this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * ListViewの行データ表示時に呼ばれる。
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view = convertView;
		if(view == null) {
			view = inflater.inflate(R.layout.shoplistview_row, null);
		}

		// 表示すべきデータの取得
		ShopInfo item = (ShopInfo)items.get(position);
		if(item != null) {
			
			// キャッチコピー２をセット
			TextView catchCopyView = (TextView)view.findViewById(R.id.catchCopy2);
			if(item.getCategory() == null || "".equals(item.getCategory())) {
				catchCopyView.setVisibility(View.GONE);
			} else {
				catchCopyView.setVisibility(View.VISIBLE);
				catchCopyView.setText(item.getCategory());
			}
			
			// お店の名前をセット
			TextView shopNameView = (TextView)view.findViewById(R.id.shopName);
			shopNameView.setTypeface(Typeface.DEFAULT_BOLD);
			shopNameView.setText(item.getName());
			
			// 距離をセット
			TextView distanceView = (TextView)view.findViewById(R.id.shopDistance);
			distanceView.setText("約" + Math.round(item.getDistance()) + "m");
			
			// 徒歩での移動時間をセット（3km/h:50m/min で計算）
			TextView walkTimeView = (TextView)view.findViewById(R.id.shopTime);
			walkTimeView.setText("約" + Math.round(item.getDistance() / 50D) + "分");
			
			// 営業時間をセット
			TextView openHourView = (TextView)view.findViewById(R.id.shopOpenHour);
			openHourView.setText(item.getOpenHour());
			
			// 平均予算をセット
			TextView shopBudgetView = (TextView)view.findViewById(R.id.shopAverageBudget);
			shopBudgetView.setText(SuguSakeUtils.formatCurrency(item.getBudget()));
			
			// クーポンありの場合「クーポンあり」ビューを表示
			TextView hasCouponView = (TextView)view.findViewById(R.id.shopCoupon);
			if("1".equalsIgnoreCase(item.getMobileCouponFlg())) {
				hasCouponView.setVisibility(View.VISIBLE);
			} else {
				hasCouponView.setVisibility(View.GONE);
			}

			// URL画像をダウンロードしてからImageViewにセットする
			ImageView imageView = (ImageView)view.findViewById(R.id.shopImage);
			imageView.setVisibility(View.GONE);		// 一旦イメージを消去しておく
			ProgressBar imageProgress = (ProgressBar)view.findViewById(R.id.shopImageProgress);
			imageProgress.setVisibility(View.VISIBLE);	// イメージの代わりにプログレスバーを表示しておく
						
			// イメージをダウンロードする
			ImageDownloadTask imageDLTask = new ImageDownloadTask(imageView, imageProgress, true);
			String imageUrl = item.getShopImage1Url();
			if("".equals(imageUrl) || imageUrl == null) {
				imageUrl = item.getShopImage2Url();
			}
			imageDLTask.execute(imageUrl);
		}
		return view;
	}

}
