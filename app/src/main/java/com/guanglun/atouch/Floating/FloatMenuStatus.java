package com.guanglun.atouch.Floating;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.guanglun.atouch.Main.EasyTool;
import com.guanglun.atouch.R;

public class FloatMenuStatus {

    private static final int ROUNDD = 14;
    private Context mContext;
    private FloatingManager mFloatingManager;
    private RelativeLayout mRelativeLayout;
    private RelativeLayout mRelativeLayoutStatus;
    private ImageButton ib_keyboard,ib_mouse,ib_adb,ib_blue,ib_map;
    public int id ;

    public FloatMenuStatus(Context context, FloatingManager floatingmanager, RelativeLayout relativeLayout,
            View.OnTouchListener mOnTouchListener) {

        this.mContext = context;
        this.mFloatingManager = floatingmanager;
        this.mRelativeLayout = relativeLayout;

        mRelativeLayoutStatus = new RelativeLayout(mContext);

        /**ADB*/
        RelativeLayout.LayoutParams mLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);

        mLayoutParams.width = EasyTool.dip2px(mContext,ROUNDD );
        mLayoutParams.height  = EasyTool.dip2px(mContext,ROUNDD);
        mLayoutParams.setMargins(0,0,0,0);
        mLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
        mLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);

        ib_adb = StatusButton(mRelativeLayoutStatus,mLayoutParams,R.drawable.float_status_adb);

        /**MAP**/
        mLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);

        mLayoutParams.width = EasyTool.dip2px(mContext,ROUNDD );
        mLayoutParams.height  = EasyTool.dip2px(mContext,ROUNDD);
        mLayoutParams.setMargins(-10,-10,-10,-10);
        mLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
        mLayoutParams.addRule(RelativeLayout.RIGHT_OF, ib_adb.getId());
        mLayoutParams.addRule(RelativeLayout.BELOW, ib_adb.getId());

        ib_map = StatusButton(mRelativeLayoutStatus,mLayoutParams,R.drawable.float_status_map);

        /**BLE*/
        mLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);

        mLayoutParams.width = EasyTool.dip2px(mContext,ROUNDD );
        mLayoutParams.height  = EasyTool.dip2px(mContext,ROUNDD);
        mLayoutParams.setMargins(0,0,0,0);
        mLayoutParams.addRule(RelativeLayout.ALIGN_TOP,ib_adb.getId());
        mLayoutParams.addRule(RelativeLayout.RIGHT_OF, ib_map.getId());

        ib_blue = StatusButton(mRelativeLayoutStatus,mLayoutParams,R.drawable.float_status_blue);

        /**MOUSE*/
        mLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);

        mLayoutParams.width = EasyTool.dip2px(mContext,ROUNDD );
        mLayoutParams.height  = EasyTool.dip2px(mContext,ROUNDD);
        mLayoutParams.setMargins(0,0,0,0);
        mLayoutParams.addRule(RelativeLayout.ALIGN_LEFT,ib_adb.getId());
        mLayoutParams.addRule(RelativeLayout.BELOW, ib_map.getId());

        ib_mouse = StatusButton(mRelativeLayoutStatus,mLayoutParams,R.drawable.float_status_mouse);

        /**KEYBOARD*/
        mLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);

        mLayoutParams.width = EasyTool.dip2px(mContext,ROUNDD );
        mLayoutParams.height  = EasyTool.dip2px(mContext,ROUNDD);
        mLayoutParams.setMargins(0,0,0,0);
        mLayoutParams.addRule(RelativeLayout.ALIGN_TOP,ib_mouse.getId());
        mLayoutParams.addRule(RelativeLayout.RIGHT_OF, ib_map.getId());

        ib_keyboard = StatusButton(mRelativeLayoutStatus,mLayoutParams,R.drawable.float_status_keyboard);

        /****/

        mLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);

        mLayoutParams.setMargins(20,20,20,20);

        GradientDrawable mDrawable = new GradientDrawable();
        mDrawable.setCornerRadius(EasyTool.dip2px(mContext,8));
        //mDrawable.setColor(Color.parseColor("#6000FF00"));
        mDrawable.setColor(Color.parseColor("#60FF0000"));
        mDrawable.setShape(GradientDrawable.RECTANGLE);

        mRelativeLayoutStatus.setBackground(mDrawable);
        mRelativeLayoutStatus.setLayoutParams(mLayoutParams);
        //mRelativeLayoutStatus.setBackgroundColor(0x6000FF00);
        mRelativeLayoutStatus.setId(View.generateViewId());
        id = mRelativeLayoutStatus.getId();
        mRelativeLayoutStatus.setClickable(true);
        mRelativeLayoutStatus.setOnTouchListener(mOnTouchListener);




        mRelativeLayout.addView(mRelativeLayoutStatus);
    }

    private ImageButton StatusButton(RelativeLayout mRelativeLayoutStatus,RelativeLayout.LayoutParams mLayoutParams,int image)
    {

        ImageButton mButton = new ImageButton(mContext);
        mButton.setPadding(0,0,0,0);
        mButton.setLayoutParams(mLayoutParams);   ////设置按钮的布局属性

        GradientDrawable mDrawable = new GradientDrawable();
        mDrawable.setCornerRadius(EasyTool.dip2px(mContext,ROUNDD));
        mDrawable.setColor(Color.parseColor("#ff0000"));
        mDrawable.setShape(GradientDrawable.RECTANGLE);

        mButton.setScaleType(ImageButton.ScaleType.FIT_XY);
        mButton.setImageDrawable(mContext.getResources().getDrawable(image));
        mButton.setBackground(mDrawable);
        mButton.setId(View.generateViewId());
        mButton.setClickable(false);

        mRelativeLayoutStatus.addView(mButton);


        return mButton;
    }

    private ImageButton StatusButtonSquare(RelativeLayout mRelativeLayoutStatus,RelativeLayout.LayoutParams mLayoutParams
            ,int image)
    {

        ImageButton mButton = new ImageButton(mContext);
        mButton.setPadding(0,0,0,0);
        mButton.setLayoutParams(mLayoutParams);   ////设置按钮的布局属性

        GradientDrawable mDrawable = new GradientDrawable();
        //mDrawable.setCornerRadius(EasyTool.dip2px(mContext,ROUNDD));
        mDrawable.setColor(Color.parseColor("#ff0000"));
        mDrawable.setShape(GradientDrawable.RECTANGLE);

        mButton.setScaleType(ImageButton.ScaleType.FIT_XY);
        mButton.setImageDrawable(mContext.getResources().getDrawable(image));
        mButton.setBackground(mDrawable);
        mButton.setId(View.generateViewId());


        mRelativeLayoutStatus.addView(mButton);

        return mButton;
    }

    public void startRotateAnimation(int start,int end)
    {
        RotateAnimation rotateAnimation = new RotateAnimation(start,end,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(400);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setAnimationListener(mAnimationListener);

        mRelativeLayoutStatus.startAnimation(rotateAnimation);

        if(start == 0) {
            rotateAnimation = new RotateAnimation(start, 360 - end,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        }else{
            rotateAnimation = new RotateAnimation(360 - start,  end,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        }
        rotateAnimation.setDuration(400);
        rotateAnimation.setFillAfter(true);

        ib_map.startAnimation(rotateAnimation);
        ib_adb.startAnimation(rotateAnimation);
        ib_blue.startAnimation(rotateAnimation);
        ib_mouse.startAnimation(rotateAnimation);
        ib_keyboard.startAnimation(rotateAnimation);
    }

    Animation.AnimationListener mAnimationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation){


        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    void SetMapStatus(boolean value)
    {
        GradientDrawable mDrawable = new GradientDrawable();
        mDrawable.setCornerRadius(EasyTool.dip2px(mContext,ROUNDD));
        if(!value)
        {
            mDrawable.setColor(Color.parseColor("#ff0000"));

        }else{
            mDrawable.setColor(Color.parseColor("#02bb99"));
        }

        mDrawable.setShape(GradientDrawable.RECTANGLE);
        ib_map.setBackground(mDrawable);
    }

    void SetADBStatus(boolean value)
    {
        GradientDrawable mDrawable = new GradientDrawable();
        mDrawable.setCornerRadius(EasyTool.dip2px(mContext,ROUNDD));
        if(!value)
        {
            mDrawable.setColor(Color.parseColor("#ff0000"));

        }else{
            mDrawable.setColor(Color.parseColor("#02bb99"));
        }

        mDrawable.setShape(GradientDrawable.RECTANGLE);
        ib_adb.setBackground(mDrawable);
    }

    void SetBLUEStatus(boolean value)
    {
        GradientDrawable mDrawable = new GradientDrawable();
        mDrawable.setCornerRadius(EasyTool.dip2px(mContext,ROUNDD));
        if(!value)
        {
            mDrawable.setColor(Color.parseColor("#ff0000"));

        }else{
            mDrawable.setColor(Color.parseColor("#02bb99"));
        }

        mDrawable.setShape(GradientDrawable.RECTANGLE);
        ib_blue.setBackground(mDrawable);
    }

    void SetMouseStatus(boolean value)
    {
        GradientDrawable mDrawable = new GradientDrawable();
        mDrawable.setCornerRadius(EasyTool.dip2px(mContext,ROUNDD));
        if(!value)
        {
            mDrawable.setColor(Color.parseColor("#ff0000"));

        }else{
            mDrawable.setColor(Color.parseColor("#02bb99"));
        }

        mDrawable.setShape(GradientDrawable.RECTANGLE);
        ib_mouse.setBackground(mDrawable);
    }
    void SetKeyBoardStatus(boolean value)
    {
        GradientDrawable mDrawable = new GradientDrawable();
        mDrawable.setCornerRadius(EasyTool.dip2px(mContext,ROUNDD));
        if(!value)
        {
            mDrawable.setColor(Color.parseColor("#ff0000"));

        }else{
            mDrawable.setColor(Color.parseColor("#02bb99"));
        }

        mDrawable.setShape(GradientDrawable.RECTANGLE);
        ib_keyboard.setBackground(mDrawable);
    }
}
