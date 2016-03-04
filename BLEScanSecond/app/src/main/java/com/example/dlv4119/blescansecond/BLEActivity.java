package com.example.dlv4119.blescansecond;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class BLEActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);
    }

    /**
     * スキャン時間
     */
    private static final long SCAN_PERIOD = 10000;
    /**
     * BLE機器のスキャンを行うクラス
     */
    private BluetoothAdapter mBluetoothAdapter;
    /**
     * BLE機器のスキャンを別スレッドで実行するためのHandler
     */
    private Handler mHandler;
    /**
     * BLE機器をスキャンした際のコールバック
     */
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            // スキャンできた端末の情報をログ出力
            ParcelUuid[] uuids = device.getUuids();
            String uuid = "";
            if (uuids != null) {
                for (ParcelUuid puuid : uuids) {
                    uuid += puuid.toString() + " ";
                }
            }
            String msg = "name=" + device.getName() + ", bondStatus="
                    + device.getBondState() + ", address="
                    + device.getAddress() + ", type" + device.getType()
                    + ", uuids=" + uuid;
            Log.d("BLEActivity", msg);
        }
    };

    /**
     * スキャン開始ボタンタップ時のコールバックメソッド
     *
     * @param v
     */
    public void onClickScan(View v) {
        // 10秒後にBLE機器のスキャンを開始します
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            }
        }, SCAN_PERIOD);
    }

    /**
     * スキャン停止ボタンタップ時のコールバックメソッド
     *
     * @param v
     */
    public void onClickStop(View v) {
        // BLE機器のスキャンを停止します
        mBluetoothAdapter.stopLeScan(mLeScanCallback);
    }
}
