package com.example.dlv4119.wifidirectgetsignal;

import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = "Wifi Direct";

    private final IntentFilter intentFilter = new IntentFilter();

    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // リストビュー
//        String[] members = { "mhidaka", "rongon_xp", "kacchi0516", "kobashinG",
//                "seit", "kei_i_t", "furusin_oriver" };
//        lv = (ListView) findViewById(R.id.listview);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, members);
//        lv.setAdapter(adapter);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        // アイテムを追加します
        adapter.add("red");
        adapter.add("green");
        adapter.add("blue");
        ListView listView = (ListView) findViewById(R.id.listview);
        // アダプターを設定します
        listView.setAdapter(adapter);


        //リスト項目がクリックされた時の処理
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                String item = (String) listView.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), item + " clicked",
                        Toast.LENGTH_LONG).show();
            }
        });

        lv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //リスト項目が選択された時の処理
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                String item = (String) listView.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), item + " selected",
                        Toast.LENGTH_LONG).show();
            }

            //リスト項目がなにも選択されていない時の処理
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(), "no item selected",
                        Toast.LENGTH_LONG).show();
            }
        });

        //リスト項目が長押しされた時の処理
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                String item = (String) listView.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), item + " long clicked",
                        Toast.LENGTH_LONG).show();
                return false;
            }
        });




        // add necessary intent values to be matched.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

//        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
//        channel = manager.initialize(this, getMainLooper(), null);

//        if (!isWifiP2pEnabled) {
//            Toast.makeText(MainActivity.this, R.string.p2p_off_warning,
//                    Toast.LENGTH_SHORT).show();
//        }

//        final DeviceFragment fragment = (DeviceFragment) getFragmentManager()
//                .findFragmentById(R.id.fragment_device);
//        fragment.onInitiateDiscovery();

//        manager.discoverPeers(channel, new ActionListener() {
//
//            @Override
//            public void onSuccess() {
//                Toast.makeText(MainActivity.this, "Discovery Initiated",
//                        Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(int reasonCode) {
//                Toast.makeText(MainActivity.this, "Discovery Failed : " + reasonCode,
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
    }


//    @Override
//    public void onChannelDisconnected() {
//        // we will try once more
//        if (manager != null && !retryChannel) {
//            Toast.makeText(this, "Channel lost. Trying again", Toast.LENGTH_LONG).show();
//            resetData();
//            retryChannel = true;
//            manager.initialize(this, getMainLooper(), this);
//        } else {
//            Toast.makeText(this,
//                    "Severe! Channel is probably lost premanently. Try Disable/Re-Enable P2P.",
//                    Toast.LENGTH_LONG).show();
//        }
//    }
//    @Override
//    public void cancelDisconnect() {
//
//        /*
//         * A cancel abort request by user. Disconnect i.e. removeGroup if
//         * already connected. Else, request WifiP2pManager to abort the ongoing
//         * request
//         */
//        if (manager != null) {
//            final DeviceListFragment fragment = (DeviceListFragment) getFragmentManager()
//                    .findFragmentById(R.id.frag_list);
//            if (fragment.getDevice() == null
//                    || fragment.getDevice().status == WifiP2pDevice.CONNECTED) {
//                disconnect();
//            } else if (fragment.getDevice().status == WifiP2pDevice.AVAILABLE
//                    || fragment.getDevice().status == WifiP2pDevice.INVITED) {
//
//                manager.cancelConnect(channel, new ActionListener() {
//
//                    @Override
//                    public void onSuccess() {
//                        Toast.makeText(MainActivity.this, "Aborting connection",
//                                Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onFailure(int reasonCode) {
//                        Toast.makeText(MainActivity.this,
//                                "Connect abort request failed. Reason Code: " + reasonCode,
//                                Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        }
//    }
//
//    @Override
//    public void showDetails(WifiP2pDevice device) {
//        DeviceDetailFragment fragment = (DeviceDetailFragment) getFragmentManager()
//                .findFragmentById(R.id.frag_detail);
//        fragment.showDetails(device);
//
//    }
//    @Override
//    public void connect(WifiP2pConfig config) {
//        manager.connect(channel, config, new ActionListener() {
//
//            @Override
//            public void onSuccess() {
//                // WiFiDirectBroadcastReceiver will notify us. Ignore for now.
//            }
//
//            @Override
//            public void onFailure(int reason) {
//                Toast.makeText(MainActivity.this, "Connect failed. Retry.",
//                        Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//    public void disconnect() {
//        final DeviceDetailFragment fragment = (DeviceDetailFragment) getFragmentManager()
//                .findFragmentById(R.id.frag_detail);
//        fragment.resetViews();
//        manager.removeGroup(channel, new ActionListener() {
//
//            @Override
//            public void onFailure(int reasonCode) {
//                Log.d(TAG, "Disconnect failed. Reason :" + reasonCode);
//
//            }
//
//            @Override
//            public void onSuccess() {
//                fragment.getView().setVisibility(View.GONE);
//            }
//
//        });
//    }


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
