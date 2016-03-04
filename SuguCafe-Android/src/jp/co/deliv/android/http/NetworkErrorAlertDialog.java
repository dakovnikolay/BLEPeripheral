package jp.co.deliv.android.http;

import jp.co.deliv.android.imasugu.sugucafe.R;
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
		title = activity.getString(R.string.msg_network_err_title);
		message = activity.getString(R.string.msg_network_err_message);
		positiveButtonText = activity.getString(R.string.msg_network_err_button_text);
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
