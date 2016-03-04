
package jp.co.deliv.android.imasugu.customize;
import jp.co.deliv.android.http.ImageDownloadTask;
import jp.co.deliv.android.imasugu.sugucafe.R;
import jp.co.deliv.android.imasugu.sugucafe.SuguCafeUtils;

import android.content.Context;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.maps.OverlayItem;
import com.readystatesoftware.mapviewballoons.BalloonOverlayView;

/**
 * set data for balloon
 * @author 120007HTT
 *
 * @param <Item> overlayItem in google library
 */
public class CustomBalloonOverlayView<Item extends OverlayItem> extends BalloonOverlayView<CustomOverlayItem> {

	/**
	 * set Views on Balloon
	 */
	private TextView catchCopyView;
	private TextView shopNameView;
	private TextView distanceView;
	private TextView walkTimeView;
	private TextView openHourView;
	private TextView hasCouponView;
	/**
	 * image of location
	 */
	private ProgressBar imageProgress;
	private ImageView imageView;
	
	public CustomBalloonOverlayView(Context context, int balloonBottomOffset) {
		super(context, balloonBottomOffset);
	}
	
	/**
	 * setup the view of Balloon
	 */
	@Override
	protected void setupView(Context context, final ViewGroup parent) {
		
		// inflate our custom layout into parent
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.balloon_data_view, parent);
		
		// setup our fields
		catchCopyView = (TextView)v.findViewById(R.id.catchCopy2);
		shopNameView = (TextView)v.findViewById(R.id.shopName);
		distanceView = (TextView)v.findViewById(R.id.shopDistance);
		walkTimeView = (TextView)v.findViewById(R.id.shopTime);
		openHourView = (TextView)v.findViewById(R.id.shopOpenHour);
		hasCouponView = (TextView)v.findViewById(R.id.shopCoupon);
		imageView = (ImageView)v.findViewById(R.id.shopImage);
		imageProgress = (ProgressBar)v.findViewById(R.id.shopImageProgress);

	}

	/**
	 * set data for balloon
	 */
	@Override
	protected void setBalloonData(CustomOverlayItem item, ViewGroup parent) {
		
		// map our custom item data to fields
		
		// キャッチコピー２をセット
		if(item.getCategory() == null || "".equals(item.getCategory())) {
			catchCopyView.setVisibility(View.GONE);
		} else {
			catchCopyView.setVisibility(View.VISIBLE);
			catchCopyView.setText(item.getCategory());
		}
		
		// お店の名前をセット
		shopNameView.setTypeface(Typeface.DEFAULT_BOLD);
		shopNameView.setText(item.getName());
		
		// 距離をセット
		distanceView.setText("約" + Math.round(item.getDistance()) + "m");
		
		// 徒歩での移動時間をセット（3km/h:50m/min で計算）
		walkTimeView.setText("約" + Math.round(item.getDistance() / 50D) + "分");
		
		// 営業時間をセット
		openHourView.setText(item.getOpenHour());
		
		// クーポンありの場合「クーポンあり」ビューを表示
		if("1".equalsIgnoreCase(item.getMobileCouponFlg())) {
			hasCouponView.setVisibility(View.VISIBLE);
		} 
		else 
		{
			hasCouponView.setVisibility(View.GONE);
		}
		// URL画像をダウンロードしてからImageViewにセットする
		imageView.setVisibility(View.GONE);		// 一旦イメージを消去しておく

		imageProgress.setVisibility(View.VISIBLE);	// イメージの代わりにプログレスバーを表示しておく
								
		// イメージをダウンロードする
		String imageUrl = item.getImage1URL();
		if(!SuguCafeUtils.isValidShopImageUrl(imageUrl)) 
		{
			imageUrl = item.getImage2URL();
		}
		imageView.setTag(imageUrl);
		ImageDownloadTask imageDLTask = new ImageDownloadTask(imageView, imageProgress, true);
		imageDLTask.execute(imageUrl);
		
	}
}
