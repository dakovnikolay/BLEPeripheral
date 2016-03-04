package jp.co.deliv.android.http;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * HTTPアクセスクライアントクラス。
 * @author DLV4002
 */
public class HttpClient {
	
	/**
	 * デフォルトタイムアウト：30秒（ミリ秒）
	 */
	public static final int DEFAULT_TIME_OUT = 30 * 1000;
	
	/**
	 * ネットワークの接続状態をチェックするユーティリティメソッド
	 * @param ctx コンテキスト
	 * @return ネットワーク（3GまたはWIFI）に接続している場合はtrue
	 */
	public static boolean isNetworkConnected(Context ctx) {
		
		ConnectivityManager conMan = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(conMan != null) {
			// ネットワーク回線の状態を確認する
			NetworkInfo[] networks = conMan.getAllNetworkInfo();
			if(networks != null) {
				for(NetworkInfo nInfo : networks) {
					if(nInfo.isConnected()) {
						// ネットワークに接続されているものが存在した
						return true;
					}
				}
			}

			/********
			＜クラッシュレポートにより以下のコードは削除＞
			// 3G回線の状態
			State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
			// WIFIの状態
			State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
			
			// いずれかが接続されているか？
			if((mobile != State.CONNECTED) && (wifi != State.CONNECTED)) {
				return false;
			}
			// どちらか、または両方接続されている
			return true;
			********/
		}
		return false;
	}
	
	/**
	 * 
	 * 引数で指定されるURLのデータをバイト配列で取得する。
	 * WEBアクセスによるXMLデータの取得や画像データの取得などによる利用を想定している。
	 * 
	 * @param strUrl WEBアクセスによるデータ取得のURL
	 * @return 指定したURLから取得したデータのバイト配列
	 * 
	 */
	public static byte[] getByteArrayFromURL(String strUrl) {
		
		byte[] byteArray = new byte[1024];  
		byte[] result = null;  
		
		HttpURLConnection con = null;
		InputStream in = null;
		ByteArrayOutputStream out = null;
		int size = 0;  
        
		try {  
            URL url = new URL(strUrl);  
            con = (HttpURLConnection) url.openConnection();  
            con.setRequestMethod("GET");  
            con.setConnectTimeout(HttpClient.DEFAULT_TIME_OUT);
            con.connect();  
            
            in = con.getInputStream();  
  
            out = new ByteArrayOutputStream();  
            while ((size = in.read(byteArray)) != -1) {
            	out.write(byteArray, 0, size);  
            }
            
            result = out.toByteArray();
            
        } catch (Exception e) {
        	result = null;
            e.printStackTrace();  
        } finally {  
            try {  
                if (con != null)  
                    con.disconnect();  
                if (in != null)  
                    in.close();  
                if (out != null)  
                    out.close();  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
        return result;  
    }

	/**
	 * 引数で指定するURLからイメージ画像を取得する
	 * @param url イメージ画像のURL
	 * @return ビットマップイメージ
	 */
	public static Bitmap getImage(String url) {  
	    byte[] byteArray = getByteArrayFromURL(url);  
	    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);  
	} 
}

