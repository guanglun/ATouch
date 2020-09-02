package com.guanglun.atouch.Main;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.OrientationEventListener;
import android.view.View;

import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import android.support.v4.content.ContextCompat;
import android.os.Build;
import android.provider.Settings;
import android.net.Uri;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.guanglun.atouch.Bluetooth.BTDevice;
import com.guanglun.atouch.Bluetooth.BlueDevice;
import com.guanglun.atouch.DBManager.DBManager;
import com.guanglun.atouch.Floating.FloatService;

import com.guanglun.atouch.R;
import com.guanglun.atouch.Serial.OpenVIO;
import com.guanglun.atouch.Serial.SerialPort;
import com.guanglun.atouch.Serial.USBPermission;
import com.guanglun.atouch.Touch.DataProc;
import com.guanglun.atouch.Touch.TCPClient;
import com.guanglun.atouch.upgrade.UpgradeHardware;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private TCPClient tcpclient = null;

    private BlueDevice blue_device;
    private BTDevice bt_device;

    private MainHandler mMainHandler;

    private DBManager mDBManager;

    private TextView tv_blue_status, tv_upgrade, tv_auto_status, tv_serial;
    private DataProc mDataProc;

    private Button bt_connect_auto;
    private String pubg_now_use = null;
    private ActivityServiceMessage mActivityServiceMessage;

    private OrientationEventListener mOrientationListener;
    static private int orientation_last = 0;

    private AlertDialog dialog_upgrade = null;

    private UpgradeHardware upgradeHardware = null;

    private Context context;

    private boolean recv_version = false;

    private SerialPort serialPort;
    private OpenVIO openvio = null;
    private USBPermission usbPermission;
    private Button bt_serial;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AppConfig config = new AppConfig();
        config.init(this);


        context = this;

        openvio = new OpenVIO(this, new OpenVIO.OpenVIOCallback() {
            @Override
            public void on_connect_success() {
                bt_serial.setEnabled(true);
                bt_serial.setText("OPENVIO断开");
                showToast("OPENVIO已连接");
                tv_serial.setText("OPENVIO已连接");
            }

            @Override
            public void on_connect_fail() {
                bt_serial.setEnabled(true);
                bt_serial.setText("OPENVIO连接");
                showToast("OPENVIO连接失败");
                tv_serial.setText("OPENVIO未连接");
            }

            @Override
            public void on_disconnect() {
                bt_serial.setEnabled(true);
                bt_serial.setText("OPENVIO连接");
                showToast("OPENVIO已断开");
                tv_serial.setText("OPENVIO未连接");
            }

            @Override
            public void on_receive(byte[] buf, int len) {
                //Log.e(TAG, EasyTool.bytes2hex(buf, len));
                tcpclient.socket_send(buf, len);
            }
        });

        serialPort = new SerialPort(this, new SerialPort.serialCallback() {
            @Override
            public void on_connect_success() {
                bt_serial.setEnabled(true);
                bt_serial.setText("串口断开");
                showToast("串口已连接");
                tv_serial.setText("串口已连接");
            }

            @Override
            public void on_connect_fail() {
                bt_serial.setEnabled(true);
                bt_serial.setText("串口连接");
                showToast("串口连接失败");
                tv_serial.setText("串口未连接");
            }

            @Override
            public void on_disconnect() {
                bt_serial.setEnabled(true);
                bt_serial.setText("串口连接");
                showToast("串口已断开");
                tv_serial.setText("串口未连接");
            }

            @Override
            public void on_receive(byte[] buf, int len) {
                // Log.e(TAG, EasyTool.bytes2hex(buf, len));
                tcpclient.socket_send(buf, len);
            }
        });
        usbPermission = new USBPermission(this, serialPort, openvio);

        /** 禁止翻转 **/
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        try {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(MainActivity.this)) {
                        Intent intent1 = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent1, 10);
                    }
                }
            }
        } catch (Exception e) {
            PermissionsManager.applyCommonPermission(this);
        }

        PermissionsManager.DSPermissions(this);

        // ExeCommand cmd = new ExeCommand(false).run("ls /data/local/tmp/", 60000);
        // while(cmd.isRunning())
        // {
        // try {
        // Thread.sleep(1000);
        // } catch (Exception e) {
        //
        // }
        // String buf = cmd.getResult();
        // Log.i("auto",buf);
        // //do something
        // }

        mActivityServiceMessage = new ActivityServiceMessage(new ActivityServiceMessage.MessengerCallback() {
            @Override
            public void on_use(String name) {
                pubg_now_use = name;

                if (pubg_now_use != null) {
                    byte[] temp = mDBManager.GetByteFromPUBG(pubg_now_use);
                    temp = DataProc.Creat((byte) 0x01, temp, temp.length);
                    tcpclient.socket_send(temp, temp.length);
                }
            }

            @Override
            public void on_rotation(int ro, int w, int h) {
                byte[] temp = new byte[5];
                temp[0] = (byte) ro;
                temp[1] = (byte) (w >> 8);
                temp[2] = (byte) (w);
                temp[3] = (byte) (h >> 8);
                temp[4] = (byte) (h);
                byte[] temp2 = DataProc.Creat((byte) 0x03, temp, temp.length);
                tcpclient.socket_send(temp2, temp2.length);
            }
        });

        tcpclient = new TCPClient("127.0.0.1", 1989, new TCPClient.socket_callback() {
            @Override
            public void on_connect_success() {
                recv_version = false;
                Log.e("DEBUG", "socket creat success");
                bt_connect_auto.setText("断开");
                tv_auto_status.setText("映射已连接");

                if (pubg_now_use != null) {
                    byte[] temp = mDBManager.GetByteFromPUBG(pubg_now_use);
                    temp = DataProc.Creat((byte) 0x01, temp, temp.length);
                    tcpclient.socket_send(temp, temp.length);
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
                Log.e("DEBUG", "socket creat fail");
                bt_connect_auto.setText("连接");
                tv_auto_status.setText("映射未连接");
                mActivityServiceMessage.mUiautoStatus = false;
                mActivityServiceMessage.SendToServiceUiautoStatus(mActivityServiceMessage.mUiautoStatus);
            }

            @Override
            public void on_disconnect() {
                Log.e("DEBUG", "socket disconnect");
                bt_connect_auto.setText("连接");
                tv_auto_status.setText("映射未连接");
                mActivityServiceMessage.mUiautoStatus = false;
                mActivityServiceMessage.SendToServiceUiautoStatus(mActivityServiceMessage.mUiautoStatus);
            }

            @Override
            public void on_receive(byte[] buf, int len) {

                // Log.e("DEBUG","on_receive" +len);
                byte[] by = new byte[len];
                if (recv_version == false) {
                    recv_version = true;

                    if (len < 10) {
                        System.arraycopy(buf, 0, by, 0, len);
                        final String str = new String(by);

                        Log.e("DEBUG", "后台程序版本：" + str);

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            public void run() {
                                showToast("后台程序版本：" + str);
                            }
                        });
                    }

                } else {
                    mDataProc.OnATouchReceive(buf, len);
                }

            }

        });

        mDataProc = new DataProc(this, tcpclient, mActivityServiceMessage);

        bt_connect_auto = (Button) findViewById(R.id.bt_connect_auto);
        bt_connect_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bt_connect_auto.getText().equals("连接")) {
                    tcpclient.connect();
                } else {
                    tcpclient.disconnect();
                }

            }
        });

        Button bt_blue_scan = (Button) findViewById(R.id.bt_blue_scan);
        bt_blue_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // if(!blue_device.isConnect)
                {
                    blue_start_check_scan();
                }
                // else{
                // showToast("蓝牙已连接");
                // }

            }
        });

        bt_serial = (Button) findViewById(R.id.bt_serial);
        bt_serial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bt_serial.setEnabled(false);
                if(!serialPort.isOpen)
                {
                    Log.i("OPENVIO","1");
                    if(!openvio.isOpen)
                    {
                        Log.i("OPENVIO","2");
                        usbPermission.tryGetUsbPermission();
                        return;
                    }
                }

                Log.i("OPENVIO","3");
                if (serialPort.isOpen){
                    Log.i("OPENVIO","4");
                    serialPort.close();
                }
                if (openvio.isOpen){
                    Log.i("OPENVIO","5");
                    openvio.close();
                }

            }
        });

        Button bt_wifi = (Button) findViewById(R.id.bt_wifi);
        bt_wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final View view = LayoutInflater.from(context).inflate(R.layout.wifi_set, null);

                final AlertDialog dialog1 = new AlertDialog.Builder(context)
                        // .setIcon(R.mipmap.icon)//设置标题的图片
                        .setTitle("配置ATouch连接路由器")// 设置对话框的标题
                        .setView(view).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                EditText et_wifi_ssid = (EditText) view.findViewById(R.id.et_wifi_ssid);
                                EditText et_wifi_passwd = (EditText) view.findViewById(R.id.et_wifi_passwd);
                                EditText et_wifi_ip = (EditText) view.findViewById(R.id.et_wifi_ip);

                                String str = "[WIFI]";
                                str += et_wifi_ssid.getText().toString() + ";";
                                str += et_wifi_passwd.getText().toString() + ";";
                                str += et_wifi_ip.getText().toString();

                                byte[] temp = DataProc.Creat((byte) 0x04, str.getBytes(), str.getBytes().length);
                                tcpclient.socket_send(temp, temp.length);

                                Log.e("DEBUG", "send " + str);

                                dialog.dismiss();
                            }
                        }).create();
                if (Build.VERSION.SDK_INT >= 26) {// 8.0新特性
                    dialog1.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
                } else {
                    dialog1.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                }

                dialog1.show();

                final EditText et_wifi_ssid = (EditText) dialog1.getWindow().findViewById(R.id.et_wifi_ssid);
                final EditText et_wifi_passwd = (EditText) dialog1.getWindow().findViewById(R.id.et_wifi_passwd);
                final EditText et_wifi_ip = (EditText) dialog1.getWindow().findViewById(R.id.et_wifi_ip);

                et_wifi_ssid.setText(EasyTool.getWifiSSID(context));
                et_wifi_ip.setText(EasyTool.getWifiIP(context));
                et_wifi_passwd.setFocusable(true);
                et_wifi_passwd.setFocusableInTouchMode(true);
                et_wifi_passwd.requestFocus();
            }
        });

        final String items[] = { "下载网络升级固件", "选择本地升级固件", "开始升级设备", "检查版本" };
        dialog_upgrade = new AlertDialog.Builder(this)
                // .setIcon(R.mipmap.icon)//设置标题的图片
                .setTitle("选择升级方式")// 设置对话框的标题
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Toast.makeText(MainActivity.this, items[which], Toast.LENGTH_SHORT).show();

                        switch (items[which]) {
                            case "下载网络升级固件":

                                break;
                            case "选择本地升级固件":
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.setType("*/*").addCategory(Intent.CATEGORY_OPENABLE);
                                try {
                                    startActivityForResult(Intent.createChooser(intent, "Choose File"), 0);
                                } catch (ActivityNotFoundException e) {
                                    showToast("亲，木有文件管理器啊-_-!!");
                                }

                                break;
                            case "开始升级设备":

                                break;
                            case "检查版本":

                                break;
                            default:
                                break;
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();

        Button bt_upgrade = (Button) findViewById(R.id.bt_upgrade);
        bt_upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_upgrade.show();
            }
        });

        blue_init();

        mMainHandler = new MainHandler(this);

        tv_blue_status = (TextView) findViewById(R.id.tv_blue_status);
        tv_auto_status = (TextView) findViewById(R.id.tv_auto_status);
        tv_upgrade = (TextView) findViewById(R.id.tv_upgrade);
        tv_serial = (TextView) findViewById(R.id.tv_serial);

        ListView lv_table = (ListView) findViewById(R.id.lv_table);

        mDBManager = new DBManager(this, lv_table, new DBManager.DBManagerCallBack() {
            @Override
            public void on_update_use_table_now(String Name) {

                pubg_now_use = Name;
                // Log.i("DEBUG",keyMouseListUseNow.toString());

                if (pubg_now_use != null) {
                    byte[] temp = mDBManager.GetByteFromPUBG(pubg_now_use);
                    temp = DataProc.Creat((byte) 0x01, temp, temp.length);
                    tcpclient.socket_send(temp, temp.length);
                }
            }
        });

        Intent intent = new Intent(this, FloatService.class);
        intent.putExtra(FloatService.ACTION, FloatService.SHOW);
        intent.putExtra("IsStartUp", "true");
        bindService(intent, mActivityServiceMessage.mServiceConnection, Context.BIND_AUTO_CREATE);

    }

    public void add_plan() {
        Intent intent = new Intent(this, FloatService.class);
        intent.putExtra(FloatService.ACTION, FloatService.SHOW);
        intent.putExtra("IsStartUp", "true");
        bindService(intent, mActivityServiceMessage.mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    char is_init = 1;
    byte test_count2 = 0;

    long testTime, testTimeLast;

    public void blue_init() {
        View blue_scan_view = getLayoutInflater().inflate(R.layout.controller_volume, null);

        bt_device = new BTDevice(this, new BTDevice.bt_callback() {

            @Override
            public void on_start_connect(String blue_name) {
                showToast("正在连接 " + blue_name);
            }

            @Override
            public void on_connect() {
                mActivityServiceMessage.SendToServiceBLUEStatus(bt_device.isConnected());
                // tv_blue_status.setText("蓝牙已连接");
                showToast("蓝牙连接成功");
                // printf("连接成功");
            }

            @Override
            public void on_disconnect() {
                mActivityServiceMessage.SendToServiceBLUEStatus(bt_device.isConnected());
                tv_blue_status.setText("蓝牙未连接");
                showToast("蓝牙已断开");
            }

            @Override
            public void on_receive(byte[] buffer, int len) {
                // showToast("接收到数据");
                // mDataProc.OnBlueReceive(buffer);

                // Log.e(TAG, EasyTool.bytes2hex(buffer, len));
                //
                // if(is_init == 1)
                // {
                // is_init = 0;
                // test_count2 = buffer[len - 1];
                // }else{
                // test_count2++;
                // if(test_count2 != buffer[len - 1])
                // {
                // test_count2 = buffer[len - 1];
                //
                // Log.e(TAG, "=============>ERROR");
                // }
                // }

                tcpclient.socket_send(buffer, len);
                // tcpclient.socket_send_not_creat_task(buffer,buffer.length);

                // long testTime = System.currentTimeMillis(); // 获取开始时间

                // Log.e(TAG,(testTime - testTimeLast) + "ms");
                // testTimeLast = testTime;

            }
        });

        bt_device.enableBluetooth();
        bt_device.init(this, blue_scan_view);
        //
        // if(blue_device.init(this,blue_scan_view))
        // {
        // Log.e("DEBUG","设备初始化成功");
        // }
        //
        // int ret = blue_device.check_blue();
        // if(ret == -2)
        // {
        // Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        // startActivityForResult(enabler, 1);
        // }
    }

    public void blue_start_check_scan() {
        // int ret = blue_device.check_blue();
        int ret = bt_device.check_blue();
        if (ret == -2) {
            showToast("蓝牙打开后请重新扫描");
            Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enabler, 1);

        } else if (ret == -1) {
            showToast("设备不支持蓝牙功能");
        } else if (ret == 0) {

            // blue_device.start_scan();
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

        // Log.e("DEBUG", "We are in onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void showToast(String str) {

        Message msg = new Message();
        msg.what = 0;
        Bundle bundle = new Bundle();
        bundle.putString("Toast", str);
        msg.setData(bundle);
        mMainHandler.sendMessage(msg);

    }

    @Override
    // 文件选择完之后，自动调用此函数
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 0) {
                Uri uri = data.getData();
                final String path = EasyTool.getPath(this, uri);
                upgradeHardware = new UpgradeHardware(this, path, new UpgradeHardware.upgrade_callback() {
                    @Override
                    public void set_status_text(String str) {
                        tv_upgrade.setText(str);
                    }
                });
                Log.i(TAG, path);
                new AlertDialog.Builder(this).setTitle("开始升级？ " + EasyTool.getFileName(path))
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                upgradeHardware.start();

                            }
                        }).setNegativeButton("取消", null).create().show();
            }
        } else {
            Log.e(TAG, "onActivityResult() error, resultCode: " + resultCode);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_setting:
                Intent intent = new Intent(this , SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_description:

                break;
            case R.id.menu_upgrade:

                break;
            case R.id.menu_upgradedevice:

                break;

        }
        return true;
    }

}
