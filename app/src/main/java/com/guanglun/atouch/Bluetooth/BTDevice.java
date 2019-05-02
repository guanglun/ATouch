package com.guanglun.atouch.Bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.guanglun.atouch.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.content.Context.BIND_AUTO_CREATE;

public class BTDevice {
    private final String TAG = "BTDevice";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket socket;
    private BluetoothDevice device, devicePair;
    private InputStream bt_input_stream;
    private OutputStream out;

    private boolean connected=false;

    private Activity activity;


    private Context context;
    private List<BluetoothDevice> blue_device_list;
    private BluelistAdapter blue_adapter;
    private ListView listView;
    private BlueScanAlertDialog blueScanAlertDialog;
    bt_callback bt_cb;

    public interface bt_callback {
        void on_start_connect(String blue_name);
        void on_connect();
        void on_disconnect();
        void on_receive(byte[] buffer,int len);
    }

    public BTDevice(Activity activity,bt_callback bt_cb){
        this.activity=activity;
        this.bt_cb = bt_cb;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void enableBluetooth(){
        if(bluetoothAdapter!=null) {
            if (!bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.enable();
            }
        }
    }

    public void disableBluetooth(){
        if(bluetoothAdapter!=null) {
            if (bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.disable();
            }
        }
    }

    public void connectToAddress(String address) {
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
        new ConnectThread(device).start();
    }

    public void connectToName(String name) {
        for (BluetoothDevice blueDevice : bluetoothAdapter.getBondedDevices()) {
            if (blueDevice.getName().equals(name)) {
                connectToAddress(blueDevice.getAddress());
                return;
            }
        }
    }

    public void connectToDevice(BluetoothDevice device){
        new ConnectThread(device).start();
    }

    public void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
        }
    }

    public boolean isConnected(){
        return connected;
    }

    public void send(String msg){
        try {
            out.write(msg.getBytes());
        } catch (IOException e) {
            connected = false;
        }
    }

    private class ReceiveThread extends Thread implements Runnable{
        public void run(){
            byte[] temp = new byte[1024];
            int len;
            boolean is_loop = true;
            while(is_loop) {

                try {
                    if(bt_input_stream.available() > 0)
                    {
                        len = bt_input_stream.read(temp);
                        if(len > 0)
                        {
                            bt_cb.on_receive(temp,len);
                        }
                    }

                } catch (IOException e1) {
                    e1.printStackTrace();
                    is_loop = false;
                }
            }
            bt_cb.on_disconnect();

        }
    }

    private class ConnectThread extends Thread {
        public ConnectThread(BluetoothDevice device) {
            BTDevice.this.device=device;
            try {

                BTDevice.this.socket = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {

            }
        }

        public void run() {
            bluetoothAdapter.cancelDiscovery();

            try {
                socket.connect();

                out = socket.getOutputStream();
                bt_input_stream = socket.getInputStream();
                connected=true;

                new ReceiveThread().start();
                Log.e(TAG,"connect success");
                bt_cb.on_connect();
            } catch (IOException e) {

                try {
                    socket.close();
                } catch (IOException closeException) {

                }
            }
        }
    }

    public List<BluetoothDevice> getPairedDevices(){
        List<BluetoothDevice> devices = new ArrayList<>();
        for (BluetoothDevice blueDevice : bluetoothAdapter.getBondedDevices()) {
            devices.add(blueDevice);
        }
        return devices;
    }

    public BluetoothSocket getSocket(){
        return socket;
    }

    public BluetoothDevice getDevice(){
        return device;
    }

    public void scanDevices(){
        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        activity.registerReceiver(mReceiverScan, filter);
        bluetoothAdapter.startDiscovery();
    }

    public void pair(BluetoothDevice device){
        activity.registerReceiver(mPairReceiver, new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED));
        devicePair=device;
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {

            Log.e(TAG,e.getMessage());
        }
    }

    public void unpair(BluetoothDevice device) {
        devicePair=device;
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            Log.e(TAG,e.getMessage());
        }
    }

    private BroadcastReceiver mReceiverScan = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            switch (action) {
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                    if (state == BluetoothAdapter.STATE_OFF) {
                        //Log.e(TAG,e.getMessage());
                    }
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    context.unregisterReceiver(mReceiverScan);
                    Log.e(TAG,"ACTION_DISCOVERY_FINISHED");
                    break;
                case BluetoothDevice.ACTION_FOUND:
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    //if (device.getBondState() != BluetoothDevice.BOND_BONDED)
                    {
                        //Log.e(TAG,"ACTION_FOUND "+device.getName());
                        if(!blue_device_list.contains(device) && device.getName() != null
                                && device.getName().contains("ATOUCH")) {
                            Log.e(TAG,device.getName() + device.getAddress());
                            blue_device_list.add(device);
                            blue_adapter.notifyDataSetChanged();

                        }
                    }

                    break;
            }
        }
    };

    private final BroadcastReceiver mPairReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                final int prevState	= intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);

                if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
                    context.unregisterReceiver(mPairReceiver);

                    Log.e(TAG,"pair");
                } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED){
                    context.unregisterReceiver(mPairReceiver);
                    Log.e(TAG,"Unpair");
                }
            }
        }
    };


    public boolean init(Context context, View blue_scan_view){


        this.context = context;
        this.blue_device_list = new ArrayList<BluetoothDevice>();
        this.listView = blue_scan_view.findViewById(R.id.listView);


        blueScanAlertDialog = new BlueScanAlertDialog(context,blue_scan_view);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BluetoothDevice blue_device = blue_device_list.get(i);
                Log.e(TAG,"start connect " + blue_device.getName() + " " + blue_device.getAddress());
                stop_scan();
                bt_cb.on_start_connect(blue_device.getName());
                connectToDevice(blue_device);

            }
        });

        return true;
    }

    public void start_scan()
    {
        blue_adapter = new BluelistAdapter(context,blue_device_list);
        listView.setAdapter(blue_adapter);

        blueScanAlertDialog.scan_start();
        blue_device_list.clear();
        //scanDevices();



        List<BluetoothDevice> paired = getPairedDevices();

        List<String> names = new ArrayList<>();
        for (BluetoothDevice d : paired){
            //names.add(d.getName());

            blue_device_list.add(d);
            blue_adapter.notifyDataSetChanged();
        }
    }

    public void stop_scan()
    {
        blueScanAlertDialog.scan_cancel();

    }

    public int check_blue()
    {


        return 0;
    }

}



