package jp.co.deliv.android.imasugu.sugucafe;

import java.util.List;

import jp.co.deliv.android.imasugu.ShopInfo;
import android.app.Application;

/**
 * Get/Set data through application
 * @author HHTung
 *
 */
public class ApplicationData extends Application{
	private double userLatitude; // Latitude of user
	private double userLongitude;// Longitude of user
	private String searchCondition;        // search condition
	private int typeSearch;    // check type of search
	private int updateLocation;          // for checking update location
	private List<ShopInfo> shopResult; //save data of shop
	
	/**
	 * get data of shop
	 * @return
	 */
	public List<ShopInfo> getShopResult() {
		return shopResult;
	}
	/**
	 * set data shop to save 
	 * @param shopResult
	 */
	public void setShopResult(List<ShopInfo> shopResult) {
		this.shopResult = shopResult;
	}
	
	/**
	 * get Latitude of user
	 * @return double
	 */
	public double getUserLatitude() {
		return userLatitude;
	}
	/**
	 * set Latitude of user
	 * @param userLatitude
	 */
	public void setUserLatitude(double userLatitude) {
		this.userLatitude = userLatitude;
	}
	
	/**
	 * Get Longitude of user
	 * @return
	 */
	public double getUserLongitude() {
		return userLongitude;
	}
	/**
	 * set longitude of user
	 * @param userLongitude
	 */
	public void setUserLongitude(double userLongitude) {
		this.userLongitude = userLongitude;
	}
	
	/**
	 * get search condition 
	 * @return String
	 */
	public String getSearchCondition() {
		return searchCondition;
	}
	
	/**
	 * set search condition
	 * @param searchCondition
	 */
	public void setSearchCondition(String searchCondition) {
		this.searchCondition = searchCondition;
	}
	
	/**
	 * get type of search
	 * @return
	 */
	public int getTypeSearch() {
		return typeSearch;
	}
	
	/**
	 * set type of search
	 * @param typeSearch
	 */
	public void setTypeSearch(int typeSearch) {
		this.typeSearch = typeSearch;
	}
	
	/**
	 * get the new update of location .
	 * @return int
	 */
	public int getUpdateLocation() {
		return updateLocation;
	}
	
	/**
	 * set the new update of location
	 * @param updateLocation
	 */
	public void setUpdateLocation(int updateLocation) {
		this.updateLocation = updateLocation;
	}
	
	/**
	 * Comment set new update .
	 */
	public void UpdateLocation()
	{
		updateLocation++;
	}
}
