package com.example.dlv4119.bluetoothsample;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class BluetoothSample extends AppCompatActivity {

    final String TAG = "Result";

    private ListView mListView;
    private ArrayList<String> mScanList;
    private ArrayAdapter<String> mAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private String mResult = "";

    // ブロードキャストレシーバーの操作
    private BroadcastReceiver mBluetoothSearchReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {
                // Bluetoothデバイスがインテントを受け取ったときの操作
                //Bluetoothオブジェクトを取得
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // RSSI値読み出し
                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);

                mScanList = new ArrayList<String>();
                mScanList.add(mResult);

                mResult = "";
                mResult += "Name : " + device.getName() + "\n"
                        + "Device Class : " + device.getBluetoothClass().getDeviceClass() + "\n"
                        + "MAC Address : " + device.getAddress() + "\n"
                        + "State : " + getBondState(device.getBondState()) + "\n"
                        + "RSSI : " + String.valueOf(rssi) + "\n";
                Log.d(TAG, "デバイス名を取得しました。");
                Log.d(TAG, "デバイスクラスを取得しました。");
                Log.d(TAG, "MACアドレスを取得しました。");
                Log.d(TAG, "接続状態を取得しました。");
                Log.d(TAG, "電波強度(RSSI)を取得しました。");
                mScanList.add(mResult);

                mAdapter.clear();
                mAdapter.addAll(mScanList);
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

//        TextView btText = (TextView) findViewById(R.id.bt_text);
//        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
//        // �y�A�����O�ς݂̃f�o�C�X�ꗗ���擾
//        Set<BluetoothDevice> btDevices = btAdapter.getBondedDevices();
//        String devList = "";
//        for (BluetoothDevice device : btDevices) {
//            devList += "Device : " + device.getName() + "(" + getBondState(device.getBondState()) + ")\n";
//        }
//        btText.setText(devList);

        // BLE�ɑΉ����Ă��邩�ǂ����𔻒f���Ă���
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            // BLE�����p�o���Ȃ��[���������ꍇ�̏������L�q
            Log.d("Available ble", "���̒[����BLE�ɑΉ����Ă��܂���B");
        }

        mListView = (ListView) findViewById(R.id.listView);
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        IntentFilter bluetoothFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mBluetoothSearchReceiver, bluetoothFilter);
        mBluetoothAdapter = mBluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.startDiscovery(); //開始
    }

    /**
     * 接続状態を判断し、返す
     *
     * @param state
     * @return 接続状態
     */
    String getBondState(int state) {
        String strState;
        switch (state) {
            case BluetoothDevice.BOND_BONDED:
                strState = "接続履歴あり";
                break;
            case BluetoothDevice.BOND_BONDING:
                strState = "接続中";
                break;
            case BluetoothDevice.BOND_NONE:
                strState = "接続履歴なし";
                break;
            default:
                strState = "エラー";
        }
        return strState;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBluetoothAdapter.cancelDiscovery(); //�����L�����Z��
        unregisterReceiver(mBluetoothSearchReceiver); //filter����
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bluetooth_sample, menu);
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
