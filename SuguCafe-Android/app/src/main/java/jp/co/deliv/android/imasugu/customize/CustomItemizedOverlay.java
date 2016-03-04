
package jp.co.deliv.android.imasugu.customize;

import java.util.ArrayList;

import jp.co.deliv.android.imasugu.sugucafe.ShopDetailView;
import jp.co.deliv.android.imasugu.sugucafe.SuguCafeConst;



import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;
import com.readystatesoftware.mapviewballoons.BalloonOverlayView;


/**
 * Control balloon
 * @author 120007HTT
 *
 * @param <Item> Overlayitem in google library
 */

public class CustomItemizedOverlay<Item extends OverlayItem> extends BalloonItemizedOverlay<CustomOverlayItem> {

	/**
	 * Array overlays items 
	 */
	private ArrayList<CustomOverlayItem> overlays = new ArrayList<CustomOverlayItem>();
	/**
	 * Map activity use 
	 */
	private MapActivity activity;
	
	/**
	 * Context of mapview
	 */
	Context c;
	public CustomItemizedOverlay(Drawable defaultMarker, MapView mapView,MapActivity activity) {
		super(boundCenter(defaultMarker), mapView);
		c = mapView.getContext();
		this.activity= activity;
	}

	/**
	 * Comment : add overlay
	 * @param overlay
	 */
	public void addOverlay(CustomOverlayItem overlay) {
	    overlays.add(overlay);
	    populate();
	}

	@Override
	protected CustomOverlayItem createItem(int i) {
		return overlays.get(i);
	}

	@Override
	public int size() {
		return overlays.size();
	}

	// set data transfer to detail view 
	@Override
	protected boolean onBalloonTap(int index, CustomOverlayItem item) {
		//hideAllBalloons();
		Intent intent = new Intent(this.activity,ShopDetailView.class);
		intent.putExtra("USER_LATITUDE", item.getLatitude());
		intent.putExtra("USER_LONGITUDE", item.getLongitude());
		intent.putExtra(SuguCafeConst.SHOP_ID, item.getId());
		intent.putExtra(SuguCafeConst.SHOP_NAME, item.getName());
		intent.putExtra(SuguCafeConst.SHOP_ADDRESS, item.getAddress());
		intent.putExtra(SuguCafeConst.SHOP_ACCESS, item.getAccess());
		intent.putExtra(SuguCafeConst.SHOP_TEL, item.getTel());
		intent.putExtra(SuguCafeConst.SHOP_OPENHOUR, item.getOpenHour());
		intent.putExtra(SuguCafeConst.SHOP_HOLIDAY, item.getHoliday());
		intent.putExtra(SuguCafeConst.SHOP_CATEGORY, item.getCategory());
		intent.putExtra(SuguCafeConst.SHOP_PR_SHORT, item.getPrShort());
		intent.putExtra(SuguCafeConst.SHOP_PR_LONG, item.getPrLong());
		intent.putExtra(SuguCafeConst.SHOP_BUDGET, item.getBudget());
		intent.putExtra(SuguCafeConst.SHOP_IMAGE_1, item.getImage1URL());
		intent.putExtra(SuguCafeConst.SHOP_IMAGE_2, item.getImage2URL());
		intent.putExtra(SuguCafeConst.SHOP_MOBILE_URL, item.getShopUrlMobile());
		intent.putExtra(SuguCafeConst.SHOP_MOBILE_COUPON_FLG, item.getMobileCouponFlg());
		intent.putExtra(SuguCafeConst.SHOP_COUPON_URL, item.getCouponUrlMobile());
		intent.putExtra(SuguCafeConst.SHOP_LATITUDE, item.getShopLatitude());
		intent.putExtra(SuguCafeConst.SHOP_LONGITUDE, item.getShoplongitude());
		intent.putExtra(SuguCafeConst.SHOP_DISTANCE, item.getDistance());
		
		// ShopDetailViewを開始する
		this.activity.startActivity(intent);
		
		return true;
	}
	
	// create data balloon
	@Override
	protected BalloonOverlayView<CustomOverlayItem> createBalloonOverlayView() {
		// use our custom balloon view with our custom overlay item type:
		return new CustomBalloonOverlayView<CustomOverlayItem>(getMapView().getContext(), getBalloonBottomOffset());
	}

}
