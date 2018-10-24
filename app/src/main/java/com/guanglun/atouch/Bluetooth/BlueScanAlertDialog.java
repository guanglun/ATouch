package com.guanglun.atouch.Bluetooth;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import java.util.Timer;
import java.util.TimerTask;

public class BlueScanAlertDialog {

    private Context context;

    private View blue_view;
    private AlertDialog alertDialog;

    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            //alertDialog.setTitle("正在扫描蓝牙");
        }
    };

    public BlueScanAlertDialog(Context context,View blue_view)
    {
        this.context = context;
        this.blue_view = blue_view;

        alertDialog = new AlertDialog.Builder(context)
                //.setTitle("正在扫描蓝牙...")
                //.setIcon(R.mipmap.ic_launcher)
                .setView(blue_view)
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramAnonymousDialogInterface,
                                        int paramAnonymousInt) {

                    }
                }).create();
    }

    public void scan_start()
    {
        alertDialog.show();
        //Timer timer = new Timer();
        //timer.schedule(timerTask,2000,2000);
    }

    public void scan_cancel()
    {
        alertDialog.cancel();
    }
}
