package jp.co.deliv.android.http;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * ネットワーク接続ができないときにユーザにネットワーク接続状態を確認するよう促す、
 * アラートダイアログを表示するクラス。
 * @author DLV4002
 */
public class NetworkErrorAlertDialog {

	private Activity activity;	
	private String title;					// ダイアログタイトル
	private String message;					// ダイアログメッセージ
	private String positiveButtonText;		// ポジティブ（OK）ボタン文字列
	
	public NetworkErrorAlertDialog(Activity activity) {
		this.activity = activity;
		title = "ネットワークエラー";
		message = "ネットワークに接続できていないか電波が途切れたため、通信ができませんでした。"
				+ "電波状態を確認し、3GネットワークまたはWi-Fiに接続した状態でご利用ください。";
		positiveButtonText = "OK";
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

	/**
	 * ダイアログを表示
	 * @param activity
	 */
	public void show() {
		new AlertDialog.Builder(activity)
		.setTitle(title)
		.setMessage(message)
		.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, final int which) { }
		})
		.create()
		.show();		
	}
}
