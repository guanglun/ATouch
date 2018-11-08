package com.guanglun.atouch.Floating;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.guanglun.atouch.DBManager.KeyMouse;
import com.guanglun.atouch.Main.EasyTool;
import com.guanglun.atouch.R;

public class FloatButton {

    private final int ROUNDD = 30;

    private Context mContext;
    private FloatingManager mFloatingManager;
    private RelativeLayout mRelativeLayout;
    private WindowManager.LayoutParams mParams;

    private Button button;
    private KeyMouse mKeyMouse;

    public int PositionX,PositionY;

    public FloatButton(Context context, FloatingManager floatingmanager, RelativeLayout relativeLayout, WindowManager.LayoutParams params,
                       KeyMouse keyMouse, int StartX, int StartY)
    {
        mContext = context;
        mFloatingManager = floatingmanager;
        mRelativeLayout = relativeLayout;
        mParams = params;
        mKeyMouse = keyMouse;

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);

        button = new Button(mContext);

        lp.width = EasyTool.dip2px(mContext,ROUNDD);
        lp.height  = EasyTool.dip2px(mContext,ROUNDD);

        button.setBackground(mContext.getResources().getDrawable(R.drawable.round_button));

        button.setX(StartX- EasyTool.dip2px(mContext,ROUNDD/2));
        button.setY(StartY- EasyTool.dip2px(mContext,ROUNDD/2));

        PositionX =StartX;
        PositionY = StartY;

        button.setText(mKeyMouse.Name);
        button.setPadding(0,0,0,0);
        button.setLayoutParams(lp);   ////设置按钮的布局属性
        button.setOnTouchListener(ButtonOnTouchListener);
        mRelativeLayout.addView(button);

        mFloatingManager.updateView(mRelativeLayout,mParams);
    }

    public void Remove()
    {
        mRelativeLayout.removeView(button);
    }

    View.OnTouchListener ButtonOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            PositionX = (int)event.getRawX();
            PositionY = (int)event.getRawY();
            button.setX(PositionX - EasyTool.dip2px(mContext,ROUNDD/2));
            button.setY(PositionY - EasyTool.dip2px(mContext,ROUNDD/2));

            return false;
        }
    };


}
