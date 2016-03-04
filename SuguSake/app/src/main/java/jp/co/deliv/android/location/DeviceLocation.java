package jp.co.deliv.android.location;

import java.util.Timer;
import java.util.TimerTask;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.util.Log;

/**
 * デバイスの位置情報の取得をサポートするクラス。このクラスでは、通常のAndroidSDKではサポートされていない位置情報取得における
 * タイムアウトの処理を行うことができるようになっている。タイムアウト処理を行うためには、 LocationListenerWithTimeoutインターフェースを
 * 実装する必要がある。
 * @author DLV4002
 */
public class DeviceLocation {

	public static final int LOCATION_TIMEOUT = 30000;		// デフォルトタイムアウト秒は30秒
	
	private static final String TAG = "DeviceLocation";
	
	private LocationManager locationManager = null;
	private LocationListenerWithTimeout locationListener = null;
	private Location latestLocation = null;
	
	/**
	 * 位置情報取得プロバイダを強制的にGPSを使うように指示するフラグ
	 */
	private boolean useGPS = true;
	
	/**
	 * UIスレッドに対し処理をPOSTするためのハンドラ
	 */
	private Handler timeoutHandler = null;
	
	/**
	 * タイムアウト処理をハンドリングするかどうかのフラグ
	 */
	private boolean handleTimeout = true;
	private Timer timer = null;
	private int timeOut = LOCATION_TIMEOUT;		// タイムアウト秒（ミリ秒）
	
	/**
	 * デバイスの位置情報取得処理の初期化
	 * @param locationManager
	 * @param locationListener
	 */
	public DeviceLocation(LocationManager locationManager, LocationListenerWithTimeout locationListener) {
		this.locationManager = locationManager;
		this.locationListener = locationListener;
	}
	
	/**
	 * タイムアウト処理付きLocationListenerをセットする
	 * @param locationListener
	 */
	public void setLocationListener(LocationListenerWithTimeout locationListener) {
		this.locationListener = locationListener;
	}
	
	/**
	 * タイムアウト時間（ミリ秒）をセットする。0をセットするとタイムアウトは発生しなくなる。
	 * @param timeout	タイムアウト時間（ミリ秒）
	 */
	public void setTimeout(int timeout) {
		this.timeOut = timeout;
		if(timeout == 0) {
			handleTimeout = false;
		}
	}
	
	/**
	 * 位置情報の取得にGPSの使用を設定する
	 * @param enableGps	false に設定するとGPSは使用しない。
	 */
	public void enableGps(boolean enableGps) {
		this.useGPS = enableGps;
	}

	/**
	 * GPSの使用を無効にする
	 */
	public void gpsDisabled() {
		enableGps(false);
	}
	
	/**
	 * GPSの使用有無を確認する
	 * @return true の場合、GPS使用。false の場合GPSを使用しない。
	 */
	public boolean gpsEnabled() {
		return useGPS;
	}
	
	/**
	 * 処理のタイムアウト秒を指定して位置情報取得リクエストを開始する
	 * @param timeOut	タイムアウト秒
	 * @throws ProviderNotFoundException 利用可能なプロバイダが見つからなかったときにスローされる
	 */
	public void start(int timeOut) throws ProviderNotFoundException {
		setTimeout(timeOut);
		start();
	}
	
	/**
	 * 位置情報取得リクエストを開始する。
	 * @throws ProviderNotFoundException 利用可能なプロバイダが見つからなかったときにスローされる
	 */
	public void start() throws ProviderNotFoundException {
		
		// プロバイダを選択する
		String provider = getProvider();
		
		// プロバイダが選択できなかったときは例外をスローする
		if(provider == null) {
			throw new ProviderNotFoundException("Valid location provider was not found.");
		}
		
		/*
		 * 選択したプロバイダの直近の位置情報を取得しておく
		 * この位置情報はタイムアウトが発生したときの位置情報として使用される
		 */
		latestLocation = locationManager.getLastKnownLocation(provider);
		if(latestLocation != null) {
			Log.d(TAG, "Latest Location: " + latestLocation.getLatitude() + "," + latestLocation.getLongitude());
		} else {
			Log.d(TAG, "Latest Location is null.");
		}
		
		// 位置情報の取得をリクエストする
		locationManager.requestLocationUpdates(provider, 0, 0, locationListener);

		// タイムアウト処理
		if(handleTimeout) {
			Log.d(TAG, "Location Timeout is enabled.");
			
			// UIスレッドに対し処理をPOSTできるよう、Handlerを作成しておく
			timeoutHandler = new Handler();

			// タイマーによるタスクを実行する
			timer = new Timer();
			timer.schedule(new TimerTask() {
	
				@Override
				public void run() {
					// timeOutミリ秒後に呼び出される。
					Log.d(TAG, "Grab location process has been time out!!");
					timer.cancel();
					timer.purge();
					timer = null;
					
					/*
					 * TimerTask#run メソッド内部はUIスレッドと異なるためUIスレッドで動作する LocationListener を
					 * 直接呼び出すことはできない。Handlerに対し、Runnableインターフェースを渡してあげることにより、
					 * UIスレッドで LocationListener のイベントを呼び出す。
					 */
					timeoutHandler.post(new Runnable() {
						public void run() {
						
							// 位置情報の取得処理を終了
							stop();
							
							// タイムアウトハンドラを呼び出す
							locationListener.onTimeout(DeviceLocation.this, latestLocation);
						}
					});
				}
			},
			timeOut);
			Log.d(TAG, "Timeout time is " + timeOut + "ms.");
		}
	}
	
	/**
	 * 位置情報取得リクエストを終了する
	 */
	public void stop() {

		Log.d(TAG, "stop: Stoped location update process.");
		
		// タイマーの状態を確認し、必要に応じて停止する
		if(timer != null) {
			timer.cancel();
			timer.purge();
			timer = null;
		}
		
		// 位置情報の更新を停止する。
		if(locationManager != null) {
			locationManager.removeUpdates(locationListener);
		}
	}

	/**
	 * 位置情報を取得するためのプロバイダを選択する。
	 * @return	選択されたプロバイダ
	 */
	protected String getProvider() {
		String provider;
		
		if(useGPS) {
			provider = LocationManager.GPS_PROVIDER;
		} else {			
			// プロバイダの取得条件
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_COARSE);		// 要求精度
			criteria.setPowerRequirement(Criteria.POWER_LOW);	// 許容電力消費
			criteria.setSpeedRequired(false);					// 速度不要
			criteria.setAltitudeRequired(false);				// 高度不要
			criteria.setBearingRequired(false);					// 方位不要
			criteria.setCostAllowed(false);						// 費用の発生不可？
			provider = locationManager.getBestProvider(criteria, true);
		}
		return provider;
	}
	
}
