package com.guanglun.atouch.Main;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;

import android.content.Context;
import android.content.Intent;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;

import android.widget.Button;
import android.widget.ListView;

import android.support.v4.content.ContextCompat;
import android.os.Build;
import android.provider.Settings;
import android.net.Uri;
import android.widget.TextView;

import com.guanglun.atouch.Bluetooth.BTDevice;
import com.guanglun.atouch.Bluetooth.BlueDevice;
import com.guanglun.atouch.DBManager.DBManager;
import com.guanglun.atouch.Floating.FloatService;
import com.guanglun.atouch.R;
import com.guanglun.atouch.Touch.DataProc;
import com.guanglun.atouch.Touch.TCPClient;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private TCPClient tcpclient = null;

    private BlueDevice blue_device;
    private BTDevice bt_device;

    private MainHandler mMainHandler;

    private DBManager mDBManager;

    private TextView tv_blue_status,tv_use_keymap_now,tv_auto_status;
    private DataProc mDataProc;

    private Button bt_connect_auto;
    private String pubg_now_use = null;
    private ActivityServiceMessage mActivityServiceMessage;

    private OrientationEventListener mOrientationListener;
    static private int orientation_last = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**禁止翻转**/
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        PermissionsManager.DSPermissions(this);

//        ExeCommand cmd = new ExeCommand(false).run("ls /data/local/tmp/", 60000);
//        while(cmd.isRunning())
//        {
//            try {
//                Thread.sleep(1000);
//            } catch (Exception e) {
//
//            }
//            String buf = cmd.getResult();
//            Log.i("auto",buf);
//            //do something
//        }

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

            @Override
            public void on_rotation(int ro, int w, int h) {
                byte[] temp = new byte[5];
                temp[0] = (byte)ro;
                temp[1] = (byte)(w>>8);
                temp[2] = (byte)(w);
                temp[3] = (byte)(h>>8);
                temp[4] = (byte)(h);
                byte[] temp2 = DataProc.Creat((byte)0x03,temp,temp.length);
                tcpclient.socket_send(temp2,temp2.length);
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
                mActivityServiceMessage.SendToServiceBLUEStatus(bt_device.isConnected());

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Thread.sleep(1000); // 休眠1秒
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mActivityServiceMessage.SendToServiceGetRotation();
                    }
                }).start();

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

                //if(!blue_device.isConnect)
                {
                    blue_start_check_scan();
                }
                //else{
                //    showToast("蓝牙已连接");
                //}

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

        mMainHandler = new MainHandler(this);

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

        tv_blue_status = (TextView)findViewById(R.id.tv_blue_status);
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

    char is_init = 1;
    byte test_count2 = 0;

    long testTime, testTimeLast;
    public void blue_init()
    {
        View blue_scan_view = getLayoutInflater().inflate(R.layout.controller_volume, null);

        bt_device = new BTDevice(this,new BTDevice.bt_callback(){

            @Override
            public void on_start_connect(String blue_name) {
                showToast("正在连接 "+blue_name);
            }

            @Override
            public void on_connect() {
                mActivityServiceMessage.SendToServiceBLUEStatus(bt_device.isConnected());
                //tv_blue_status.setText("蓝牙已连接");
                showToast("蓝牙连接成功");
                //printf("连接成功");
            }

            @Override
            public void on_disconnect() {
                mActivityServiceMessage.SendToServiceBLUEStatus(bt_device.isConnected());
                tv_blue_status.setText("蓝牙未连接");
                showToast("蓝牙已断开");
            }

            @Override
            public void on_receive(byte[] buffer,int len) {
                //showToast("接收到数据");
                //mDataProc.OnBlueReceive(buffer);


//                Log.e(TAG, EasyTool.bytes2hex(buffer, len));
//
//                if(is_init == 1)
//                {
//                    is_init = 0;
//                    test_count2 = buffer[len - 1];
//                }else{
//                    test_count2++;
//                    if(test_count2 != buffer[len - 1])
//                    {
//                        test_count2 = buffer[len - 1];
//
//                        Log.e(TAG, "=============>ERROR");
//                    }
//                }

                tcpclient.socket_send(buffer,len);
                //tcpclient.socket_send_not_creat_task(buffer,buffer.length);

                //long testTime = System.currentTimeMillis(); // 获取开始时间

                //Log.e(TAG,(testTime - testTimeLast) + "ms");
                //testTimeLast = testTime;

            }
        });

        bt_device.enableBluetooth();
        bt_device.init(this,blue_scan_view);
//
//        if(blue_device.init(this,blue_scan_view))
//        {
//            Log.e("DEBUG","设备初始化成功");
//        }
//
//        int ret = blue_device.check_blue();
//        if(ret == -2)
//        {
//            Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enabler, 1);
//        }
    }

    public void blue_start_check_scan()
    {
        //int ret = blue_device.check_blue();
        int ret = bt_device.check_blue();
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

            //blue_device.start_scan();
            bt_device.start_scan();
        }

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

    public void showToast(String str)
    {

        Message msg = new Message();
        msg.what = 0;
        Bundle bundle = new Bundle();
        bundle.putString("Toast",str);
        msg.setData(bundle);
        mMainHandler.sendMessage(msg);
        
    }
    



}
