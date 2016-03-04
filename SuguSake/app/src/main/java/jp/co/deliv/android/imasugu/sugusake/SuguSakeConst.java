package jp.co.deliv.android.imasugu.sugusake;

import android.view.Menu;

/**
 * すぐカフェアプリ定数
 * @author Atsushi
 *
 */
public class SuguSakeConst {

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
	 * 5回検索するため、検索件数が多くなる可能性があるため、検索範囲（キーワード range）は、500m以内（range=2）で検索する。
	 */
	//CTG610 	和風居酒屋
	public static final String GNAVI_API_OPTION_1 = "range=2&category_l=CTG610&hit_per_page=50&coordinates_mode=2";
	//CTG620 	洋風居酒屋
	public static final String GNAVI_API_OPTION_2 = "range=2&category_l=CTG620&hit_per_page=50&coordinates_mode=2";
	//CTG630 	アジア・無国籍居酒屋
	public static final String GNAVI_API_OPTION_3 = "range=2&category_l=CTG630&hit_per_page=50&coordinates_mode=2";
	//CTG640 	ダイニングバー・ビアレストラン
	public static final String GNAVI_API_OPTION_4 = "range=2&category_l=CTG640&hit_per_page=50&coordinates_mode=2";
	//CTG650 	バー・パブ
	public static final String GNAVI_API_OPTION_5 = "range=2&category_l=CTG650&hit_per_page=50&coordinates_mode=2";
		
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
	public static final String SHOP_EQUIPMENT = "ShopEquipment";

	/**
	 * メニューID
	 */
	public static final int MENU_ID_SHOPLIST_UPDATE = (Menu.FIRST + 1);	// ショップリスト更新
	public static final int MENU_ID_LOCATION_UPDATE = (Menu.FIRST + 2);	// 位置情報更新
	public static final int MENU_ID_ABOUT = (Menu.FIRST + 99);				// バージョン情報
	
}
