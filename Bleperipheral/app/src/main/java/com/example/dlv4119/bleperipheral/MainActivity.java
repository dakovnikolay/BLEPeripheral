package com.example.dlv4119.bleperipheral;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final String DEVICE_NAME = "DKV";
    BluetoothGattServer gattServer;
    BluetoothDevice mDevice;
    BluetoothGattCharacteristic mCharacteristic;
    BluetoothGattCharacteristic nCharacteristic;

    private static final String serviceUUID = "0000fff0-0000-1000-8000-00805f9b34fb";
    private static final String characteristicUUID1 = "0000fff1-0000-1000-8000-00805f9b34fb";
    private static final String characteristicUUID2 = "0000fff2-0000-1000-8000-00805f9b34fb";
    private static final String descriptorUUID = "00002902-0000-1000-8000-00805f9b34fb";

    private boolean advertising;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RNBLEModule ble = new RNBLEModule(this, this);
        ble.addService(serviceUUID, true);
        ble.addCharacteristicToService(serviceUUID, characteristicUUID1, 17, 16);
        ble.addCharacteristicToService(serviceUUID, characteristicUUID2, 17, 6);
        ble.start();

        /*
        setContentView(R.layout.activity_main);

        BluetoothManager manager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

        gattServer = manager.openGattServer(getApplicationContext(), gattServerCallback);

        BluetoothGattService service = new BluetoothGattService(UUID.fromString(serviceUUID),
                BluetoothGattService.SERVICE_TYPE_PRIMARY);

        nCharacteristic = new BluetoothGattCharacteristic(UUID.fromString(characteristicUUID1), 16, 17);
        nCharacteristic.addDescriptor(new BluetoothGattDescriptor(UUID.fromString(descriptorUUID), 17));
        service.addCharacteristic(nCharacteristic);

        mCharacteristic = new BluetoothGattCharacteristic(UUID.fromString(characteristicUUID2),6, 17);
        mCharacteristic.addDescriptor(new BluetoothGattDescriptor(UUID.fromString(descriptorUUID), 17));
        service.addCharacteristic(mCharacteristic);

        gattServer.addService(service);

        AdvertiseSettings settings = new AdvertiseSettings.Builder()
                .setConnectable(true)
                .build();

        AdvertiseData advertiseData = new AdvertiseData.Builder()
                .setIncludeDeviceName(true)
                .setIncludeTxPowerLevel(true)
                .addServiceUuid(new ParcelUuid(UUID.fromString(serviceUUID)))
                .build();

        BluetoothAdapter adapter = manager.getAdapter();
        adapter.setName(DEVICE_NAME);
        BluetoothLeAdvertiser advertiser = adapter.getBluetoothLeAdvertiser();
        advertiser.startAdvertising(settings, advertiseData, advertiseCallback);*/
    }

    private final BluetoothGattServerCallback gattServerCallback = new  BluetoothGattServerCallback() {
        @Override
        public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
            super.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite, responseNeeded, offset, value);
            if (value != null) {
                Log.d("TAG", "value ~ " + new String(value));
            }
            gattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, null);
        }

        @Override
        public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicReadRequest(device, requestId, offset, characteristic);
            gattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, "ABC".getBytes());
        }

        @Override
        public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
            super.onConnectionStateChange(device, status, newState);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                mDevice = device;
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                mDevice = null;
            }
        }
    };

    private final AdvertiseCallback advertiseCallback = new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);
            Log.e("RNBLEModule", "Advertising success! ");
            advertising = true;

        }

        @Override
        public void onStartFailure(int errorCode) {
            advertising = false;
            Log.e("RNBLEModule", "Advertising onStartFailure: " + errorCode);
            super.onStartFailure(errorCode);
        }
    };
}
