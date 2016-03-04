package jp.co.deliv.android.imasugu.sugucafe;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 緯度経度から住所変換を行うバックグラウンドタスク。
 * コンストラクタにて住所を表示するTextViewをセットしておくと、住所変換後にTextViewに対して
 * 変換した住所をセットするようになっている。
 * @author DLV4002
 */
public class GetFormattedAddressTask extends AsyncTask<Double, Void, String> {

	private Activity activity = null;
	private TextView fmtAddressView = null;
	
	public GetFormattedAddressTask(Activity activity, TextView formatedAddressView) {
		this.activity = activity;
		this.fmtAddressView = formatedAddressView;
	}
	
	@Override
	protected String doInBackground(Double... params) {
		Double latitude = params[0];
		Double longitude = params[1];
				
		Geocoder coder = new Geocoder(activity.getApplicationContext(), Locale.JAPAN);
		List<Address> addresses = null;
		try {
			addresses = coder.getFromLocation(latitude, longitude, 10);
		} catch (IOException e) {
			Toast.makeText(activity, "位置情報の取得に失敗しました。ネットワークの接続状況を確認してください。", Toast.LENGTH_LONG);
			return new String("-----");
			
		}
		String addressLine;
		if(addresses.size() > 0) {
			Address address = addresses.get(0);
			addressLine = address.getAddressLine(1);
			Log.d("GetFormattedAddressTask", addressLine);
		} else {
			addressLine = "-----";
		}
		
		// 緯度経度を住所に変換し、取得した文字列を返却する
		return addressLine;
	}

	@Override
	protected void onPostExecute(String result) {
		
		String formattedAddress = result;
		
		/*
		 * TextViewにセットする
		 */
		if(fmtAddressView != null) {
			fmtAddressView.setText(formattedAddress + " 付近");
		}
	}
}
