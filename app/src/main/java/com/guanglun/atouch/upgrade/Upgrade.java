package com.guanglun.atouch.upgrade;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.guanglun.atouch.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Upgrade {


    private static final String TAG = "Upgrade";
    private Context context,upgradeCcontext;
    private long mReqId;
    private AppDownloadManager mDownLoadManager;

    private ProgressDialog getVersionDialog, downDialog,installDialog;
    private AlertDialog dialog_upgrade;
    private UpgradeInfo[] upgradeInfos = {new UpgradeInfo(),new UpgradeInfo(),new UpgradeInfo()};

    private String hardwarePath,hardwareName;

    public Upgrade(Activity activity)
    {
        this.context = activity.getApplicationContext();
        mDownLoadManager = new AppDownloadManager(activity, new AppDownloadManager.DownloadCallback() {
            @Override
            public void onDownload(int pro) {
                downDialog.setProgress(pro);
            }

            @Override
            public void onSuccess() {
                downDialog.cancel();
            }

            @Override
            public void onFail() {

            }

            @Override
            public void onInstallFirmware(String name, String path) {
                hardwareName = name;
                hardwarePath = path;
                Message msg = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putInt("id", 1);
                msg.setData(bundle);
                waitInfoHandler.sendMessage(msg);
            }
        });
    }

    private Handler waitInfoHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            int id = bundle.getInt("id");
            if(id == 0)
            {
                getVersionDialog.cancel();
                showAlertDialog(upgradeCcontext);
            }else if(id == 1)
            {
                installHardware(upgradeCcontext,hardwareName,hardwarePath);
            }

        }
    };


    class UpgradeInfo{
        public Float remote_version;
        public Float local_version;
        public String updateurl;

        public UpgradeInfo()
        {
            remote_version = 0f;
            local_version = 0f;
            updateurl = null;
        }
    }

    private Runnable checkUpgradeRunnable = new Runnable() {
        @Override
        public void run() {
            JSONObject jsonObject;
            String json = "";

            String VersionName = getLocalVersionName(context);
            if(VersionName == null)
            {
                upgradeInfos[0].local_version = 0f;
            }else{
                upgradeInfos[0].local_version = Float.parseFloat(VersionName);
            }

            VersionName = getServiceVersionName(context);
            if(VersionName == null)
            {
                upgradeInfos[1].local_version = 0f;
            }else{
                upgradeInfos[1].local_version = Float.parseFloat(VersionName);
            }

            try {

                URL url = new URL("http://guanglundz.com/atouch/upgrade/upgrade.json");
                InputStream is = url.openStream();
                InputStreamReader isr = new InputStreamReader(is,"utf-8");

                BufferedReader bufferedReader=new BufferedReader(isr);
                String line;
                while ((line = bufferedReader.readLine()) != null){
                    json += line;
                }
                bufferedReader.close();
                isr.close();
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(json.length() > 0)
            {
                try {
                    String version,updateurl;
                    jsonObject = new JSONObject(json);
                    Log.i(TAG,jsonObject.toString());

                    JSONObject jsonObject2 = jsonObject.getJSONObject("app");
                    if(jsonObject2 != null) {
                        version = jsonObject2.getString("version");
                        updateurl = jsonObject2.getString("updateurl");

                        upgradeInfos[0].updateurl = updateurl;

                        if(version == null)
                        {
                            upgradeInfos[0].remote_version = 0f;
                        }else{
                            upgradeInfos[0].remote_version = Float.parseFloat(version);
                        }

                    }else{
                        upgradeInfos[0].updateurl = null;
                        upgradeInfos[0].remote_version = 0f;
                    }

                    jsonObject2 = jsonObject.getJSONObject("service");
                    if(jsonObject2 != null) {
                        version = jsonObject2.getString("version");
                        updateurl = jsonObject2.getString("updateurl");

                        upgradeInfos[1].updateurl = updateurl;

                        if(version == null)
                        {
                            upgradeInfos[1].remote_version = 0f;
                        }else{
                            upgradeInfos[1].remote_version = Float.parseFloat(version);
                        }

                    }else{
                        upgradeInfos[1].updateurl = null;
                        upgradeInfos[1].remote_version = 0f;
                    }

                    jsonObject2 = jsonObject.getJSONObject("firmware");
                    if(jsonObject2 != null) {
                        version = jsonObject2.getString("version");
                        updateurl = jsonObject2.getString("updateurl");

                        upgradeInfos[2].updateurl = updateurl;

                        if(version == null)
                        {
                            upgradeInfos[2].remote_version = 0f;
                        }else{
                            upgradeInfos[2].remote_version = Float.parseFloat(version);
                        }

                        upgradeInfos[2].local_version = 0f;
                    }else{
                        upgradeInfos[2].updateurl = null;
                        upgradeInfos[2].remote_version = 0f;
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

            Message msg = Message.obtain();
            Bundle bundle = new Bundle();
            bundle.putInt("id", 0);
            msg.setData(bundle);
            waitInfoHandler.sendMessage(msg);
        }
    };

    public void checkUpgrade()
    {
        for(UpgradeInfo info:upgradeInfos)
        {
            info.local_version=0f;
            info.remote_version=0f;
            info.updateurl=null;
        }
        Thread upgradeThread = new Thread(checkUpgradeRunnable);
        upgradeThread.start();
    }



    public static String getLocalVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    public String getServiceVersionName(Context context) {

        PackageManager pckMan = context.getPackageManager();
        ArrayList<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();

        List<PackageInfo> packageInfo = pckMan.getInstalledPackages(0);

        for (PackageInfo pInfo : packageInfo) {

            if(pInfo.packageName.equals("com.guanglun.service") && pInfo.applicationInfo.loadLabel(pckMan).toString().equals("ATouchService"))
            {
                //Log.i("TAG",pInfo.toString());
                return pInfo.versionName;
            }
        }

        return null;
    }

    public void showGetVersionDialog(Context context)
    {
        upgradeCcontext = context;
        getVersionDialog = new ProgressDialog(context);
        getVersionDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
        getVersionDialog.setCancelable(false);// 设置是否可以通过点击Back键取消
        getVersionDialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        //getVersionDialog.setIcon(R.mipmap.ic_launcher);//设置提示的title的图标，默认是没有的，如果没有设置title的话只设置Icon是不会显示图标的
        getVersionDialog.setTitle("正在获取版本信息");
        getVersionDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                });
        getVersionDialog.show();
        checkUpgrade();

    };

    private void showDownDialog(Context context, final String Url, final String title, final String desc)
    {
        downDialog = new ProgressDialog(context);
        downDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);  // 设置水平进度条
        downDialog.setCancelable(false);                                // 设置是否可以通过点击Back键取消
        downDialog.setCanceledOnTouchOutside(false);                   // 设置在点击Dialog外是否取消Dialog进度条
//        alertDialog.setIcon(R.mipmap.ic_launcher);                    // 设置提示的title的图标，默认是没有的
        downDialog.setTitle(title);
        downDialog.setMax(100);
        downDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                });
        downDialog.show();
        mDownLoadManager.downloadApk(Url,title,desc);
    }

    public void showAlertDialog(final Context context) {

        View view = View.inflate(context, R.layout.update_dialog, null);

        Button bt_app = ((Button)view.findViewById(R.id.bt_app_upgrade));
        Button bt_service = ((Button)view.findViewById(R.id.bt_service_upgrade));
        Button bt_firmware = ((Button)view.findViewById(R.id.bt_firmware_upgrade));


        if(upgradeInfos[0].local_version != 0f)
        {
            ((TextView)view.findViewById(R.id.tv_app_local_version)).setText("V"+String.valueOf(upgradeInfos[0].local_version));
        }else{
            ((TextView)view.findViewById(R.id.tv_app_local_version)).setText("失败");
        }

        if(upgradeInfos[0].remote_version != 0f)
        {
            ((TextView)view.findViewById(R.id.tv_app_remote_version)).setText("V"+String.valueOf(upgradeInfos[0].remote_version));
        }else{
            ((TextView)view.findViewById(R.id.tv_app_remote_version)).setText("失败");
            bt_app.setVisibility(View.GONE);
        }

        if(upgradeInfos[1].local_version != 0f)
        {
            ((TextView)view.findViewById(R.id.tv_service_local_version)).setText("V"+String.valueOf(upgradeInfos[1].local_version));
        }else{
            ((TextView)view.findViewById(R.id.tv_service_local_version)).setText("失败");
        }

        if(upgradeInfos[1].remote_version != 0f)
        {
            ((TextView)view.findViewById(R.id.tv_service_remote_version)).setText("V"+String.valueOf(upgradeInfos[1].remote_version));
        }else{
            ((TextView)view.findViewById(R.id.tv_service_remote_version)).setText("失败");
            bt_service.setVisibility(View.GONE);
        }

        if(upgradeInfos[2].local_version != 0f)
        {
            ((TextView)view.findViewById(R.id.tv_firmware_local_version)).setText("V"+String.valueOf(upgradeInfos[2].local_version));
        }else{
            ((TextView)view.findViewById(R.id.tv_firmware_local_version)).setText("失败");
        }

        if(upgradeInfos[2].remote_version != 0f)
        {
            ((TextView)view.findViewById(R.id.tv_firmware_remote_version)).setText("V"+String.valueOf(upgradeInfos[2].remote_version));
        }else{
            ((TextView)view.findViewById(R.id.tv_firmware_remote_version)).setText("失败");
            bt_firmware.setVisibility(View.GONE);
        }

        bt_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_upgrade.cancel();
                mDownLoadManager.setKind(AppDownloadManager.DOWNLOAD_APP);
                showDownDialog(context,upgradeInfos[0].updateurl,"ATouch 下载","ATouch APP 下载");
            }
        });

        bt_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_upgrade.cancel();
                mDownLoadManager.setKind(AppDownloadManager.DOWNLOAD_SERVICE);
                showDownDialog(context,upgradeInfos[1].updateurl,"ATouch Service 下载","ATouch Service 下载");
            }
        });

        bt_firmware.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_upgrade.cancel();
                mDownLoadManager.setKind(AppDownloadManager.DOWNLOAD_FIRMWARE);
                showDownDialog(context,upgradeInfos[2].updateurl,"ATouch Firmware 下载","ATouch Firmware 下载");
            }
        });

        dialog_upgrade = new AlertDialog.Builder(context)
                .setTitle("选择升级方式")
                .setView(view)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog_upgrade.setCancelable(false);                                        // 设置是否可以通过点击Back键取消
        dialog_upgrade.show();
    }

    public void installHardware(final Context context,final String name, final String path)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("固件升级（请连接ATouch热点！）");
        builder.setMessage("警告：点击确定前请务必切换WIFI连接至ATouch_XXXXXX，否则将升级失败！!!\r\n\n是否升级固件？\r\n\n" + name);
        builder.setCancelable(false);
        builder.setPositiveButton("开始升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                UpgradeHardware upgradeHardware = new UpgradeHardware(context, path, new UpgradeHardware.upgrade_callback() {
                    @Override
                    public void set_status_text(String str) {
                        installDialog.setMessage(str);
                    }

                    @Override
                    public void onConnect() {

                    }

                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onProcess(int pro) {
                        installDialog.setProgress(pro);
                    }
                });
                dialog.dismiss();
                showInstallDialog(context);
                upgradeHardware.start();
            }

        });
        //设置反面按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void showInstallDialog(Context context)
    {
        installDialog = new ProgressDialog(context);
        installDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);  // 设置水平进度条
        installDialog.setCancelable(false);                                // 设置是否可以通过点击Back键取消
        installDialog.setCanceledOnTouchOutside(false);                   // 设置在点击Dialog外是否取消Dialog进度条
//        alertDialog.setIcon(R.mipmap.ic_launcher);                      // 设置提示的title的图标，默认是没有的
        installDialog.setTitle("固件升级");
        installDialog.setMessage("正在连接");
        installDialog.setMax(100);
        installDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        installDialog.cancel();
                    }
                });
        installDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        installDialog.cancel();
                    }
                });
        installDialog.show();
    }
}
