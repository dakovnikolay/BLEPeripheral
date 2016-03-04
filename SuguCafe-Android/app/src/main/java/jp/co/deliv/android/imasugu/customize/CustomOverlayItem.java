package jp.co.deliv.android.imasugu.customize;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

/**
 * class to get and set data for balloon
 * 
 * @author 120007HTT
 * 
 */

public class CustomOverlayItem extends OverlayItem {

	/**
	 * data use in Balloon
	 */
	private String image1Url, image2Url, openHour, mobileCouponFlg, name,
			category;
	private double distance;

	/**
	 * data detail but not show in Balloon
	 */
	private double latitude, longitude, shopLatitude, shoplongitude;
	private String id, address, access, tel, holiday, prShort, prLong, budget,
			shopUrlMobile, couponUrlMobile;

	public CustomOverlayItem(GeoPoint point, String Category, String Name,
			Double Distance, String OpenHour, String MobileCouponFlg,
			String Image1Url, String Image2Url) {
		super(point, Name, Category);
		this.name = Name;
		this.category = Category;
		this.distance = Distance;
		this.openHour = OpenHour;
		this.mobileCouponFlg = MobileCouponFlg;
		this.image2Url = Image2Url;
		this.image1Url = Image1Url;
	}

	/**
	 * get image url 2
	 * 
	 * @return String
	 */
	public String getImage2URL() {
		return image2Url;
	}

	/**
	 * get image url 1
	 * 
	 * @return String
	 */
	public String getImage1URL() {
		return image1Url;
	}

	/**
	 * get distance
	 * 
	 * @return Double
	 */
	public Double getDistance() {
		return distance;
	}

	/**
	 * get open hour of shop
	 * 
	 * @return String
	 */
	public String getOpenHour() {
		return openHour;
	}

	/**
	 * get mobile shop
	 * 
	 * @return String
	 */
	public String getMobileCouponFlg() {
		return mobileCouponFlg;
	}

	/**
	 * get name of shop
	 * 
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * get category
	 * 
	 * @return String
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * get Latitude of user
	 * 
	 * @return double
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * set Latitude of user
	 * @param latitude
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * get longitude of user
	 * @return
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * set longitude of user
	 * @param longitude
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * get latitude of shop which be chosen
	 * 
	 * @return Double
	 */
	public double getShopLatitude() {
		return shopLatitude;
	}

	/**
	 * set latitude of shop which be chosen
	 * @param shopLatitude
	 */
	public void setShopLatitude(double shopLatitude) {
		this.shopLatitude = shopLatitude;
	}

	/**
	 * get longitude of shop which be chosen
	 * @return
	 */
	public double getShoplongitude() {
		return shoplongitude;
	}

	/**
	 * set longitude of shop which be chosen
	 * @param shoplongitude
	 */
	public void setShoplongitude(double shoplongitude) {
		this.shoplongitude = shoplongitude;
	}

	/**
	 * get ID of shop
	 * 
	 * @return String
	 */
	public String getId() {
		return id;
	}

	/**
	 * set id of shop
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * get and set Address
	 * 
	 * @return String
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * set address of shop
	 * @param address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * get Access
	 * 
	 * @return String
	 */
	public String getAccess() {
		return access;
	}

	/**
	 * set access 
	 * @param access
	 */
	public void setAccess(String access) {
		this.access = access;
	}

	/**
	 * get Tel of shop
	 * 
	 * @return String
	 */
	public String getTel() {
		return tel;
	}

	/**
	 * set tel of shop
	 * @param tel
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}

	/**
	 * set shop's holiday
	 * 
	 * @return String
	 */
	public String getHoliday() {
		return holiday;
	}

	/**
	 * set shop's holiday
	 * @param holiday
	 */
	public void setHoliday(String holiday) {
		this.holiday = holiday;
	}

	/**
	 * get PrShort
	 * 
	 * @return String
	 */
	public String getPrShort() {
		return prShort;
	}
	
	/**
	 *set prShort 
	 * @param prShort
	 */
	public void setPrShort(String prShort) {
		this.prShort = prShort;
	}

	/**
	 * get PrLong
	 * @return
	 */
	public String getPrLong() {
		return prLong;
	}
	
	/**
	 *set PrLong
	 * 
	 * @return String
	 */
	public void setPrLong(String prLong) {
		this.prLong = prLong;
	}

	/**
	 * get budget
	 * @return
	 */
	public String getBudget() {
		return budget;
	}

	/**
	 * set budget
	 * 
	 * @return String
	 */
	public void setBudget(String budget) {
		this.budget = budget;
	}

	/**
	 * get shop url Mobile
	 * 
	 * @return String
	 */
	public String getShopUrlMobile() {
		return shopUrlMobile;
	}

	/**
	 * set shop url Mobile
	 * @param shopUrlMobile
	 */
	public void setShopUrlMobile(String shopUrlMobile) {
		this.shopUrlMobile = shopUrlMobile;
	}

	/**
	 * get Coupon url mobile
	 * 
	 * @return String
	 */
	public String getCouponUrlMobile() {
		return couponUrlMobile;
	}

	/**
	 * set coupon url mobile
	 * @param couponUrlMobile
	 */
	public void setCouponUrlMobile(String couponUrlMobile) {
		this.couponUrlMobile = couponUrlMobile;
	}

}
