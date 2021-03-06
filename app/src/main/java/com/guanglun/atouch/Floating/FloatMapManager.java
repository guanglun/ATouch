package com.guanglun.atouch.Floating;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.guanglun.atouch.DBManager.MapUnit;

import java.util.ArrayList;
import java.util.List;

public class FloatMapManager {

    private Context mContext;
    private RelativeLayout mRelativeLayout;
    private WindowManager.LayoutParams mParams;

    public boolean isShow = false;

    private final String DEBUG_TAG = "FloatMapManager";


    FloatButtonPUBG bt_calibr;

    public String SelectName = null;
    public List<String> DBNameList = null;
    public boolean isShowEditWindow = false;
    public List<MapUnit> maplist = null;
    private FloatMenu mFloatMenu;

    public int[] offset = new int[2];
    public FloatMapManager(Context context, FloatMenu mFloatMenu, RelativeLayout relativeLayout, WindowManager.LayoutParams params)
    {
        mContext = context;
        this.mFloatMenu = mFloatMenu;
        mRelativeLayout = relativeLayout;
        mParams = params;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                reloadOffset();
            }
        }).start();
    }

    public void reloadOffset()
    {
        mRelativeLayout.getLocationOnScreen(offset);
        Log.i("button offset",offset[0] + " " + offset[1]);
    }

    public void ShowWindow()
    {
        mParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
        mParams.height = FrameLayout.LayoutParams.MATCH_PARENT;
        mRelativeLayout.setVisibility(View.VISIBLE);
        mFloatMenu.mFloatingManager.updateView(mRelativeLayout, mParams);
        isShowEditWindow = true;
    }

    public void HideWindow()
    {
        mParams.width = 0;
        mParams.height = 0;
        mRelativeLayout.setVisibility(View.INVISIBLE);
        mFloatMenu.mFloatingManager.updateView(mRelativeLayout, mParams);
        isShowEditWindow = false;
    }

    public void removeAll()
    {
        if(maplist != null)
        {
            for(MapUnit map:maplist)
            {
                if(map.MFV == MapUnit.MFV_NORMAL)
                {
                    map.bt.Remove();
                }else if(map.MFV == MapUnit.MFV_PUBG){
                    map.bts.Remove();
                }else if(map.MFV == MapUnit.MFV_MOUSE){
                    map.btm.Remove();
                }else if(map.MFV == MapUnit.MFV_ROCKER){
                    map.btr.Remove();
                }else if(map.MFV == MapUnit.MFV_ATOUCH){
                    map.bta.Remove();
                }
            }
            maplist.clear();
        }
        maplist = null;
    }

    public void showAll(String name)
    {
        maplist = mFloatMenu.dbManager.dbControl.getRawByName(name);
        for(MapUnit map:maplist)
        {
            if(map.MFV == MapUnit.MFV_NORMAL)
            {
                map.bt =  new FloatButtonMap(mContext,mFloatMenu,map);
            }else if(map.MFV == MapUnit.MFV_PUBG){
                map.bts =  new FloatButtonMapSlide(mContext,mFloatMenu,map);
            }else if(map.MFV == MapUnit.MFV_MOUSE){
                map.btm =  new FloatButtonMapMouse(mContext,mFloatMenu,map);
            }else if(map.MFV == MapUnit.MFV_ROCKER){
                map.btr =  new FloatButtonMapRocker(mContext,mFloatMenu,map);
            }else if(map.MFV == MapUnit.MFV_ATOUCH){
                map.bta =  new FloatButtonMapATouch(mContext,mFloatMenu,map);
            }

        }
    }

    public void creatNew()
    {
        if(maplist == null)
            maplist = new ArrayList<MapUnit>();

        final String items[] = {"普通按键","移动滑盘","鼠标滑动","摇杆滑动","ATouch体感"};
        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle("选择添加的类型")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MapUnit map;
                        switch (which)
                        {
                            case 0:
                                map = new MapUnit();
                                if(maplist.size() > 0) {
                                    map.Name = maplist.get(0).Name;
                                }
                                map.PX = 400;
                                map.PY = 400;
                                map.KeyName = "";
                                map.MFV = MapUnit.MFV_NORMAL;
                                map.bt =  new FloatButtonMap(mContext,mFloatMenu,map);
                                mFloatMenu.dbManager.showMapAdapterView(map);
                                maplist.add(map);
                                break;
                            case 1:
                                map = new MapUnit();
                                if(maplist.size() > 0) {
                                    map.Name = maplist.get(0).Name;
                                }
                                map.PX = 400;
                                map.PY = 400;
                                map.FV0 = 500;
                                map.FV1 = 400;
                                map.KeyName = "";
                                map.MFV = MapUnit.MFV_PUBG;
                                map.bts =  new FloatButtonMapSlide(mContext,mFloatMenu,map);
                                mFloatMenu.dbManager.showMapSlideAdapterView(map);
                                maplist.add(map);
                                break;
                            case 2:
                                map = new MapUnit();
                                if(maplist.size() > 0) {
                                    map.Name = maplist.get(0).Name;
                                }
                                map.PX = 400;
                                map.PY = 400;
                                map.FV1 = 500;
                                map.FV2 = 400;
                                map.KeyName = "";
                                map.MFV = MapUnit.MFV_MOUSE;
                                map.btm =  new FloatButtonMapMouse(mContext,mFloatMenu,map);
                                mFloatMenu.dbManager.showMapMouseAdapterView(map);
                                maplist.add(map);
                                break;
                            case 3:
                                map = new MapUnit();
                                if(maplist.size() > 0) {
                                    map.Name = maplist.get(0).Name;
                                }
                                map.PX = 400;
                                map.PY = 400;
                                map.FV1 = 500;
                                map.FV2 = 400;
                                map.KeyName = "";
                                map.MFV = MapUnit.MFV_ROCKER;
                                map.btr =  new FloatButtonMapRocker(mContext,mFloatMenu,map);
                                mFloatMenu.dbManager.showMapRockerAdapterView(map);
                                maplist.add(map);
                                break;
                            case 4:
                                map = new MapUnit();
                                if(maplist.size() > 0) {
                                    map.Name = maplist.get(0).Name;
                                }
                                map.PX = 400;
                                map.PY = 400;
                                map.FV1 = 500;
                                map.FV2 = 400;
                                map.FV3 = 30;
                                map.FV4 = 30;
                                map.KeyName = "";
                                map.MFV = MapUnit.MFV_ATOUCH;
                                map.bta =  new FloatButtonMapATouch(mContext,mFloatMenu,map);
                                mFloatMenu.dbManager.showMapATouchAdapterView(map);
                                maplist.add(map);
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

    public void SaveMap() {
        if(!mFloatMenu.mFloatMapManager.isShowEditWindow)
            return;

        if(maplist.size() == 0) {
            Toast.makeText(mContext,"映射为空，无法保存",Toast.LENGTH_SHORT).show();
            return;
        }

        if(maplist.get(0).Name.length() == 0)
        {
            final EditText editText = new EditText(mContext);

            AlertDialog dialog = new AlertDialog.Builder(mContext)
                    .setTitle("请输入新的映射名称")
                    .setView(editText)
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String NewName = editText.getText().toString();
                            if(NewName.length() > 0)
                            {
                                if(mFloatMenu.dbManager.dbControl.getRawByName(NewName) != null)
                                {
                                    Toast.makeText(mContext,"保存失败，映射名称已存在",Toast.LENGTH_SHORT).show();
                                }else{
                                    for(MapUnit map:maplist)
                                    {
                                        map.Name = editText.getText().toString();
                                    }
                                    mFloatMenu.dbManager.dbControl.insertList(maplist);

                                    Toast.makeText(mContext,"保存成功",Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(mContext,"映射名称不能为空",Toast.LENGTH_SHORT).show();

                            }
                            dialog.dismiss();
                        }
                    }).create();
            if (Build.VERSION.SDK_INT>=26) {//8.0新特性
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
            }else{
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            }
            dialog.show();
        }else{
            AlertDialog dialog = new AlertDialog.Builder(mContext)

                    .setTitle("确认保存？")//设置对话框的标题
                    .setNeutralButton("保存并使用", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mFloatMenu.dbManager.dbControl.deleteName(maplist.get(0).Name);
                            mFloatMenu.dbManager.dbControl.insertList(maplist);
                            mFloatMenu.cb.ChoseName(maplist.get(0).Name);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mFloatMenu.dbManager.dbControl.deleteName(maplist.get(0).Name);
                            mFloatMenu.dbManager.dbControl.insertList(maplist);
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

}
