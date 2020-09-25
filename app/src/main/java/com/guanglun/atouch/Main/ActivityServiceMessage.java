package com.guanglun.atouch.Main;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.guanglun.atouch.Floating.FloatService;
import com.guanglun.atouch.Touch.DataProc;

import java.util.Timer;
import java.util.TimerTask;

public class ActivityServiceMessage {

    public static final int STATUS_UIAUTO   = 1;
    public static final int STATUS_ADB      = 2;
    public static final int STATUS_BLUE     = 3;
    public static final int STATUS_KEYBOARD = 4;
    public static final int STATUS_MOUSE    = 5;
    public static final int STATUS_MOUSE_DATA    = 6;
    public static final int STATUS_MOUSE_CLICK    = 7;
    public static final int STATUS_MOUSE_SHOW    = 8;
    public static final int STATUS_GET_ROTATION    = 9;
    

    private Messenger sMessenger;

    private MessengerCallback cb;

    public boolean mUiautoStatus = false;



    public interface MessengerCallback {
        void on_use(String name);
        void on_rotation(int ro,int w,int h);
    }

    public ActivityServiceMessage(MessengerCallback cb)
    {
        this.cb = cb;

    }

    private Messenger cMessenger = new Messenger(new Handler() {
        // 获取Service发送过来的消息进行处理
        @Override
        public void handleMessage(Message msg) {
            Log.i("DEBUG", "----->Activity Receive From Service");
            switch (msg.what) {
                case FloatService
                        .MESSAGE_USE_NAME:
                    cb.on_use(msg.getData().getString("use_name"));

                    break;
                case FloatService
                        .MESSAGE_ROTATION:
                    cb.on_rotation(msg.getData().getInt("MESSAGE_ROTATION_RO"),
                            msg.getData().getInt("MESSAGE_ROTATION_W"),
                            msg.getData().getInt("MESSAGE_ROTATION_H"));

                    break;
            }
        }
    });

    //定义Connection，作为启动Services的参数
    public ServiceConnection mServiceConnection = new ServiceConnection(){
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            sMessenger = new Messenger(service);

            // 初始化发送给Service的消息，并将cMessenger传递给Service
            Message msg = Message.obtain();
            msg.replyTo = cMessenger;
            msg.what = 0;
            try {
                sMessenger.send(msg);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            SendToServiceUiautoStatus(mUiautoStatus);
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            sMessenger = null;
        }
    };

    public void SendToServiceUiautoStatus(boolean value)
    {
        if(sMessenger != null)
        {
            // 初始化发送给Service的消息，并将cMessenger传递给Service
            Message msg = new Message();
            msg.what = STATUS_UIAUTO;
            Bundle bundle = new Bundle();
            bundle.putBoolean("STATUS_UIAUTO",value);
            msg.setData(bundle);//mes利用Bundle传递数据

            try {
                sMessenger.send(msg);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void SendToServiceBLUEStatus(boolean value)
    {
        if(sMessenger != null)
        {
            // 初始化发送给Service的消息，并将cMessenger传递给Service
            Message msg = new Message();
            msg.what = STATUS_BLUE;
            Bundle bundle = new Bundle();
            bundle.putBoolean("STATUS_BLUE",value);
            msg.setData(bundle);//mes利用Bundle传递数据

            try {
                sMessenger.send(msg);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void SendToServiceADBStatus(boolean value)
    {
        if(sMessenger != null)
        {
            // 初始化发送给Service的消息，并将cMessenger传递给Service
            Message msg = new Message();
            msg.what = STATUS_ADB;
            Bundle bundle = new Bundle();
            bundle.putBoolean("STATUS_ADB",value);
            msg.setData(bundle);//mes利用Bundle传递数据

            try {
                sMessenger.send(msg);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public void SendToServiceKeyBoardStatus(boolean value)
    {
        if(sMessenger != null)
        {
            // 初始化发送给Service的消息，并将cMessenger传递给Service
            Message msg = new Message();
            msg.what = STATUS_KEYBOARD;
            Bundle bundle = new Bundle();
            bundle.putBoolean("STATUS_KEYBOARD",value);
            msg.setData(bundle);//mes利用Bundle传递数据

            try {
                sMessenger.send(msg);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public void SendToServiceMouseStatus(boolean value)
    {
        if(sMessenger != null)
        {
            // 初始化发送给Service的消息，并将cMessenger传递给Service
            Message msg = new Message();
            msg.what = STATUS_MOUSE;
            Bundle bundle = new Bundle();
            bundle.putBoolean("STATUS_MOUSE",value);
            msg.setData(bundle);//mes利用Bundle传递数据

            try {
                sMessenger.send(msg);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public void SendToServiceMouseData(int x,int y)
    {
        if(sMessenger != null)
        {
            // 初始化发送给Service的消息，并将cMessenger传递给Service
            Message msg = new Message();
            msg.what = STATUS_MOUSE_DATA;
            Bundle bundle = new Bundle();
            bundle.putInt("STATUS_MOUSE_DATA_X",x);
            bundle.putInt("STATUS_MOUSE_DATA_Y",y);
            msg.setData(bundle);//mes利用Bundle传递数据

            try {
                sMessenger.send(msg);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void SendToServiceMouseClick(boolean down)
    {
        if(sMessenger != null)
        {
            // 初始化发送给Service的消息，并将cMessenger传递给Service
            Message msg = new Message();
            msg.what = STATUS_MOUSE_CLICK;
            Bundle bundle = new Bundle();
            bundle.putBoolean("STATUS_MOUSE_CLICK",down);
            msg.setData(bundle);//mes利用Bundle传递数据

            try {
                sMessenger.send(msg);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void SendToServiceMouseIsShow(boolean isshow)
    {
        if(sMessenger != null)
        {
            // 初始化发送给Service的消息，并将cMessenger传递给Service
            Message msg = new Message();
            msg.what = STATUS_MOUSE_SHOW;
            Bundle bundle = new Bundle();
            bundle.putBoolean("STATUS_MOUSE_SHOW",isshow);
            msg.setData(bundle);//mes利用Bundle传递数据

            try {
                sMessenger.send(msg);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public void SendToServiceGetRotation()
    {
        if(sMessenger != null)
        {
            // 初始化发送给Service的消息，并将cMessenger传递给Service
            Message msg = new Message();
            msg.what = STATUS_GET_ROTATION;
            Bundle bundle = new Bundle();
            msg.setData(bundle);//mes利用Bundle传递数据

            try {
                sMessenger.send(msg);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
}
