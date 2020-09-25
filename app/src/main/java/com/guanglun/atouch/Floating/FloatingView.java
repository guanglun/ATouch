package com.guanglun.atouch.Floating;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.guanglun.atouch.DBManager.DBControlPUBG;
import com.guanglun.atouch.DBManager.KeyBoardCode;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 悬浮窗view
 */
public class FloatingView extends FrameLayout{

    private Context mcontext,mContext;

    private Button bt_float_manager,bt_float_chose,bt_float_save,bt_float_close;

    private int mTouchStartX, mTouchStartY;//手指按下时坐标

    private FloatingManager mFloatingManager;



    private RelativeLayout mRelativeLayout;

    private List<KeyBoardCode> keyMouseList;


    private WindowManager.LayoutParams mParams;
    private DBControlPUBG dbControlPUBG;
    private boolean isChange = false;
    private String SelectName;
    private List<String> NameList;
    private final String DEBUG_TAG = "FloatingView";

    public FloatMenu mFloatMenu;

    public enum WindowStatus {
        CLOSE,
        OPEN
    }

    private WindowStatus mWindowStatus = WindowStatus.CLOSE;
    private WindowManager wManager;
    public int mRotationLast = -1;

    public interface FloatingViewCallBack {
        void ChoseName(String Name);
    }

    public int[] getAccurateScreenDpi()
    {
        int[] screenWH = new int[2];
        Display display = wManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            Class<?> c = Class.forName("android.view.Display");
            Method method = c.getMethod("getRealMetrics",DisplayMetrics.class);
            method.invoke(display, dm);
            screenWH[0] = dm.widthPixels;
            screenWH[1] = dm.heightPixels;
        }catch(Exception e){
            e.printStackTrace();
        }
        return screenWH;
    }


    public FloatingView(Context context,FloatingViewCallBack cb) {

        super(context);
        mcontext = context;
        mContext = context.getApplicationContext();
        mFloatMenu = new FloatMenu(mContext,cb);

        wManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
    }

    public void show(String SelectName,String IsStartUp) {

    }

    public void onSreenOrientationConfigChange(int angle) {
        if(mFloatMenu.mFloatMapManager != null) {
            mFloatMenu.mFloatMapManager.reloadOffset();
        }
    }

    public void hide() {
        mFloatingManager.removeView(mRelativeLayout);

    }

    public void MouseDataRecv(int x,int y)
    {
        mFloatMenu.mFloatMouse.SetMouse(x,y);
    }
    public void MouseClickRecv(boolean down)
    {
        mFloatMenu.mFloatMouse.SetMouseClick(down);
    }


}
