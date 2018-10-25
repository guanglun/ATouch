package com.guanglun.atouch.Main;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;

import android.content.Intent;

import android.content.pm.PackageManager;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.ListView;

import android.support.v4.content.ContextCompat;
import android.os.Build;
import android.provider.Settings;
import android.net.Uri;

import com.guanglun.atouch.Bluetooth.BlueScanAlertDialog;
import com.guanglun.atouch.Bluetooth.BluetoothLeService;
import com.guanglun.atouch.Bluetooth.BuleDevice;
import com.guanglun.atouch.Bluetooth.ScanBlueActivity;
import com.guanglun.atouch.DBManager.DBControl;
import com.guanglun.atouch.DBManager.DatabaseStatic;
import com.guanglun.atouch.Floating.FloatService;
import com.guanglun.atouch.DBManager.KeyMouse;
import com.guanglun.atouch.R;
import com.guanglun.atouch.Touch.TCPClient;

public class MainActivity extends AppCompatActivity {

    private TCPClient tcpclient = null;
    private BluetoothLeService mBluetoothLeService = null;
    private boolean mConnected = false;

    private View blue_view = null;
    private ListView blue_listview;
    private AlertDialog alertDialog;
    private BuleDevice bule_device;

    private MainHandler blue_handler;

    private BlueScanAlertDialog blueScanAlertDialog;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionsManager.DSPermissions(this);

        tcpclient = new TCPClient("127.0.0.1",1989,new TCPClient.socket_callback(){
            @Override
            public void on_connect_success() {
                Log.e("DEBUG","socket creat success");
            }

            @Override
            public void on_connect_fail() {
                Log.e("DEBUG","socket creat fail");
            }

            @Override
            public void on_disconnect() {
                Log.e("DEBUG","socket disconnect");
            }
        });

        Button bt_connect_auto = (Button) findViewById(R.id.bt_connect_auto);
        bt_connect_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tcpclient.connect();
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
            }
        });



        blue_init();

        blue_handler = new MainHandler(this);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SYSTEM_ALERT_WINDOW) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(MainActivity.this)) {
                    Intent intent1 = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent1, 10);
                }
            }
        }

        DBControl dbControl = new DBControl(this);

        KeyMouse keyMouse = new KeyMouse(1,"F1","F1按键");

        //dbControl.InsertDatabase(DatabaseStatic.TABLE_NAME,keyMouse);

        dbControl.SearchDatabase(DatabaseStatic.TABLE_NAME);
    }

    public void add_plan()
    {
        Intent intent = new Intent(this, FloatService.class);
        intent.putExtra(FloatService.ACTION, FloatService.SHOW);
        startService(intent);
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
                printf(new String(buffer));
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
        Intent intent = new Intent();
        intent.setClass(this, ScanBlueActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Log.e("DEBUG", "We are in onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Log.e("DEBUG", "We are in onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Log.e("DEBUG", "We are in onDestroy");
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
