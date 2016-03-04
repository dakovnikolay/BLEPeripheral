package jp.co.deliv.android.imasugu.sugucafe;

import android.view.Menu;

/**
 * すぐカフェアプリ定数
 * @author Atsushi
 *
 */
public class SuguCafeConst {

	
	/**
	 * デフォルト緯度（広尾駅周辺）
	 */
	
	public static final double DEF_LATITUDE = 35.652937;
	/**
	 * デフォルト経度（広尾駅周辺）
	 */
	public static final double DEF_LONGITUDE = 139.722319;

	/*
	 * ぐるナビWEBサービス、レストラン検索APIのアクセスURL
	 */
	public static final String GNAVI_REST_API_URL = "http://api.gnavi.co.jp/ver1/RestSearchAPI/";
	public static final String GNAVI_API_KEY = "keyid=9fcb6c23b03b4180f7dd866852041260";
	/*
	 * ぐるなびAPIのオプション指定
	 * 半径1000m、カテゴリ：カフェ、1ページあたり50件まで、位置情報の測地系：世界測地系
	 */
	public static final String GNAVI_API_OPTION = "range=3&category_l=CTG500&hit_per_page=50&coordinates_mode=2";
	
	/*
	 * 店舗情報の各要素定数（Intentでの値受け渡しに使用する）
	 */
	public static final String SHOP_ID = "ShopId";
	public static final String SHOP_NAME = "ShopName";
	public static final String SHOP_ADDRESS = "ShopAddress";
	public static final String SHOP_ACCESS = "ShopAccess";
	public static final String SHOP_TEL = "ShopTel";
	public static final String SHOP_OPENHOUR = "ShopOpenHour";
	public static final String SHOP_HOLIDAY = "ShopHoliday";
	public static final String SHOP_CATEGORY = "ShopCategory";
	public static final String SHOP_PR_SHORT = "ShopPrShort";
	public static final String SHOP_PR_LONG = "ShopPrLong";
	public static final String SHOP_BUDGET = "ShopBudget";
	public static final String SHOP_IMAGE_1 = "ShopImage1";
	public static final String SHOP_IMAGE_2 = "ShopImage2";
	public static final String SHOP_PC_URL = "ShopPcUrl";
	public static final String SHOP_MOBILE_URL = "ShopMobileUrl";
	public static final String SHOP_MOBILE_COUPON_FLG = "ShopMobileCouponFlag";
	public static final String SHOP_COUPON_URL = "ShopCouponUrl";
	public static final String SHOP_LATITUDE = "ShopLatitude";
	public static final String SHOP_LONGITUDE = "ShopLangitude";
	public static final String SHOP_DISTANCE = "ShopDistance";

	/**
	 * メニューID
	 */
	public static final int MENU_ID_SHOPLIST_UPDATE = (Menu.FIRST + 1);	// ショップリスト更新
	public static final int MENU_ID_LOCATION_UPDATE = (Menu.FIRST + 2);	// 位置情報更新
	public static final int MENU_ID_Move_To_List = (Menu.FIRST + 3);	// 位置情報更新
	public static final int MENU_ID_Move_To_Map = (Menu.FIRST + 4);	// 位置情報更新
	public static final int MENU_ID_ABOUT = (Menu.FIRST + 99);				// バージョン情報
	
	
	/**
	 * search around
	 */
	public static final int SEARCH_CAFE_ARROUND =1;
	public static final int SEARCH_CAFE_WITH_ADDRESS = 0;
	
	public static final int TAB_PERFORM_TEXT_AND_IMAGE = 0;
	public static final int TAB_PERFORM_IMAGE_ONLY = 1;
	public static final int TAB_PERFORM_TEXT_ONLY =2;
	
	public static final int GPS_DETECT_TIME_OUT = 6000;
	public static final int LIST_VIEW_IMAGE_HEIGHT = 128;
	public static final int LIST_VIEW_IMAGE_WIDTH = 128;
	

	
	
}
