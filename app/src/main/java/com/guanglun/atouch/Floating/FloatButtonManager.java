package com.guanglun.atouch.Floating;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.guanglun.atouch.R;

public class FloatButton {

    private Context mContext;
    private FloatingManager mFloatingManager;
    private RelativeLayout mRelativeLayout;

    private WindowManager.LayoutParams mParams;

    public FloatButton(Context context, FloatingManager floatingmanager, RelativeLayout relativeLayout,WindowManager.LayoutParams params)
    {
        mContext = context;
        mFloatingManager = floatingmanager;
        mRelativeLayout = relativeLayout;
        mParams = params;
    }

    public void Add()
    {

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);

        final Button bt_test = new Button(mContext);

        //lp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        //lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        lp.width = dip2px(mContext,30);
        lp.height  = dip2px(mContext,30);


        bt_test.setBackground(mContext.getResources().getDrawable(R.drawable.round_button));

        //ib_test.setBackgroundColor(0x00FFFFFF);

        bt_test.setX(500);
        bt_test.setY(500);
        bt_test.setText("A");
        bt_test.setPadding(0,0,0,0);
        bt_test.setLayoutParams(lp);   ////设置按钮的布局属性
        mRelativeLayout.addView(bt_test);

        mFloatingManager.updateView(mRelativeLayout,mParams);


    }

    public void Show(){


    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
