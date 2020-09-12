package com.guanglun.atouch.Floating;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.guanglun.atouch.DBManager.KeyMouse;
import com.guanglun.atouch.Main.EasyTool;
import com.guanglun.atouch.R;

import static android.view.View.VISIBLE;

public class FloatButtonPUBG {
    private final int ROUNDD = 30;

    private Context mContext;
    private FloatingManager mFloatingManager;
    private RelativeLayout mRelativeLayout;
    private WindowManager.LayoutParams mParams;

    private Button button;

    public int PositionX,PositionY;
    private FloatMenu mFloatMenu;

    public FloatButtonPUBG(Context context, FloatMenu mFloatMenu, RelativeLayout relativeLayout, WindowManager.LayoutParams params,
                       int StartX, int StartY,int picture)
    {
        mContext = context;
        this.mFloatMenu = mFloatMenu;
        mFloatingManager = mFloatMenu.mFloatingManager;
        mRelativeLayout = relativeLayout;
        mParams = params;

        RelativeLayout.LayoutParams mLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);

        button = new Button(mContext);

        mLayoutParams.width = EasyTool.dip2px(mContext,ROUNDD);
        mLayoutParams.height  = EasyTool.dip2px(mContext,ROUNDD);

        mLayoutParams.setMargins(0,0,0,0);
        mLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
        mLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);

        button.setBackground(mContext.getResources().getDrawable(picture));

        button.setX(StartX - mFloatMenu.mFloatPUBGManager.offset[0]- EasyTool.dip2px(mContext,ROUNDD/2));
        button.setY(StartY - mFloatMenu.mFloatPUBGManager.offset[1]- EasyTool.dip2px(mContext,ROUNDD/2));

        PositionX =StartX;
        PositionY = StartY;

        //button.setText(mKeyMouse.Name);
        button.setPadding(0,0,0,0);
        button.setLayoutParams(mLayoutParams);   ////设置按钮的布局属性
        button.setOnTouchListener(ButtonOnTouchListener);
        mRelativeLayout.addView(button);

        mFloatingManager.updateView(mRelativeLayout,mParams);

    }

    public void Remove()
    {
        if(button != null)
        {
            mRelativeLayout.removeView(button);
            button = null;
        }
    }

    public void Show()
    {
        if(button != null)
        {
            button.setVisibility(VISIBLE);
            //mRelativeLayout.up(button);
            //button = null;
        }
    }

    public void Hide()
    {
        if(button != null)
        {
            button.setVisibility(View.GONE);
            //mRelativeLayout.removeView(button);
            //button = null;
        }
    }

    View.OnTouchListener ButtonOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {


//            PositionX = mFloatMenu.mFloatMouse.mParamsMouse.x;//(int)event.getRawX();
//            PositionY = mFloatMenu.mFloatMouse.mParamsMouse.y;//(int)event.getRawY();
            PositionX = (int)event.getRawX();
            PositionY = (int)event.getRawY();
            button.setX(PositionX  - mFloatMenu.mFloatPUBGManager.offset[0] - EasyTool.dip2px(mContext,ROUNDD/2));
            button.setY(PositionY  - mFloatMenu.mFloatPUBGManager.offset[1] - EasyTool.dip2px(mContext,ROUNDD/2));

            return false;
        }
    };
}
