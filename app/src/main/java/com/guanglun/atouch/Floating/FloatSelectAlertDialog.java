package com.guanglun.atouch.Floating;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

public class FloatSelectAlertDialog {

    private Context context;

    private View select_view;
    private AlertDialog alertDialog;
    private FloatSelectAlertDialogCallBack cb;

    public interface FloatSelectAlertDialogCallBack {
        void NewCreat();
    }

    public FloatSelectAlertDialog(Context context,View blue_view,FloatSelectAlertDialogCallBack cb)
    {
        this.context = context;
        this.select_view = blue_view;
        this.cb = cb;


        alertDialog = new AlertDialog.Builder(context)
                .setView(select_view)
                .setNegativeButton("新建", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Creat();
                    }
                })
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramAnonymousDialogInterface,
                                        int paramAnonymousInt) {

                    }
                }).create();
        if (Build.VERSION.SDK_INT>=26) {//8.0新特性
            alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        }else{
            alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
    }

    public void Show()
    {
        alertDialog.show();
    }

    public void Cancel()
    {
        alertDialog.cancel();
    }

    public void Creat()
    {
        cb.NewCreat();
    }
}
