package com.guanglun.atouch.Floating;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.guanglun.atouch.R;

/**
 * 悬浮窗view
 */
public class FloatingView extends FrameLayout {
    private Context mContext;
    private View mView;
    private Button bt_float_manager,bt_float_add;
    private int mTouchStartX, mTouchStartY;//手指按下时坐标
    private WindowManager.LayoutParams mParams;
    private FloatingManager mWindowManager;

    public FloatingView(Context context) {
        super(context);
        mContext = context.getApplicationContext();
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        mView = mLayoutInflater.inflate(R.layout.floating_view, null);
        bt_float_manager = (Button) mView.findViewById(R.id.bt_float_manager);
        bt_float_manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("DEBUG","bt_float_manager");
            }
        });

        //bt_float_manager.setOnTouchListener(mOnTouchListener);

        bt_float_add = (Button) mView.findViewById(R.id.bt_float_add);
        bt_float_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("DEBUG","bt_float_add");
            }
        });

        mWindowManager = FloatingManager.getInstance(mContext);
    }

    public void show() {
        mParams = new WindowManager.LayoutParams();
        mParams.gravity = Gravity.TOP | Gravity.LEFT;
        mParams.x = 0;
        mParams.y = 100;
        //总是出现在应用程序窗口之上
        mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        //设置图片格式，效果为背景透明
        mParams.format = PixelFormat.RGBA_8888;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR |
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        mParams.width = LayoutParams.MATCH_PARENT;
        mParams.height = LayoutParams.WRAP_CONTENT;
        mWindowManager.addView(mView, mParams);

        //bt_float_add.setVisibility(View.GONE);
        //逐帧动画
        //AnimationDrawable animationDrawable=(AnimationDrawable)mImageView.getDrawable();
        //animationDrawable.start();
    }

    public void hide() {
        mWindowManager.removeView(mView);
    }

    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mTouchStartX = (int) event.getRawX();
                    mTouchStartY = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    mParams.x += (int) event.getRawX() - mTouchStartX;
                    mParams.y += (int) event.getRawY() - mTouchStartY;//相对于屏幕左上角的位置
                    mWindowManager.updateView(mView, mParams);
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            return true;
        }
    };
}
