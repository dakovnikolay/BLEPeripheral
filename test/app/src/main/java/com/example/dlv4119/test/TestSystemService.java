package com.example.dlv4119.test;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by dlv4119 on 2016/01/27.
 */
public class TestSystemService {

    public String getWifiSSID() {
        WifiManager manager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String[] apInfo = new String[4];
        apInfo[0] = String.format("SSID : %s", info.getSSID());
        return apInfo[0];
    }
}
