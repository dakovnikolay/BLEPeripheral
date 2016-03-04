package jp.co.deliv.android.imasugu.sugusake;

import jp.co.deliv.android.http.ImageDownloadTask;
import jp.co.deliv.android.imasugu.ShopInfo;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * お店の詳細情報を表示するアクティビティ
 * 「ここに行く」ボタンで、地図画面に遷移する。
 * @author Atsushi
 */
public class ShopDetailView extends Activity {

	/*
	 * ユーザ位置情報
	 */
	private double userLatitude;
	private double userLongitude;
	
	/*
	 * お店の詳細情報
	 */
	private ShopInfo shopInfo = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shopdetailview);
		
		/*
		 * Intentで値を取得
		 */
		Intent intent = getIntent();
		
		// ユーザの位置情報を取得
		userLatitude = intent.getDoubleExtra("USER_LATITUDE", 0.0);
		userLongitude = intent.getDoubleExtra("USER_LONGITUDE", 0.0);

		// 選択したお店の情報を取得
		shopInfo = new ShopInfo();
		shopInfo.setId(intent.getStringExtra(SuguSakeConst.SHOP_ID));
		shopInfo.setName(intent.getStringExtra(SuguSakeConst.SHOP_NAME));
		shopInfo.setAddress(intent.getStringExtra(SuguSakeConst.SHOP_ADDRESS));
		shopInfo.setAccess(intent.getStringExtra(SuguSakeConst.SHOP_ACCESS));
		shopInfo.setTel(intent.getStringExtra(SuguSakeConst.SHOP_TEL));
		shopInfo.setOpenHour(intent.getStringExtra(SuguSakeConst.SHOP_OPENHOUR));
		shopInfo.setHoliday(intent.getStringExtra(SuguSakeConst.SHOP_HOLIDAY));
		shopInfo.setCategory(intent.getStringExtra(SuguSakeConst.SHOP_CATEGORY));
		shopInfo.setPrShort(intent.getStringExtra(SuguSakeConst.SHOP_PR_SHORT));
		shopInfo.setPrLong(intent.getStringExtra(SuguSakeConst.SHOP_PR_LONG));
		shopInfo.setBudget(intent.getStringExtra(SuguSakeConst.SHOP_BUDGET));
		shopInfo.setShopImage1Url(intent.getStringExtra(SuguSakeConst.SHOP_IMAGE_1));
		shopInfo.setShopImage2Url(intent.getStringExtra(SuguSakeConst.SHOP_IMAGE_2));
		shopInfo.setShopUrlMobile(intent.getStringExtra(SuguSakeConst.SHOP_MOBILE_URL));
		shopInfo.setMobileCouponFlg(intent.getStringExtra(SuguSakeConst.SHOP_MOBILE_COUPON_FLG));
		shopInfo.setCouponUrlMobile(intent.getStringExtra(SuguSakeConst.SHOP_COUPON_URL));
		shopInfo.setLatitude(intent.getDoubleExtra(SuguSakeConst.SHOP_LATITUDE, 0D));
		shopInfo.setLongitude(intent.getDoubleExtra(SuguSakeConst.SHOP_LONGITUDE, 0D));
		shopInfo.setDistance(intent.getDoubleExtra(SuguSakeConst.SHOP_DISTANCE, 0D));
		shopInfo.setEquipment(intent.getStringExtra(SuguSakeConst.SHOP_EQUIPMENT));		
		/*
		 * お店情報を表示
		 */
		((TextView)findViewById(R.id.shopName)).setText(shopInfo.getName());
		((TextView)findViewById(R.id.shopAddress)).setText(shopInfo.getAddress());
		((TextView)findViewById(R.id.shopAccess)).setText(shopInfo.getAccess());
		((TextView)findViewById(R.id.shopOpenHour)).setText(shopInfo.getOpenHour());
		// BUDGET (平均予算)
		TextView shopBudget = (TextView)findViewById(R.id.shopBudget);
		String strBudget = shopInfo.getBudget();
		if (strBudget==null || "".equals(strBudget)){
			shopBudget.setVisibility(View.GONE);
		}else {
			shopBudget.setVisibility(View.VISIBLE);
			shopBudget.setText(SuguSakeUtils.formatCurrency(strBudget));
		}
		((TextView)findViewById(R.id.shopCategory)).setText(shopInfo.getCategory());
		((TextView)findViewById(R.id.shopCatchCopy1)).setText(shopInfo.getPrLong());
		((TextView)findViewById(R.id.shopAverage)).setText(shopInfo.getBudget());
		((TextView)findViewById(R.id.shopTel)).setText(shopInfo.getTel());
		((TextView)findViewById(R.id.shopHoliday)).setText(shopInfo.getHoliday());
		
		// EQUIPMENT (設備・サービス)
		TextView shopEquipment = (TextView)findViewById(R.id.shopEquipment);
		String strEquipment = shopInfo.getEquipment();
		if (strEquipment==null || "".equals(strEquipment)){
			((ImageView)findViewById(R.id.shopEquipmentAboveBar)).setVisibility(View.GONE);			
			shopEquipment.setVisibility(View.GONE);			
		}else {
			((ImageView)findViewById(R.id.shopEquipmentAboveBar)).setVisibility(View.VISIBLE);			
			shopEquipment.setVisibility(View.VISIBLE);
			shopEquipment.setText(strEquipment);			
		}
		
		// PR_LONG（設定がない場合は非表示）
		TextView shopPrLong = (TextView)findViewById(R.id.shopCatchCopy2);
		String strPrLong = shopInfo.getPrLong();
		if(strPrLong == null || "".equals(strPrLong)) {
			shopPrLong.setVisibility(View.GONE);
		} else {
			shopPrLong.setVisibility(View.VISIBLE);
			shopPrLong.setText(strPrLong);
		}

		// PR_SHORT（設定がない場合は非表示）
		TextView shopPrShort = (TextView)findViewById(R.id.shopCatchCopy2);
		String strText = shopInfo.getPrShort();
		if(strText == null || "".equals(strText)) {
			shopPrShort.setVisibility(View.GONE);
		} else {
			shopPrShort.setVisibility(View.VISIBLE);
			shopPrShort.setText(strText);
		}
				
		// 大きい画像を表示する
		ProgressBar imageProgress = (ProgressBar)findViewById(R.id.shopImageProgress);
		ImageView imageView = (ImageView)findViewById(R.id.shopImage);
		ImageDownloadTask imageDLTask = new ImageDownloadTask(imageView, imageProgress, false);
		String imageUrl = shopInfo.getShopImage1Url();
		if("".equals(imageUrl) || imageUrl == null) {
			imageUrl = shopInfo.getShopImage2Url();
		}
		imageDLTask.execute(imageUrl);
		
		/*
		 * 「ここに行く」ボタンを押すと地図ビューを表示する
		 */
		Button goNowButton = (Button)findViewById(R.id.btnGoNow);
		goNowButton.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				/*
				Intent intent = new Intent(ShopDetailView.this, CafeMapView.class);
				intent.putExtra("USER_LATITUDE", userLatitude);
				intent.putExtra("USER_LONGITUDE", userLongitude);
				intent.putExtra("DESTINATION_LATITUDE", shopInfo.getLatitude());
				intent.putExtra("DESTINATION_LONGITUDE", shopInfo.getLongitude());
				startActivity(intent);
				*/

				//これも悪くないが、UIを制御できないのがいまいちか。。。
				// Google Map で経路検索
				String sAddr = userLatitude + "," + userLongitude;
				String dAddr = shopInfo.getLatitude() + "," + shopInfo.getLongitude();
				String url = "http://maps.google.com/maps?saddr=" + sAddr + "&daddr=" + dAddr + "&dirflg=w";
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
				intent.setData(Uri.parse(url));
				startActivity(intent);
				 
			}
		});
		
		/**
		 * 電話を掛ける
		 */
		Button btnTelNow = (Button)findViewById(R.id.btnTelephone);
		if(shopInfo.getTel() != null && !"".equals(shopInfo.getTel())) {
			btnTelNow.setEnabled(true);
			btnTelNow.setOnClickListener(new View.OnClickListener() {				
				public void onClick(View v) {
					String phoneNo = shopInfo.getTel();
					phoneNo = phoneNo.replace("-", "");
					Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNo));  
					startActivity(i);					
				}
			});
		} else {
			btnTelNow.setEnabled(false);
		}
		
		/*
		 * 「クーポン表示」ボタンを押すとクーポンを表示（ホットペッパーやぐるナビ限定だな。。。食べログのときはどうしようか。。。）
		 */
		Button btnShowCoupon = (Button)findViewById(R.id.btnShowCoupon);
		if("1".equalsIgnoreCase(shopInfo.getMobileCouponFlg())) {
			btnShowCoupon.setVisibility(View.VISIBLE);
			btnShowCoupon.setEnabled(true);
			btnShowCoupon.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(shopInfo.getShopUrlMobile()));  
					startActivity(i);
				}
			});
		} else {
			btnShowCoupon.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// 文字列取得のためリソースを取得
		Resources res = getResources();
		
		// バージョン情報メニューを追加
    	menu.add(Menu.NONE, SuguSakeConst.MENU_ID_ABOUT, Menu.NONE, res.getText(R.string.menu_about))
    	.setIcon(android.R.drawable.ic_menu_info_details);
    	
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		boolean ret = true;
		switch(item.getItemId()) {
		
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

}
