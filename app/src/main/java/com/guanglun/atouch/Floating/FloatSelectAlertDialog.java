package com.guanglun.atouch.Floating;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.WindowManager;

public class FloatSelectAlertDialog {

    private Context context;

    private View select_view;
    private AlertDialog alertDialog;


    public FloatSelectAlertDialog(Context context,View blue_view)
    {
        this.context = context;
        this.select_view = blue_view;



        alertDialog = new AlertDialog.Builder(context)
                //.setTitle("正在扫描蓝牙...")
                //.setIcon(R.mipmap.ic_launcher)
                .setView(select_view)
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramAnonymousDialogInterface,
                                        int paramAnonymousInt) {

                    }
                }).create();

        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
    }

    public void Show()
    {
        alertDialog.show();
    }

    public void Cancel()
    {
        alertDialog.cancel();
    }
}
