package com.example.dlv4119.test;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
    }

    public String getWifiSSID() {
        WifiManager manager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String[] apInfo = new String[4];
        apInfo[0] = String.format("SSID : %s", info.getSSID());
        return apInfo[0];
    }
}
