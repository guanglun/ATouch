package com.guanglun.atouch.Floating;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.guanglun.atouch.Main.EasyTool;
import com.guanglun.atouch.R;

import static android.support.constraint.Constraints.TAG;

public class FloatMouse {
    public boolean isEnable = false,isShow = false;
    private Context mContext;
    private FloatingManager mFloatingManager;
    private RelativeLayout mRelativeLayout;
    public WindowManager.LayoutParams mParamsMouse;

    public static final float LENGTH = 20*145/200;
    public static final float HIGHT = 20;

    private int mouse_x,mouse_y;
    private int[] offset = new int[2],offset2 = new int[2];
    public int offset_x = 0,offset_y  = 0;
    private boolean is_enable = false;
    private MouseCallback cb;
    private ImageButton mButton;

    public interface MouseCallback {
        void onClick(boolean down);
    }

    public FloatMouse(Context context, FloatingManager mFloatingManager,MouseCallback cb) {

        this.cb = cb;
        mContext = context;
        this.mFloatingManager = mFloatingManager;

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

        mButton = new ImageButton(mContext);


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

        // mParamsMouse.x = mouse_x;
        // mParamsMouse.y = mouse_y;

        mRelativeLayout.setClickable(false);
        //mRelativeLayout.setBackgroundColor(0x00000000);
        mRelativeLayout.setBackgroundColor(0x6000FF00);

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
//        mParamsMouse.width = FrameLayout.LayoutParams.MATCH_PARENT;
//        mParamsMouse.height = FrameLayout.LayoutParams.MATCH_PARENT;
        mRelativeLayout.setVisibility(View.VISIBLE);
        mFloatingManager.updateView(mRelativeLayout, mParamsMouse);

        mRelativeLayout.getLocationOnScreen(offset);
        mRelativeLayout.getLocationInWindow(offset2);

        Log.i("mouse[size]",offset[0]+" "+offset[1]+" "+offset2[0]+" "+offset2[1]);
        Toast.makeText(mContext,offset[0]+" "+offset[1]+" "+offset2[0]+" "+offset2[1],Toast.LENGTH_SHORT).show();
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
            mParamsMouse.x = x-offset[0];
            mParamsMouse.y = y-offset[1];
            mFloatingManager.updateView(mRelativeLayout, mParamsMouse);
//            Log.i(TAG,"x:"+mParamsMouse.x+" y:"+mParamsMouse.y);
//            mButton.setX(x-offset[0]);
//            mButton.setY(y-offset[1]);
        }
    }

    public void SetMouseClick(boolean down)
    {
        cb.onClick(down);
    }
}
