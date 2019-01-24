package com.guanglun.atouch.Floating;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.guanglun.atouch.Floating.FloatingView;

public class FloatService extends Service {
    public static final String ACTION="action";

    public static final String SHOW="show";
    public static final String HIDE="hide";
    private FloatingView mFloatingView;

    private boolean isShow = false;
    @Override
    public void onCreate(){
        super.onCreate();

        mFloatingView = new FloatingView(this, new FloatingView.FloatingViewCallBack() {
            @Override
            public void ChoseName(String Name) {
                SendBroadcast(Name);
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
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

    public void SendBroadcast(String str)
    {
        Intent mintent = new Intent("com.guanglun.atouch.RECEIVER");
        mintent.putExtra("Name", str);
        sendBroadcast(mintent);
    }


}
