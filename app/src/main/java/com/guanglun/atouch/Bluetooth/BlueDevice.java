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
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.guanglun.atouch.Bluetooth.BluelistAdapter;
import com.guanglun.atouch.R;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.content.Context.BIND_AUTO_CREATE;

public class BlueDevice {

    private static final String TAG = "BlueDevice";

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

    public boolean isConnect = false;

    private Context context;
    private BluetoothAdapter mBtAdapter;
    private List<BluetoothDevice> blue_device_list;
    private BluelistAdapter blue_adapter;

    public BluetoothGattCharacteristic mNotifyCharacteristic;
    private BluetoothGatt mBluetoothGatt;
    private ListView listView;

    private blue_callback blue_cb;
    private BluetoothLeService mBluetoothLeService = null;
    public String mConnectionState = BluetoothLeService.ACTION_GATT_DISCONNECTED;
    private BlueScanAlertDialog blueScanAlertDialog;

    public interface blue_callback {
        void on_start_connect(String blue_name);
        void on_connect();
        void on_disconnect();
        void on_receive(byte[] buffer);
    }

    public BlueDevice(blue_callback blue_cb)
    {
        this.blue_cb = blue_cb;
    }

    public boolean init(Context context, View blue_scan_view){

        this.context = context;
        this.blue_device_list = new ArrayList<BluetoothDevice>();
        this.listView = blue_scan_view.findViewById(R.id.listView);


        blueScanAlertDialog = new BlueScanAlertDialog(context,blue_scan_view);

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

        if (mBluetoothLeService == null) {
            Intent gattServiceIntent = new Intent(context, BluetoothLeService.class);
            context.bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        }
        initReceiver();




        return true;
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize(new BluetoothLeService.BluetoothLeServiceCallback() {
                @Override
                public void on_receive(byte[] buffer) {
                    blue_cb.on_receive(buffer);
                }
            })) {
                Log.e("MainActivity","Unable to initialize Bluetooth");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

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
        blue_adapter = new BluelistAdapter(context,blue_device_list);
        listView.setAdapter(blue_adapter);

        blueScanAlertDialog.scan_start();
        blue_device_list.clear();
        mBtAdapter.startDiscovery();


    }

    public void stop_scan()
    {
        blueScanAlertDialog.scan_cancel();
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
                    if(!blue_device_list.contains(device) && device.getName() != null
                            && device.getName().contains("ATOUCH")) {
                        Log.i("DEBUG",device.getName() + device.getAddress());
                        blue_device_list.add(device);
                        blue_adapter.notifyDataSetChanged();

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
        Log.i(TAG,"Attempt to connect device : " + device.getName());
        if (mBluetoothLeService != null) {

            stop_scan();
            if (mBluetoothLeService.connect(device.getAddress())) {
                Log.i(TAG,"Attempt to connect device : " + device.getName());
                blue_cb.on_start_connect(device.getName());
                mConnectionState = BluetoothLeService.ACTION_GATT_CONNECTING;
            }
        }else{
            Log.i(TAG,"mBluetoothLeService is null");
        }
    }

    private void initReceiver() {
        Log.i(TAG, "initReceiver()");
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);

        context.registerReceiver(mGattUpdateReceiver, intentFilter);
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                Log.i(TAG, "ACTION_GATT_CONNECTED!!!");

                isConnect = true;
                mConnectionState = BluetoothLeService.ACTION_GATT_CONNECTED;

                blue_cb.on_connect();

            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                Log.i(TAG, "ACTION_GATT_DISCONNECTED!!!");

                isConnect = false;
                mConnectionState = BluetoothLeService.ACTION_GATT_DISCONNECTED;
                mBluetoothLeService.close();
                blue_cb.on_disconnect();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                mBluetoothLeService.getSupportedGattServices();
            }
        }
    };

    private void inputMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Message to send");

        final EditText input = new EditText(context);

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = input.getText().toString();
                btSendBytes(text.getBytes());
            }
        });


        builder.show();
    }
    public void btSendBytes(byte[] data) {
        if (mBluetoothLeService != null &&
                isConnect) {
            mBluetoothLeService.writeCharacteristic(data);
        }
    }



}
