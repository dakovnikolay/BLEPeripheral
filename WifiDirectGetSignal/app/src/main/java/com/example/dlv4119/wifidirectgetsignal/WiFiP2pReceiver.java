package com.example.dlv4119.wifidirectgetsignal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

public class WiFiP2pReceiver extends BroadcastReceiver {

    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private MainActivity activity;

    public WiFiP2pReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel,
                           MainActivity activity) {
        this.manager = manager;
        this.channel = channel;
        this.activity = activity;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)){
//            // request available peers from the wifi p2p manager. This is an
//            // asynchronous call and the calling activity is notified with a
//            // callback on PeerListListener.onPeersAvailable()
            if (manager != null) {
//                manager.requestPeers(channel, (WifiP2pManager.PeerListListener) activity.getFragmentManager()
//                        .findFragmentById(R.id.frag_list));
            }
            Log.d(MainActivity.TAG, "P2P peers changed");
        }

        throw new UnsupportedOperationException("Not yet implemented");
    }
}
