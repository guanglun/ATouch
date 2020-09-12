package com.guanglun.atouch.Floating;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.audiofx.DynamicsProcessing;
import android.os.Build;
import android.view.WindowManager;

import com.guanglun.atouch.Main.AppConfig;

public class ConfigDialog {
    AppConfig config = new AppConfig();
    ConfigDialogCallback cb;
    public interface ConfigDialogCallback {
        void onOffsetUpdate();
        void onStartCalibr();
    }

    public ConfigDialog(ConfigDialogCallback cb){
        this.cb = cb;
    }

    public void mainView(final Context mContext){

        config.init(mContext);

        final String items[] = {"通用偏移设置"};
        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle("设置菜单")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which)
                        {
                            case 0:
                                final String itemOffset[] = {"方案1","方案2","方案3","方案4","方案5"};
                                AlertDialog dialog2 = new AlertDialog.Builder(mContext).setTitle("选择偏移方案")
                                        .setSingleChoiceItems(itemOffset, config.getOffsetConfig()-1, new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                config.setOffsetConfig(which + 1);
                                                config.store();
                                                dialog.dismiss();
                                                if(cb != null) {
                                                    cb.onOffsetUpdate();
                                                }
                                            }
                                        }).create();
                                if (Build.VERSION.SDK_INT>=26) {//8.0新特性
                                    dialog2.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
                                }else{
                                    dialog2.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                                }

                                dialog2.show();
                                break;
                            case 1:
                                break;
                            default:
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        if (Build.VERSION.SDK_INT>=26) {//8.0新特性
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        }else{
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }

        dialog.show();
    }
}
