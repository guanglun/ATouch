package com.guanglun.atouch.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.guanglun.atouch.Bluetooth.BluelistAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BuleDevice {

    public final static UUID UUID_NOTIFY =
            UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");
    public final static UUID UUID_SERVICE =
            UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");

    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";

    private Context context;
    private BluetoothAdapter mBtAdapter;
    private List<BluetoothDevice> blue_device_list;
    private BluelistAdapter blue_adapter;

    public BluetoothGattCharacteristic mNotifyCharacteristic;
    private BluetoothGatt mBluetoothGatt;
    private ListView listView;

    private blue_callback blue_cb;

    public interface blue_callback {
        public void on_connect();
        public void on_disconnect();
        public void on_receive(byte[] buffer);
    }

    public BuleDevice(blue_callback blue_cb)
    {
        this.blue_cb = blue_cb;
    }

    public boolean init(Context context, ListView listview){
        this.context = context;
        this.blue_device_list = new ArrayList<BluetoothDevice>();
        this.listView = listview;



        final BluetoothManager bluetoothManager = (BluetoothManager) context
                .getSystemService(Context.BLUETOOTH_SERVICE);
        mBtAdapter = bluetoothManager.getAdapter();

        if (mBtAdapter == null) {
            return false;
        }

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(mReceiver, filter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BluetoothDevice blue_device = blue_device_list.get(i);
                connect_device(blue_device);
                //("正在连接" + blue_device.getName());
            }
        });

        return true;
    }

    public int check_blue()
    {

        if(mBtAdapter == null){
            return -1;
        }

        if(!mBtAdapter.isEnabled()) {
            return -2;
        }

        return 0;
    }

    public void start_scan()
    {
        blue_device_list.clear();
        mBtAdapter.startDiscovery();
    }
    public void stop_scan()
    {
        mBtAdapter.cancelDiscovery();
    }

    // 创建一个接收ACTION_FOUND广播的BroadcastReceiver
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //避免重复添加已经绑定过的设备
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    if(!blue_device_list.contains(device) && device.getName() != null) {
                        Log.i("DEBUG",device.getName());
                        blue_device_list.add(device);
                        blue_adapter = new BluelistAdapter(context,blue_device_list);
                        listView.setAdapter(blue_adapter);
                    }
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                Toast.makeText(context,"开始搜索",Toast.LENGTH_LONG).show();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Toast.makeText(context,"搜索完毕",Toast.LENGTH_LONG).show();
            }
        }
    };

    private void connect_device(BluetoothDevice device)
    {
        final BluetoothDevice connect_device = device;
        new Thread(new Runnable() {
            @Override
            public void run() {

                    Log.i("DEBUG","开始连接"+connect_device.getName());
                    if(mBluetoothGatt != null)
                    {
                        mBluetoothGatt.close();
                        mBluetoothGatt = null;
                    }
                    mBluetoothGatt = connect_device.connectGatt(context, false, mGattCallback);
            }
        }).run();
    }

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            //Log.i("DEBUG", "oldStatus=" + status + " NewStates=" + newState);
            if(status == BluetoothGatt.GATT_SUCCESS)
            {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    Log.i("DEBUG", "Attempting to start service discovery:" + mBluetoothGatt.discoverServices());

                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    intentAction = ACTION_GATT_DISCONNECTED;
                    mBluetoothGatt.close();
                    mBluetoothGatt = null;
                    Log.i("DEBUG", "Disconnected from GATT server.");
                }
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.i("DEBUG", "onServicesDiscovered received: " + status);
                findService(gatt.getServices());
                blue_cb.on_connect();
            } else {
                if(mBluetoothGatt.getDevice().getUuids() == null)
                    Log.i("DEBUG", "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            //broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            final byte[] data = characteristic.getValue();
            if (data != null && data.length > 0) {
                blue_cb.on_receive(data);

            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic,
                                          int status)
        {
            Log.i("DEBUG", "OnCharacteristicWrite2");
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt,
                                     BluetoothGattDescriptor bd,
                                     int status) {
            Log.i("DEBUG", "onDescriptorRead");
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt,
                                      BluetoothGattDescriptor bd,
                                      int status) {
            Log.i("DEBUG", "onDescriptorWrite");
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int a, int b)
        {
            Log.i("DEBUG", "onReadRemoteRssi");
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int a)
        {
            Log.i("DEBUG", "onReliableWriteCompleted");
        }

    };


    public void findService(List<BluetoothGattService> gattServices)
    {
        //Log.i("DEBUG", "Count is:" + gattServices.size());
        for (BluetoothGattService gattService : gattServices)
        {
            //Log.i("DEBUG", gattService.getUuid().toString());
            //Log.i("DEBUG", UUID_SERVICE.toString());
            if(gattService.getUuid().toString().equalsIgnoreCase(UUID_SERVICE.toString()))
            {
                List<BluetoothGattCharacteristic> gattCharacteristics =
                        gattService.getCharacteristics();
                //Log.i("DEBUG", "Count is:" + gattCharacteristics.size());
                for (BluetoothGattCharacteristic gattCharacteristic :
                        gattCharacteristics)
                {
                    if(gattCharacteristic.getUuid().toString().equalsIgnoreCase(UUID_NOTIFY.toString()))
                    {
                        //Log.i("DEBUG", gattCharacteristic.getUuid().toString());
                        //Log.i("DEBUG", UUID_NOTIFY.toString());
                        mNotifyCharacteristic = gattCharacteristic;
                        setCharacteristicNotification(gattCharacteristic, true);
                        //ACTION_GATT_SERVICES_DISCOVERED

                        return;
                    }
                }
            }
        }
    }

    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                              boolean enabled) {
        if (mBtAdapter == null || mBluetoothGatt == null) {
            Log.i("DEBUG", "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

    }

    private void printf(String str)
    {
        System.out.printf(str);
    }
}
