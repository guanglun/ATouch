package com.guanglun.atouch.Floating;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

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

    private Button button = null;

    public int PositionX,PositionY;
    private int xtmp = 0, ytmp = 0;;


    public FloatButtonPUBG(Context context, FloatingManager floatingmanager, RelativeLayout relativeLayout, WindowManager.LayoutParams params,
                       int StartX, int StartY,int picture,int xtmp,int ytmp)
    {
        mContext = context;
        mFloatingManager = floatingmanager;
        mRelativeLayout = relativeLayout;
        mParams = params;

        this.xtmp = xtmp;
        this.ytmp = ytmp;

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);

        button = new Button(mContext);

        lp.width = EasyTool.dip2px(mContext,ROUNDD);
        lp.height  = EasyTool.dip2px(mContext,ROUNDD);

        button.setBackground(mContext.getResources().getDrawable(picture));

        button.setX(StartX - xtmp - EasyTool.dip2px(mContext,ROUNDD/2));
        button.setY(StartY - ytmp - EasyTool.dip2px(mContext,ROUNDD/2));

        PositionX =StartX;
        PositionY = StartY;

        //button.setText(mKeyMouse.Name);
        button.setPadding(0,0,0,0);
        button.setLayoutParams(lp);   ////设置按钮的布局属性
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

            PositionX = (int)event.getRawX();
            PositionY = (int)event.getRawY();
            button.setX(PositionX - xtmp - EasyTool.dip2px(mContext,ROUNDD/2));
            button.setY(PositionY - ytmp - EasyTool.dip2px(mContext,ROUNDD/2));

            return false;
        }
    };


}
