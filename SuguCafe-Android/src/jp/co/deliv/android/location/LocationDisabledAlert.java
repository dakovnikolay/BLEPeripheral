package jp.co.deliv.android.location;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;

/**
 * GPSやNetworkを通じて位置情報を取得できないときにアラートダイアログを表示するクラス。
 * 位置情報の設定画面を表示することができるようになっている。
 * @author DLV4002
 *
 */
public class LocationDisabledAlert {

	private Activity activity;	
	private String title;					// ダイアログタイトル
	private String message;					// ダイアログメッセージ
	private String positiveButtonText;		// ポジティブ（OK）ボタン文字列
	private String negativeButtonText;		// ネガティブ（キャンセル）ボタン文字列
	
	public LocationDisabledAlert(Activity activity) {
		this.activity = activity;
		title = "位置情報が無効。";
		message = "現在、お使いの端末において位置情報が無効になっており、アプリケーションで位置情報を検出できません。"
				+ "次のように設定を行い位置情報が検出できるように設定してください。:\n\n"
				+ "● 位置情報の設定で「GPS機能を使用」と「ワイヤレスネットワークを使う」をオンにする\n";
		positiveButtonText = "設定";
		negativeButtonText = "スキップ";
	}
	
	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}

	public String getPositiveButtonText() {
		return positiveButtonText;
	}

	public void setPositiveButtonText(String positiveButtonText) {
		this.positiveButtonText = positiveButtonText;
	}

	public String getNegativeButtonText() {
		return negativeButtonText;
	}

	public void setNegativeButtonText(String negativeButtonText) {
		this.negativeButtonText = negativeButtonText;
	}

	/**
	 * ダイアログを表示
	 * @param activity
	 */
	public void show() {
		new AlertDialog.Builder(activity)
		.setTitle(title)
		.setMessage(message)
		.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, final int which) {
				try {
					activity.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
				} catch (ActivityNotFoundException e) {}	// 無視する
			}
		})
		.setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, final int which) {}
		})
		.create()
		.show();		
	}
}
