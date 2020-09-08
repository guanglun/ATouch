package com.guanglun.atouch.upgrade;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class AppDownloadManager {

    public static final String TAG = "AppDownloadManager";
    private DownloadManager mDownloadManager;
    private long mReqId;
    private Timer timer;
    private TimerTask task;
    private Activity activity;
    private DownloadCallback cb;
    private UpgradeHardware upgradeHardware;

    public AppDownloadManager(Activity activity, DownloadCallback cb) {
        this.activity = activity;
        this.cb = cb;
    }

    public interface DownloadCallback {
        void onDownload(int pro);

        void onSuccess();

        void onFail();

        void onInstallFirmware(String name,String path);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            int pro = bundle.getInt("pro");
            cb.onDownload(pro);
        }
    };

    public static final int DOWNLOAD_APP           = 0;
    public static final int DOWNLOAD_SERVICE       = 1;
    public static final int DOWNLOAD_FIRMWARE      = 2;

    private int downloadKind = DOWNLOAD_APP;
    private String binName = null;

    public void setKind(int kind)
    {
        this.downloadKind = kind;
    }

    public void downloadApk(String apkUrl, String title, String desc) {

        mDownloadManager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkUrl));
        //设置title
        request.setTitle(title);
        // 设置描述
        request.setDescription(desc);
        // 完成后显示通知栏
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //request.setDestinationInExternalFilesDir(activity, Environment.DIRECTORY_DOWNLOADS, "atouch.apk");

        //指定下载到SD卡的/download/my/目录下
        if(downloadKind == DOWNLOAD_APP)
        {
            //Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdir();
            String strs[]  = apkUrl.split("/");
            if(strs.length > 2)
            {
                request.setDestinationInExternalPublicDir("/ATouch/App",strs[strs.length-1]);
            }
        }else if(downloadKind == DOWNLOAD_SERVICE)
        {
            //Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdir();
            String strs[]  = apkUrl.split("/");
            if(strs.length > 2)
            {
                request.setDestinationInExternalPublicDir("/ATouch/Service",strs[strs.length-1]);
            }
        }else if(downloadKind == DOWNLOAD_FIRMWARE)
        {
            //Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdir();
            String strs[]  = apkUrl.split("/");
            if(strs.length > 2)
            {
                binName = strs[strs.length-1];
                Log.i(TAG,strs[strs.length-1]);
                request.setDestinationInExternalPublicDir("/ATouch/Firmware","firmware.apk");
            }
        }

        request.setMimeType("application/vnd.android.package-archive");
        mReqId = mDownloadManager.enqueue(request);

        final DownloadManager.Query query = new DownloadManager.Query();

        timer = new Timer();
        task = new TimerTask() {

            @Override
            public void run() {
                Message msg = Message.obtain();
                Bundle bundle = new Bundle();
                Cursor cursor = mDownloadManager.query(query.setFilterById(mReqId));
                if (cursor != null && cursor.moveToFirst()) {
                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {

                        bundle.putInt("pro", 100);
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                        task.cancel();
                        cb.onSuccess();

                        if(downloadKind != DOWNLOAD_FIRMWARE)
                        {
                            installApk();
                        }else{
                            installFirware();
                        }


                    } else {
                        int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                        int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                        int pro = (bytes_downloaded * 100) / bytes_total;

                        bundle.putInt("pro", pro);
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }
                }
                cursor.close();
            }
        };
        timer.schedule(task, 0, 100);
    }

    private void installApk() {

        DownloadManager dManager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);

        Intent install = new Intent(Intent.ACTION_VIEW);

        install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Uri downloadFileUri = dManager.getUriForDownloadedFile(mReqId);

        install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");

        if (downloadFileUri != null) {
            if ((Build.VERSION.SDK_INT >= 24)) {//判读版本是否在7.0以上
                install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
            }
            activity.startActivity(install);
        }
    }

    private void installFirware() {

        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        renameFile(sdPath+"/ATouch/Firmware/firmware.apk",sdPath+"/ATouch/Firmware/" + binName);
        cb.onInstallFirmware(binName,sdPath+"/ATouch/Firmware/" + binName);

    }
    public void renameFile(String file, String toFile) {

        File toBeRenamed = new File(file);
        //检查要重命名的文件是否存在，是否是文件
        if (!toBeRenamed.exists() || toBeRenamed.isDirectory()) {
            System.out.println("File does not exist: " + file);
            return;
        }
        File newFile = new File(toFile);

        //修改文件名
        if (toBeRenamed.renameTo(newFile)) {
            System.out.println("File has been renamed.");
        } else {
            System.out.println("Error renmaing file");
        }
    }
}
