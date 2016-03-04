package com.example.dlv4119.wifidiriectscanlist;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    WifiManager wifiManager = null;
    List<ScanResult> scanResults = null;
    ArrayAdapter<String> adapter = null;
    String aps = null;
    ArrayList<String> apList = null;

    private Timer mTimer = null;
    private Handler mHandler = null;
    private UpdateTimerTask mUpdateTimerTask = null;

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.dlv4119.wifidirectscanlist.R.layout.activity_main);

        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        // 文字列コレクションの取得
        apList = getScanList();
        // 文字列型アダプターの作成 (文字列コレクションを設定)
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, apList);
        // リストビューオブジェクトを作成する
        android.widget.ListView listView = (android.widget.ListView) findViewById(com.example.dlv4119.wifidirectscanlist.R.id.listView);
        // アダプターを設定します
        listView.setAdapter(adapter);

        mHandler = new Handler();
        mTimer = new Timer();
        mUpdateTimerTask = new UpdateTimerTask();
        // taskオブジェクトのrunメソッドを現在時刻からdelay（ミリ秒）後を開始時点としてperiod（ミリ秒）間隔で実行する
        mTimer.schedule(mUpdateTimerTask, 1000, 1000);
    }

    /**
     * リストの更新
     */
    public void updateList() {
        // リストの更新
        apList = getScanList();
        adapter.clear();
        adapter.addAll(apList);
        adapter.notifyDataSetChanged();
        Log.d("Test", "リストを更新しました。");
    }

    /**
     * スキャン結果からWiFi DirectのみのAPリストを作成
     *
     * @return 接続可能なWiFi Direct APリスト　(文字列コレクション)
     */
    public ArrayList getScanList() {
        scanResults = wifiManager.getScanResults();
        apList = new ArrayList<>();
        Log.d("ScanResultSize", String.valueOf(scanResults.size()));
        for (int i = 0; i < scanResults.size(); i++) {
            aps = "SSID:" + scanResults.get(i).SSID + "\n"
                    + "チャンネル周波数：" + scanResults.get(i).frequency + "MHz " + "\n"
                    + "信号レベル：" + scanResults.get(i).level + "dBm";
            apList.add(aps);

        }
//        apList =  new ArrayList<String>(Arrays.asList(aps));
        return apList;
    }

    /**
     * 繰り返しタスクを設定しているクラス
     */
    public class UpdateTimerTask extends TimerTask {
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                public void run() {
                    updateList();
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }


}
