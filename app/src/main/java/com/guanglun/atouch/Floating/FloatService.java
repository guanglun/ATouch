package com.guanglun.atouch.Floating;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.Surface;
import android.view.WindowManager;

import com.guanglun.atouch.Floating.FloatingView;
import com.guanglun.atouch.Main.ActivityServiceMessage;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

public class FloatService extends Service {

    public static final int MESSAGE_USE_NAME = 1;
    public static final int MESSAGE_ROTATION = 2;

    public static final String ACTION="action";

    public static final String SHOW="show";
    public static final String HIDE="hide";
    private FloatingView mFloatingView;

    private boolean isShow = false;

    private Messenger sMessenger = null,cMessenger = null;

    @Override
    public void onCreate(){
        super.onCreate();

        Log.i("DEBUG","FloatService Run");

        sMessenger = new Messenger(mHandler);
        mFloatingView = new FloatingView(this, new FloatingView.FloatingViewCallBack() {
            @Override
            public void ChoseName(String Name) {
                SendToActivityUseName(Name);

            }
        });

        mFloatingView.setSystemUiVisibility(SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        registerConfigChangeReceiver();
    }



    @Override
    public IBinder onBind(Intent intent){
        return sMessenger.getBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {

            String action = intent.getStringExtra(ACTION);
            String SelectName = intent.getStringExtra("SelectName");
            String IsStartUp = intent.getStringExtra("IsStartUp");
            if(SHOW.equals(action)){

                //if(!isShow)
                {
                    mFloatingView.show(SelectName,IsStartUp);
                    isShow = true;
                }


            }else if(HIDE.equals(action)){
                //if(isShow)
                {

                    mFloatingView.hide();
                    isShow = false;

                }
            }
        }

        return super.onStartCommand(intent, flags, startId);

    }

    // 定义Handler，重载Handler的消息处理方法
    private Handler mHandler = new Handler() {
        // 处理消息内容的具体方法定义
        @Override
        public void handleMessage(Message msg) {
            //Log.i("DEBUG", "----->Service Receive From Activity");
            // 定义反馈给客户端的消息内容
            switch (msg.what) {
                case 0:
                    Log.i("DEBUG", "----->获取Activity Messager成功！");
                    cMessenger = msg.replyTo;
                case ActivityServiceMessage
                        .STATUS_UIAUTO:
                    boolean is_map_connect = msg.getData().getBoolean("STATUS_UIAUTO");
                    mFloatingView.mFloatMenu.mFloatMenuStatus.SetMapStatus(is_map_connect);
                    break;
                case ActivityServiceMessage
                        .STATUS_BLUE:
                    boolean is_blue_connect = msg.getData().getBoolean("STATUS_BLUE");
                    mFloatingView.mFloatMenu.mFloatMenuStatus.SetBLUEStatus(is_blue_connect);
                    break;
                case ActivityServiceMessage
                        .STATUS_KEYBOARD:
                    boolean is_keyboard_connect = msg.getData().getBoolean("STATUS_KEYBOARD");
                    mFloatingView.mFloatMenu.mFloatMenuStatus.SetKeyBoardStatus(is_keyboard_connect);
                    break;
                case ActivityServiceMessage
                        .STATUS_MOUSE:
                    boolean is_mouse_connect = msg.getData().getBoolean("STATUS_MOUSE");
                    mFloatingView.mFloatMenu.mFloatMenuStatus.SetMouseStatus(is_mouse_connect);
                    break;
                case ActivityServiceMessage
                        .STATUS_ADB:
                    boolean is_adb_connect = msg.getData().getBoolean("STATUS_ADB");
                    mFloatingView.mFloatMenu.mFloatMenuStatus.SetADBStatus(is_adb_connect);
                    break;
                case ActivityServiceMessage
                        .STATUS_MOUSE_DATA:
                    int x = msg.getData().getInt("STATUS_MOUSE_DATA_X");
                    int y = msg.getData().getInt("STATUS_MOUSE_DATA_Y");
                    mFloatingView.MouseDataRecv(x,y);
                    break;
                case ActivityServiceMessage
                        .STATUS_MOUSE_SHOW:
                    boolean isshow = msg.getData().getBoolean("STATUS_MOUSE_SHOW");
                    if(isshow)
                    {
                        mFloatingView.mFloatMenu.mFloatMouse.Show();
                    }else{
                        mFloatingView.mFloatMenu.mFloatMouse.Hide();
                    }

                    break;
                case ActivityServiceMessage
                        .STATUS_GET_ROTATION:
                    mFloatingView.mRotationLast = -1;

                    break;

            }
            super.handleMessage(msg);
        }
    };

    private void SendToActivityUseName(String name)
    {
        if(cMessenger != null)
        {
            // 初始化发送给Service的消息，并将cMessenger传递给Service
            Message msg = new Message();
            msg.what = MESSAGE_USE_NAME;
            Bundle bundle = new Bundle();
            bundle.putString("use_name",name);
            msg.setData(bundle);//mes利用Bundle传递数据

            try {
                cMessenger.send(msg);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public void SendToActivityRotation(int ro,int w,int h)
    {
        if(cMessenger != null)
        {
            // 初始化发送给Service的消息，并将cMessenger传递给Service
            Message msg = new Message();
            msg.what = MESSAGE_ROTATION;
            Bundle bundle = new Bundle();
            bundle.putInt("MESSAGE_ROTATION_RO",ro);
            bundle.putInt("MESSAGE_ROTATION_W",w);
            bundle.putInt("MESSAGE_ROTATION_H",h);
            msg.setData(bundle);//mes利用Bundle传递数据

            try {
                cMessenger.send(msg);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    private void registerConfigChangeReceiver(){
        IntentFilter configChangeFilter = new IntentFilter();
        configChangeFilter.addAction(Intent.ACTION_CONFIGURATION_CHANGED);
        configChangeFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(mConfigChangeReceiver, configChangeFilter);
    }

    private BroadcastReceiver mConfigChangeReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            //int angle = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
            mFloatingView.onSreenOrientationConfigChange(0);
        }
    };

}
