package com.guanglun.atouch.Floating;

import android.content.Context;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.guanglun.atouch.Main.EasyTool;
import com.guanglun.atouch.R;

import static android.view.animation.Animation.ABSOLUTE;

public class FloatMenuButton {

    public static final int MENU_MAIN_BUTTON  = 0;
    public static final int MENU_RIGHT_BUTTON = 1;
    public static final int MENU_LEFT_BUTTON  = 2;

    private final int ROUNDD = 40;
    public int id = 0;
    private Button mButton = null;
    private RelativeLayout mRelativeLayout;

    public FloatMenuButton(Context mContext,RelativeLayout mRelativeLayout,int Type,int align_id){

        RelativeLayout.LayoutParams mLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);

        this.mRelativeLayout = mRelativeLayout;

        mButton = new Button(mContext);

        if(Type == MENU_MAIN_BUTTON)
        {
            mLayoutParams.width = EasyTool.dip2px(mContext,ROUNDD);
            mLayoutParams.height  = EasyTool.dip2px(mContext,ROUNDD);
            mLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
            mLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
        }else if(Type == MENU_RIGHT_BUTTON)
        {
            mLayoutParams.width = EasyTool.dip2px(mContext,ROUNDD - 4);
            mLayoutParams.height  = EasyTool.dip2px(mContext,ROUNDD - 4);
            mLayoutParams.setMargins(20,0,0,0);
            mLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL,RelativeLayout.TRUE);
            mLayoutParams.addRule(RelativeLayout.RIGHT_OF, align_id );

        }else if(Type == MENU_LEFT_BUTTON)
        {
            mLayoutParams.width = EasyTool.dip2px(mContext,ROUNDD - 4);
            mLayoutParams.height  = EasyTool.dip2px(mContext,ROUNDD - 4);
            mLayoutParams.setMargins(0,0,20,0);
            mLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL,RelativeLayout.TRUE);
            mLayoutParams.addRule(RelativeLayout.LEFT_OF, align_id);
        }



        mButton.setPadding(0,0,0,0);
        mButton.setLayoutParams(mLayoutParams);   ////设置按钮的布局属性

        mButton.setText("+");
        mButton.setTextColor(0xFFFFFFFF);
        mButton.setBackground(mContext.getResources().getDrawable(R.drawable.round_button_red));

        mButton.setId(View.generateViewId());


        id = mButton.getId();

        mRelativeLayout.addView(mButton);


//        RotateAnimation (float fromDegrees, float toDegrees, int pivotXType, float pivotXValue, int pivotYType, float pivotYValue)
//        参数说明：
//        float fromDegrees：旋转的开始角度。
//        float toDegrees：旋转的结束角度。
//        int pivotXType：X轴的伸缩模式，可以取值为ABSOLUTE、RELATIVE_TO_SELF、RELATIVE_TO_PARENT。
//        float pivotXValue：X坐标的伸缩值。
//        int pivotYType：Y轴的伸缩模式，可以取值为ABSOLUTE、RELATIVE_TO_SELF、RELATIVE_TO_PARENT。
//        float pivotYValue：Y坐标的伸缩值。

        RotateAnimation rotateAnimation = new RotateAnimation(0,45,ABSOLUTE,ABSOLUTE);
        rotateAnimation.setDuration(3000);
        mButton.startAnimation(rotateAnimation);

    }

    public void Remove()
    {
        mRelativeLayout.removeView(mButton);
    }

    public void SetOnTouchListener(View.OnTouchListener mOnTouchListener)
    {
        mButton.setOnTouchListener(mOnTouchListener);
    }
}
