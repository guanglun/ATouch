package com.guanglun.atouch.Floating;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.guanglun.atouch.Main.EasyTool;
import com.guanglun.atouch.R;

public class FloatMouse {
    public boolean isEnable = false,isShow = false;
    private Context mContext;
    private FloatingManager mFloatingManager;
    private RelativeLayout mRelativeLayout;
    private WindowManager.LayoutParams mParamsMouse;

    public static final float LENGTH = 20*145/200;
    public static final float HIGHT = 20;

    private int mouse_x,mouse_y;
    private boolean is_enable = false;

    public FloatMouse(Context context, FloatingManager floatingmanager) {

        mContext = context;
        mFloatingManager = floatingmanager;

        mRelativeLayout = new RelativeLayout(mContext);
        mParamsMouse = new WindowManager.LayoutParams();

        mouse_x = EasyTool.getScreenWidth(mContext)/2;
        mouse_y = EasyTool.getScreenHeight(mContext)/2;

        RelativeLayout.LayoutParams mLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);

        mLayoutParams.width = EasyTool.dip2px(mContext,LENGTH);
        mLayoutParams.height  = EasyTool.dip2px(mContext,HIGHT);


        mLayoutParams.setMargins(0,0,0,0);
        mLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
        mLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);

        ImageButton mButton = new ImageButton(mContext);


        mButton.setPadding(0,0,0,0);
        mButton.setLayoutParams(mLayoutParams);   ////设置按钮的布局属性

        mButton.setScaleType(ImageButton.ScaleType.FIT_XY);
        mButton.setImageDrawable(mContext.getResources().getDrawable(R.drawable.float_mouse));
        mButton.setBackgroundColor(0x00000000);
        mButton.setId(View.generateViewId());
        mButton.setClickable(false);

        mParamsMouse.gravity = Gravity.LEFT|Gravity.TOP;
        //总是出现在应用程序窗口之上
        if (Build.VERSION.SDK_INT>=26) {//8.0新特性
            mParamsMouse.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else{
            mParamsMouse.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }

        //设置图片格式，效果为背景透明
        mParamsMouse.format = PixelFormat.RGBA_8888;
        mParamsMouse.flags =  WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;

        /*
        * 初始化不显示
        * */
        mParamsMouse.width = 0;
        mParamsMouse.height = 0;
        mRelativeLayout.setVisibility(View.INVISIBLE);

//        mParamsMouse.width = FrameLayout.LayoutParams.WRAP_CONTENT;
//        mParamsMouse.height = FrameLayout.LayoutParams.WRAP_CONTENT;
//        mRelativeLayout.setVisibility(View.VISIBLE);

        mParamsMouse.x = mouse_x;
        mParamsMouse.y = mouse_y;

        mRelativeLayout.setClickable(false);
        mRelativeLayout.setBackgroundColor(0x00000000);
        //mRelativeLayout.setBackgroundColor(0x6000FF00);

        mRelativeLayout.addView(mButton);
        mFloatingManager.addView(mRelativeLayout, mParamsMouse);
    }




    public void Show()
    {
        if(!is_enable)
        {
            is_enable = true;
        }
        mParamsMouse.width = FrameLayout.LayoutParams.WRAP_CONTENT;
        mParamsMouse.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        mRelativeLayout.setVisibility(View.VISIBLE);
        mFloatingManager.updateView(mRelativeLayout, mParamsMouse);
    }

    public void Hide()
    {
        if(is_enable)
        {
            mParamsMouse.width = 0;
            mParamsMouse.height = 0;
            mRelativeLayout.setVisibility(View.INVISIBLE);
            mFloatingManager.updateView(mRelativeLayout, mParamsMouse);
        }

    }

    public void SetMouse(int x,int y)
    {
        if(is_enable) {
            mParamsMouse.x = x;
            mParamsMouse.y = y;
            mFloatingManager.updateView(mRelativeLayout, mParamsMouse);
        }
    }
}
