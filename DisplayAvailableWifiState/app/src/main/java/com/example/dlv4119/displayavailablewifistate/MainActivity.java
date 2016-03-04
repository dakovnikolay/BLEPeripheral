package com.example.dlv4119.displayavailablewifistate;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button)findViewById(R.id.btn);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshList();
            }
        });
    }

    public String[] getWifiState() {
        WifiManager manager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String[] apInfo = new String[4];
        apInfo[0] = "WiFi情報";
        // SSID取得
        apInfo[1] = String.format("SSID : %s", info.getSSID());
        // MACアドレスを取得
        apInfo[2] = String.format("MAC Address : %s", info.getMacAddress());
        // 受信信号強度&信号レベルを取得
        int rssi = info.getRssi();
        int level = WifiManager.calculateSignalLevel(rssi, 5);
        apInfo[3] = String.format("RSSI : %d / Level : %d/4", rssi, level);
        return apInfo;
    }

    /**
     * 初回起動時のリストビューを作成
     */
    public void refreshList(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getWifiState());
        // リストビューオブジェクトを作成する
        android.widget.ListView listView = (android.widget.ListView) findViewById(R.id.listView);
        // アダプターを設定します
        listView.setAdapter(adapter);
        // リストビューのアイテムがクリックされた時に呼び出されるコールバックリスナーを登録します
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                android.widget.ListView listView = (android.widget.ListView) parent;
                // クリックされたアイテムを取得します
                String item = (String) listView.getItemAtPosition(position);
                Toast.makeText(MainActivity.this, item, Toast.LENGTH_LONG).show();
            }
        });

        // リストビューのアイテムが選択された時に呼び出されるコールバックリスナーを登録します
        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                android.widget.ListView listView = (android.widget.ListView) parent;
                // 選択されたアイテムを取得します
                String item = (String) listView.getSelectedItem();
                Toast.makeText(MainActivity.this, item, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
