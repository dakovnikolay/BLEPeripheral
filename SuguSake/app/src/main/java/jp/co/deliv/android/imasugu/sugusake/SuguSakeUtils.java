package jp.co.deliv.android.imasugu.sugusake;

import java.text.NumberFormat;

import android.app.Activity;
import android.content.Intent;

/**
 * すぐカフェアプリケーションユーティリティメソッド
 * @author DLV4002
 *
 */
public class SuguSakeUtils {

	
	/**
	 * すぐカフェアプリケーションのバージョン情報を表示するユーティリティメソッド
	 * @param activity
	 */
	public static void showAboutInformation(Activity activity) {
		Intent intent = new Intent(activity, SuguSakeAboutView.class);
		activity.startActivity(intent);
	}
	
	/**
	 * 金額文字から日本金にコンバートするユーティリティメソッド
	 * @param 金額文字
	 * @return　x,xxx円の文字
	 */
	public static String formatCurrency(String number){
		NumberFormat nf = NumberFormat.getNumberInstance();		
		return nf.format(Long.parseLong(number)) + "円";
	}
}
