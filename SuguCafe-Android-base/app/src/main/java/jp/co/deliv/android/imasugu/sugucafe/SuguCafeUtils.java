package jp.co.deliv.android.imasugu.sugucafe;

import android.app.Activity;
import android.content.Intent;

/**
 * すぐカフェアプリケーションユーティリティメソッド
 * @author DLV4002
 *
 */
public class SuguCafeUtils {

	
	/**
	 * すぐカフェアプリケーションのバージョン情報を表示するユーティリティメソッド
	 * @param activity
	 */
	public static void showAboutInformation(Activity activity) {
		Intent intent = new Intent(activity, SuguCafeAboutView.class);
		activity.startActivity(intent);
	}
	/**
	 * 画像Urlが正しいか確認します
	 * @param url 確認したいUrl
	 * @return true: 正しい false:正しく
	 */
	public static boolean isValidShopImageUrl(String url){
		if (url==null || url.equals("")) return false;
		
		if (url.contains("{0}") || url.contains("{1}")) return false;
		
		return true;
	}
}
