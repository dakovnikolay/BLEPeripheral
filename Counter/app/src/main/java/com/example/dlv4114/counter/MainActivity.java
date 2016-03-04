package com.example.dlv4114.counter;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private final static String TAG = "connectWifi";

    int count;
    int netWorkId;
    private TextView wifiState_text;
    private Button wifiChange_button;

    String ssid;
    WifiManager mWifimanager;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button bt_connection1;
        Button bt_connection2;
        Button doConnection_button;
        wifiChange_button = (Button) findViewById(R.id.bt_wifi);
        wifiState_text = (TextView) findViewById(R.id.wifi_stateText);
        bt_connection1 = (Button) findViewById(R.id.bt_connection1);
        bt_connection2 = (Button) findViewById(R.id.bt_connection2);
        doConnection_button = (Button) findViewById(R.id.bt_doConnection);

        /** Called when the activity is first created. */
        mWifimanager = (WifiManager) getSystemService(WIFI_SERVICE);

        if (mWifimanager.isWifiEnabled()) {
            // WifiOn時のラベルをボタンに適用
            wifiChange_button.setText(R.string.toOff_label);
            wifiState_text.setText(R.string.onNow_label);
        } else {
            // WifiOff時のラベルをボタンに適用
            wifiChange_button.setText(R.string.toOn_label);
            wifiState_text.setText(R.string.offNow_label);
        }

        wifiChange_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == wifiChange_button) {
                    if (mWifimanager.isWifiEnabled()) {
                        // WifiをOnからOffに
                        mWifimanager.setWifiEnabled(false);
                        wifiChange_button.setText(R.string.toOn_label);
                        wifiState_text.setText(R.string.offNow_label);
                    } else {
                        // WifiをOffからOnに
                        mWifimanager.setWifiEnabled(true);
                        wifiChange_button.setText(R.string.toOff_label);
                        wifiState_text.setText(R.string.onNow_label);
                    }
                }
            }
        });

        //接続１読み込みボタンの処理
        bt_connection1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                String ssid = "秋元真夏 の iPhone";
                WifiConfiguration config = new WifiConfiguration();
                config.SSID = "\"" + ssid + "\"";
                config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                config.preSharedKey = "\"akimotoManatsu\"";
                netWorkId = wifiManager.addNetwork(config); // 失敗した場合は-1となります
                wifiManager.saveConfiguration();
                wifiManager.updateNetwork(config);
            }
        });

        //接続２読み込みボタンの処理
        bt_connection2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "configの編集");
                WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                ssid = "dlvinnwr0g";
                WifiConfiguration config = new WifiConfiguration();
                config.SSID = "\"" + ssid + "\"";
                config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                config.preSharedKey = "\"Buff@l0sghy32\"";
                netWorkId = wifiManager.addNetwork(config); // 失敗した場合は-1となります
                wifiManager.saveConfiguration();
                wifiManager.updateNetwork(config);
                Log.d(TAG, "configの編集終了 (" + ssid + ")");
            }
        });

        doConnection_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                //int networkId = 0; // 上記設定で取得できたものを使用
                String targetSSID = ssid;
                // WiFi機能が無効の状態で呼び出されるとSSID検索の所でnullとなるので念のため例外処理を行なう
                try {
                    // ssidの検索を開始
                    wifiManager.startScan();
                    List<ScanResult> apList = wifiManager.getScanResults();
//////////////////////////////////////////////////////////////////////////////

                    String[] aps = new String[apList.size()];
                    for (int i = 0; i < apList.size(); i++) {
                        aps[i] = "SSID:" + apList.get(i).SSID + "\n"
                                + apList.get(i).frequency + "MHz " + apList.get(i).level + "dBm";
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>
                            (MainActivity.this, android.R.layout.simple_list_item_1, aps);

                    lv = (ListView) findViewById(R.id.list_wifiApList);
                    lv.setAdapter(adapter);
                    Log.d("test", "リストを表示しました。");

//////////////////////////////////////////////////////////////////////////////
                    for (ScanResult result : wifiManager.getScanResults()) {
//                        for (apList:
//                             wifiManager.getScanResults()) {
                            // Android4.2以降よりダブルクォーテーションが付いてくるので除去
                            String resultSSID = result.SSID.replace("\"", "");
                            if (resultSSID.equals(targetSSID)) {
                                // 接続を行う
                                if (netWorkId > 0) {
                                    // 先に既存接続先を無効にしてから接続します
                                    for (WifiConfiguration c0 : wifiManager.getConfiguredNetworks()) {
                                        wifiManager.enableNetwork(c0.networkId, false);
                                    }
                                    wifiManager.enableNetwork(netWorkId, true);
                                }
                                break;
                            }

                    }
                } catch (NullPointerException e) {
                    // 適切な例外処理をしてください。
                }
            }
        });
    }

    @Override
    protected void onPause() {
        SharedPreferences pref = getSharedPreferences("count_status", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("count_int", (count));
        editor.commit();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pref = getSharedPreferences("count_status", Context.MODE_PRIVATE);
        count = (pref.getInt("count_int", 0));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
