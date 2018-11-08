package com.guanglun.atouch.Floating;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.guanglun.atouch.DBManager.DBControl;
import com.guanglun.atouch.DBManager.DBManager;
import com.guanglun.atouch.DBManager.KeyMouse;

import java.util.ArrayList;
import java.util.List;

public class FloatButtonManager {

    private Context mContext;
    private FloatingManager mFloatingManager;
    private RelativeLayout mRelativeLayout;
    private WindowManager.LayoutParams mParams;
    private int ScreenWidth,ScreenHigh;

    List<FloatButton> FloatButtonList =  new ArrayList<FloatButton>();
    List<KeyMouse> KeyMouseList =  new ArrayList<KeyMouse>();

    public FloatButtonManager(Context context, FloatingManager floatingmanager, RelativeLayout relativeLayout, WindowManager.LayoutParams params)
    {
        mContext = context;
        mFloatingManager = floatingmanager;
        mRelativeLayout = relativeLayout;
        mParams = params;

        WindowManager mWindowManager  = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(metrics);
        ScreenWidth = metrics.widthPixels;//获取到的是px，像素，绝对像素，需要转化为dpi
        ScreenHigh = metrics.heightPixels;
    }

    public void Add(KeyMouse mKeyMouse)
    {
        for(int i=0;i<KeyMouseList.size();i++)
        {
            if(KeyMouseList.get(i).Name.equals(mKeyMouse.Name))
            {
                return;
            }
        }

        FloatButton mFloatButton = new FloatButton(mContext,mFloatingManager,mRelativeLayout,mParams,
                mKeyMouse,ScreenWidth/2,ScreenHigh/2);

        KeyMouseList.add(mKeyMouse);
        FloatButtonList.add(mFloatButton);


    }

    public void RemoveAll()
    {
        for(int i = 0; i < FloatButtonList.size();i++)
        {
            FloatButton fb = FloatButtonList.get(i);
            fb.Remove();
        }

        FloatButtonList.clear();
        KeyMouseList.clear();
    }

    public void Save(String TableName,DBControl mDBControl){

        for(int i = 0;i < KeyMouseList.size();i++)
        {
            FloatButton fb = FloatButtonList.get(i);
            KeyMouse km = KeyMouseList.get(i);
            km.SetPosition(fb.PositionX,fb.PositionY);
            mDBControl.InsertDatabase(TableName,km);
        }

    }

    public void Load(String TableName,DBControl mDBControl){

        KeyMouseList = mDBControl.LoadTableDatabaseList(TableName);

        for(int i = 0;i < KeyMouseList.size();i++)
        {
            KeyMouse km = KeyMouseList.get(i);
            FloatButton mFloatButton = new FloatButton(mContext,mFloatingManager,mRelativeLayout,mParams,
                    km,km.PX,km.PY);
            FloatButtonList.add(mFloatButton);
        }

    }


}
