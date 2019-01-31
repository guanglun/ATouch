package com.guanglun.atouch.Main;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import android.support.v4.content.ContextCompat;
import android.os.Build;
import android.provider.Settings;
import android.net.Uri;
import android.widget.TextView;
import android.widget.Toast;

import com.guanglun.atouch.Bluetooth.BlueScanAlertDialog;
import com.guanglun.atouch.Bluetooth.BluetoothLeService;
import com.guanglun.atouch.Bluetooth.BuleDevice;
import com.guanglun.atouch.Bluetooth.ScanBlueActivity;
import com.guanglun.atouch.DBManager.DBControl;
import com.guanglun.atouch.DBManager.DBManager;
import com.guanglun.atouch.DBManager.DatabaseStatic;
import com.guanglun.atouch.Floating.FloatService;
import com.guanglun.atouch.DBManager.KeyMouse;
import com.guanglun.atouch.R;
import com.guanglun.atouch.Touch.DataProc;
import com.guanglun.atouch.Touch.TCPClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TCPClient tcpclient = null;
    private BluetoothLeService mBluetoothLeService = null;

    private View blue_view = null;
    private ListView blue_listview;
    private AlertDialog alertDialog;
    private BuleDevice bule_device;

    private MainHandler blue_handler;

    private BlueScanAlertDialog blueScanAlertDialog;
    private DBManager mDBManager;

    private TextView tv_use_keymap_now,tv_auto_status;
    private DataProc mDataProc;

    private Button bt_connect_auto;
    private String pubg_now_use = null;
    private ActivityServiceMessage mActivityServiceMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionsManager.DSPermissions(this);

        mActivityServiceMessage = new ActivityServiceMessage(new ActivityServiceMessage.MessengerCallback() {
            @Override
            public void on_use(String name) {
                pubg_now_use = name;
                tv_use_keymap_now.setText("使用映射：" + pubg_now_use);

                if(pubg_now_use != null)
                {
                    byte[] temp = mDBManager.GetByteFromPUBG(pubg_now_use);
                    temp = DataProc.Creat((byte)0x01,temp,temp.length);
                    tcpclient.socket_send(temp,temp.length);
                }
            }
        });



        tcpclient = new TCPClient("127.0.0.1",1989,new TCPClient.socket_callback(){
            @Override
            public void on_connect_success() {
                Log.e("DEBUG","socket creat success");
                bt_connect_auto.setText("断开");
                tv_auto_status.setText("映射已连接");

                if(pubg_now_use != null)
                {
                    byte[] temp = mDBManager.GetByteFromPUBG(pubg_now_use);
                    temp = DataProc.Creat((byte)0x01,temp,temp.length);
                    tcpclient.socket_send(temp,temp.length);
                }

                mActivityServiceMessage.mUiautoStatus = true;
                mActivityServiceMessage.SendToServiceUiautoStatus(mActivityServiceMessage.mUiautoStatus);
            }

            @Override
            public void on_connect_fail() {
                Log.e("DEBUG","socket creat fail");
                bt_connect_auto.setText("连接");
                tv_auto_status.setText("映射未连接");
                mActivityServiceMessage.mUiautoStatus = false;
                mActivityServiceMessage.SendToServiceUiautoStatus(mActivityServiceMessage.mUiautoStatus);
            }

            @Override
            public void on_disconnect() {
                Log.e("DEBUG","socket disconnect");
                bt_connect_auto.setText("连接");
                tv_auto_status.setText("映射未连接");
                mActivityServiceMessage.mUiautoStatus = false;
                mActivityServiceMessage.SendToServiceUiautoStatus(mActivityServiceMessage.mUiautoStatus);
            }

            @Override
            public void on_receive(byte[] buf, int len) {

                //Log.e("DEBUG","on_receive");
                mDataProc.OnATouchReceive(buf,len);
            }

        });

        mDataProc = new DataProc(this,tcpclient,mActivityServiceMessage);

        bt_connect_auto = (Button) findViewById(R.id.bt_connect_auto);
        bt_connect_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bt_connect_auto.getText().equals("连接"))
                {
                    tcpclient.connect();
                }else {
                    tcpclient.disconnect();
                }

            }
        });

        Button bt_blue_scan = (Button) findViewById(R.id.bt_blue_scan);
        bt_blue_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blue_start_check_scan();

            }
        });

        Button bt_creat_plan = (Button) findViewById(R.id.bt_creat_plan);
        bt_creat_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_plan();
                moveTaskToBack(true);
            }
        });

        blue_init();

        blue_handler = new MainHandler(this);

        try{
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(MainActivity.this)) {
                        Intent intent1 = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent1, 10);
                    }
                }
            }
        }catch(Exception e){
            PermissionsManager.applyCommonPermission(this);

        }

        tv_auto_status = (TextView)findViewById(R.id.tv_auto_status);
        tv_use_keymap_now = (TextView)findViewById(R.id.tv_use_keymap_now);
        ListView lv_table = (ListView)findViewById(R.id.lv_table);

        mDBManager = new DBManager(this, lv_table, new DBManager.DBManagerCallBack() {
            @Override
            public void on_update_use_table_now(String Name) {

                tv_use_keymap_now.setText("使用映射：" + Name);
                pubg_now_use = Name;
                //Log.i("DEBUG",keyMouseListUseNow.toString());

                if(pubg_now_use != null)
                {
                    byte[] temp = mDBManager.GetByteFromPUBG(pubg_now_use);
                    temp = DataProc.Creat((byte)0x01,temp,temp.length);
                    tcpclient.socket_send(temp,temp.length);
                }
            }
        });

        Intent intent = new Intent(this, FloatService.class);
        intent.putExtra(FloatService.ACTION, FloatService.SHOW);
        intent.putExtra("IsStartUp", "true");
        bindService(intent,mActivityServiceMessage.mServiceConnection,Context.BIND_AUTO_CREATE);

    }

    public void add_plan()
    {
        Intent intent = new Intent(this, FloatService.class);
        intent.putExtra(FloatService.ACTION, FloatService.SHOW);
        intent.putExtra("IsStartUp", "true");
        bindService(intent,mActivityServiceMessage.mServiceConnection,Context.BIND_AUTO_CREATE);
    }


    public void blue_init()
    {
        blue_view = getLayoutInflater().inflate(R.layout.controller_volume, null);
        blue_listview = blue_view.findViewById(R.id.listView);

        blueScanAlertDialog = new BlueScanAlertDialog(this,blue_view);

        bule_device = new BuleDevice(new BuleDevice.blue_callback(){

            @Override
            public void on_connect() {

                blueScanAlertDialog.scan_cancel();
                showToast("连接成功");
                //printf("连接成功");
            }

            @Override
            public void on_disconnect() {

            }

            @Override
            public void on_receive(byte[] buffer) {
                //showToast("接收到数据");
                //mDataProc.OnBlueReceive(buffer);
                Log.i("DEBUG", "BLE "+buffer.length);
                tcpclient.socket_send(buffer,buffer.length);
            }
        });

        if(bule_device.init(this,blue_listview))
        {
            Log.e("DEBUG","设备初始化成功");
        }

        int ret = bule_device.check_blue();
        if(ret == -2)
        {
            Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enabler, 1);
        }
    }

    public void blue_start_check_scan()
    {
        int ret = bule_device.check_blue();
        if(ret == -2)
        {
            showToast("蓝牙打开后请重新扫描");
            Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enabler, 1);

        }else if(ret == -1)
        {
            showToast("设备不支持蓝牙功能");
        }else if(ret == 0)
        {
            blueScanAlertDialog.scan_start();
            bule_device.start_scan();
        }

    }

    public void new_activity()
    {
        Intent intent = new Intent(this, FloatService.class);
        intent.putExtra(FloatService.ACTION, FloatService.SHOW);
        intent.putExtra("IsStartUp", "true");
        bindService(intent,mActivityServiceMessage.mServiceConnection,Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDBManager.LoadTableList();
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Log.e("DEBUG", "We are in onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void showToast(String str)
    {

        Message msg = new Message();
        msg.what = 0;
        Bundle bundle = new Bundle();
        bundle.putString("Toast",str);
        msg.setData(bundle);
        blue_handler.sendMessage(msg);


    }

    private void printf(String str)
    {
        System.out.printf(str);
    }







}
