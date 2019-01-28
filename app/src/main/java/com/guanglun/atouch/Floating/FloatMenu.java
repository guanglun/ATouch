package com.guanglun.atouch.Floating;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.guanglun.atouch.Main.EasyTool;
import com.guanglun.atouch.R;

public class FloatMenu {

    private Button button;
    private Context mContext;
    private FloatingManager mFloatingManager;
    private RelativeLayout mRelativeLayout;
    private WindowManager.LayoutParams mParams;
    private final int ROUNDD = 50;
    private int StartX = 0,StartY = 0;
    private FloatMenuButton fmb_menu,fmb1,fmb2,fmb3;

    @SuppressLint("ResourceType")
    public FloatMenu(Context context, FloatingManager floatingmanager, RelativeLayout relativeLayout, WindowManager.LayoutParams params)
    {
        mContext = context;
        mFloatingManager = floatingmanager;
        mRelativeLayout = relativeLayout;
        mParams = params;

        mParams = new WindowManager.LayoutParams();
        mParams.gravity = Gravity.LEFT|Gravity.TOP;

        mParams.x = (StartX);
        mParams.y = (StartY + EasyTool.getStatusBarHeight(mContext));

        //总是出现在应用程序窗口之上
        mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        //设置图片格式，效果为背景透明
        mParams.format = PixelFormat.RGBA_8888;
        mParams.flags =  WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        mParams.width = FrameLayout.LayoutParams.WRAP_CONTENT;//MATCH_PARENT;
        mParams.height = FrameLayout.LayoutParams.WRAP_CONTENT;//MATCH_PARENT;

        //mParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
        //mParams.height = FrameLayout.LayoutParams.MATCH_PARENT;

        mRelativeLayout.setBackgroundColor(0x60ebebeb);


        fmb_menu = new FloatMenuButton(mContext,mRelativeLayout,FloatMenuButton.MENU_MAIN_BUTTON,1);
        fmb_menu.SetOnTouchListener(mOnTouchListener);

        mFloatingManager.addView(mRelativeLayout, mParams);
    }

    private int temp_x = 0,temp_y = 0;
    boolean isMoveTouch = false;

    View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    temp_x = (int)event.getRawX();
                    temp_y = (int)event.getRawY();
                    isMoveTouch = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                        if(!isMoveTouch)
                        {
                            if((int)event.getRawX() != temp_x || (int)event.getRawY() != temp_y)
                            {
                                isMoveTouch = true;
                                mParams.x += ((int)event.getRawX() - temp_x);
                                mParams.y += ((int)event.getRawY() - temp_y);
                                temp_x = (int)event.getRawX();
                                temp_y = (int)event.getRawY();
                                mFloatingManager.updateView(mRelativeLayout, mParams);
                            }
                        }else{
                            mParams.x += ((int)event.getRawX() - temp_x);
                            mParams.y += ((int)event.getRawY() - temp_y);
                            temp_x = (int)event.getRawX();
                            temp_y = (int)event.getRawY();
                            mFloatingManager.updateView(mRelativeLayout, mParams);
                        }

                    break;
                case MotionEvent.ACTION_UP:
                    if(!isMoveTouch)
                    {
                        MenuClick();
                    }
                    break;
            }



            Log.i("DEBUG","touch "+(int)event.getRawX()+" "+(int)event.getRawY());
            return false;
        }
    };

    private boolean isMenuClick = true;
    private void MenuClick()
    {
        if(isMenuClick)
        {
            isMenuClick = false;

            fmb1 = new FloatMenuButton(mContext,mRelativeLayout,FloatMenuButton.MENU_RIGHT_BUTTON,fmb_menu.id);
            fmb2 = new FloatMenuButton(mContext,mRelativeLayout,FloatMenuButton.MENU_RIGHT_BUTTON,fmb1.id);
            fmb3 = new FloatMenuButton(mContext,mRelativeLayout,FloatMenuButton.MENU_RIGHT_BUTTON,fmb2.id);



            mFloatingManager.updateView(mRelativeLayout, mParams);





        }else{
            isMenuClick = true;

            fmb1.Remove();
            fmb2.Remove();
            fmb3.Remove();
            mFloatingManager.updateView(mRelativeLayout, mParams);
        }
    }


}
