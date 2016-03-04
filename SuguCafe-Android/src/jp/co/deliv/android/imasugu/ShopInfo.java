package jp.co.deliv.android.imasugu;

/**
 * このクラスは今すぐシリーズで表示する店舗の属性を定義しているBeanクラス。
 * ShopListViewやShopDetailViewで表示するお店の情報定義。
 * ぐるナビAPIで取得できる属性に最適化されている。
 * 
 * ＜すぐカフェ＞
 * ぐるNAVIのAPIを使用して取得した情報を格納
 * 
 * @author Atsushi
 */
public class ShopInfo {

	private String id;					// 店舗識別ID（ぐるナビ）
	private String name;				// 店舗名（ぐるナビ）
	private String address;				// 店舗住所（ぐるナビ）
	private String access;				// 最寄り駅からのアクセス（ぐるナビ）
	private String openHour;			// お店の営業時間（ぐるナビ）
	
	private String category;			// カテゴリ
	private String prShort;				// 短いPR（ぐるナビ）
	private String prLong;				// 長いPR（ぐるナビ）
		
	private String budget;				// 平均予算（ぐるナビ）
	
	private String tel;					// お店への電話番号（ぐるナビ）
	private String fax;					// お店へのFAX番号（ぐるナビ）
		
	private String shopImage1Url;		// お店の画像１（ぐるナビ）
	private String shopImage2Url;		// お店の画像２（ぐるナビ）
	
	private String holiday;				// 休業日（ぐるナビ）
	private String mobileCouponFlg;		// モバイルクーポンフラグ（ぐるナビ）
	
	private String shopUrlPC;			// 店舗URL（PC向け）
	private String shopUrlMobile;		// 店舗URL（モバイル向け）
	
	private String couponUrlMobile;		// クーポンURL（モバイル向け）
	
	private double latitude;		// 店舗位置：緯度
	private double longitude;		// 店舗位置：軽度
	
	private double distance;		// ユーザ現在位置からの距離（単位：メートル）
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getAccess() {
		return access;
	}
	public void setAccess(String access) {
		this.access = access;
	}
	public String getOpenHour() {
		return openHour;
	}
	public void setOpenHour(String openHour) {
		this.openHour = openHour;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public String getShopUrlPC() {
		return shopUrlPC;
	}
	public void setShopUrlPC(String shopUrlPC) {
		this.shopUrlPC = shopUrlPC;
	}
	public String getCouponUrlMobile() {
		return couponUrlMobile;
	}
	public void setCouponUrlMobile(String couponUrlMobile) {
		this.couponUrlMobile = couponUrlMobile;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getShopImage1Url() {
		return shopImage1Url;
	}
	public void setShopImage1Url(String shopImage1Url) {
		this.shopImage1Url = shopImage1Url;
	}
	public String getShopImage2Url() {
		return shopImage2Url;
	}
	public void setShopImage2Url(String shopImage2Url) {
		this.shopImage2Url = shopImage2Url;
	}
	public String getHoliday() {
		return holiday;
	}
	public void setHoliday(String holiday) {
		this.holiday = holiday;
	}
	public String getMobileCouponFlg() {
		return mobileCouponFlg;
	}
	public void setMobileCouponFlg(String mobileCouponFlg) {
		this.mobileCouponFlg = mobileCouponFlg;
	}
	public String getShopUrlMobile() {
		return shopUrlMobile;
	}
	public void setShopUrlMobile(String shopUrlMobile) {
		this.shopUrlMobile = shopUrlMobile;
	}
	public String getPrShort() {
		return prShort;
	}
	public void setPrShort(String prShort) {
		this.prShort = prShort;
	}
	public String getPrLong() {
		return prLong;
	}
	public void setPrLong(String prLong) {
		this.prLong = prLong;
	}
	public String getBudget() {
		return budget;
	}
	public void setBudget(String budget) {
		this.budget = budget;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
}
