package com.guanglun.atouch.Floating;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.guanglun.atouch.DBManager.DBControl;
import com.guanglun.atouch.DBManager.DBControlPUBG;
import com.guanglun.atouch.DBManager.DatabaseStatic;
import com.guanglun.atouch.DBManager.KeyMouse;
import com.guanglun.atouch.DBManager.PUBG;
import com.guanglun.atouch.R;

import java.util.List;

/**
 * 悬浮窗view
 */
public class FloatingView extends FrameLayout{

    private Context mContext;

    private Button bt_float_manager,bt_float_chose,bt_float_save,bt_float_close;

    private int mTouchStartX, mTouchStartY;//手指按下时坐标

    private FloatingManager mFloatingManager;



    private RelativeLayout mRelativeLayout;

    private List<KeyMouse> keyMouseList;

    private FloatPUBGManager mFloatPUBGManager;
    private FloatButtonManager mFloatButtonManager;
    private WindowManager.LayoutParams mParams;
    private DBControl dbControl;
    private DBControlPUBG dbControlPUBG;
    private boolean isChange = false;
    private String SelectName;
    private List<String> NameList;
    private final String DEBUG_TAG = "FloatingView";


    public enum WindowStatus {
        CLOSE,
        OPEN
    }

    private WindowStatus mWindowStatus = WindowStatus.CLOSE;

    public interface FloatingViewCallBack {
        void ChoseName(String Name);
    }

    public FloatingView(Context context,FloatingViewCallBack cb) {

        super(context);

        mContext = context.getApplicationContext();
        FloatMenu mFloatMenu = new FloatMenu(mContext,cb);
    }

    public void show(String SelectName,String IsStartUp) {



//        if(SelectName != null) {
//
//            pubg = dbControlPUBG.GetRawByName(SelectName);
//
//            //isChange = true;
//            mParams.height = LayoutParams.MATCH_PARENT;
//            mRelativeLayout.setBackgroundColor(0x60ebebeb);
//            mFloatingManager.updateView(mRelativeLayout, mParams);
//            bt_float_manager.setText("取消");
//            bt_float_chose.setVisibility(VISIBLE);
//            bt_float_save.setVisibility(VISIBLE);
//            bt_float_close.setVisibility(VISIBLE);
//
//            mWindowStatus = WindowStatus.OPEN;
//
//            mFloatPUBGManager.Show(pubg);
//
//        }else {
//            pubg = new PUBG();
//            mFloatPUBGManager.Show(pubg);
//        }

    }

    public void hide() {
        mFloatPUBGManager.RemoveAll();
        mFloatingManager.removeView(mRelativeLayout);

    }
}
