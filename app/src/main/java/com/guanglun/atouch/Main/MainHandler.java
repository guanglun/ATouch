package com.guanglun.atouch.Main;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class MainHandler extends Handler {
    WeakReference<MainActivity> mMainActivityWeakReference;

    public MainHandler(MainActivity mainActivity) {
        mMainActivityWeakReference = new WeakReference<>(mainActivity);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if(msg.what == 0)
        {
            Toast.makeText(mMainActivityWeakReference.get(),msg.getData().getString("Toast"),Toast.LENGTH_LONG).show();
            //MainActivity mainActivity = mMainActivityWeakReference.get();
            //mainActivity.adapter.notifyDataSetChanged();
        }

    }
}
