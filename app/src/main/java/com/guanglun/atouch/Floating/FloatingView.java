package com.guanglun.atouch.Floating;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PixelFormat;
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
public class FloatingView extends FrameLayout implements View.OnClickListener {

    private Context mContext;
    private View select_view;
    private Button bt_float_manager,bt_float_add,bt_float_save,bt_float_close;

    private int mTouchStartX, mTouchStartY;//手指按下时坐标

    private FloatingManager mFloatingManager;
    private FloatSelectAlertDialog floatSelectAlertDialog;
    private ListView select_listview;

    private RelativeLayout mRelativeLayout;

    private List<KeyMouse> keyMouseList;

    private FloatPUBGManager mFloatPUBGManager;
    private FloatButtonManager mFloatButtonManager;
    private WindowManager.LayoutParams mParams;
    private DBControl dbControl;
    private DBControlPUBG dbControlPUBG;
    private boolean isChange = false;
    private String ChangeTableName;

    public enum WindowStatus {
        CLOSE,
        OPEN
    }

    private WindowStatus mWindowStatus = WindowStatus.CLOSE;



    public FloatingView(Context context) {

        super(context);

        mContext = context.getApplicationContext();

        LayoutInflater mLayoutInflater = LayoutInflater.from(context);

        mRelativeLayout = (RelativeLayout) mLayoutInflater.inflate(R.layout.floating_view, null);

        select_view = mLayoutInflater.inflate(R.layout.float_controller_volume, null);
        select_listview = select_view.findViewById(R.id.listview);
        floatSelectAlertDialog = new FloatSelectAlertDialog(mContext,select_view);

        //dbControl = new DBControl(context);
        //keyMouseList = dbControl.LoadTableDatabaseList(DatabaseStatic.TABLE_NAME);

        dbControlPUBG = new DBControlPUBG(context);

        //FloatListAdapter floatListAdapter = new FloatListAdapter(context,keyMouseList);
        //select_listview.setAdapter(floatListAdapter);

        //select_listview.setOnItemClickListener(OnItemClickListenerItem);

        bt_float_manager = (Button) mRelativeLayout.findViewById(R.id.bt_float_manager);
        bt_float_add = (Button) mRelativeLayout.findViewById(R.id.bt_float_add);
        bt_float_save = (Button) mRelativeLayout.findViewById(R.id.bt_float_save);
        bt_float_close = (Button) mRelativeLayout.findViewById(R.id.bt_float_close);

        bt_float_manager.setOnClickListener(this);
        bt_float_add.setOnClickListener(this);
        bt_float_save.setOnClickListener(this);
        bt_float_close.setOnClickListener(this);

        mFloatingManager = FloatingManager.getInstance(mContext);
        mFloatButtonManager = new FloatButtonManager(mContext,mFloatingManager,mRelativeLayout,mParams);
        mFloatPUBGManager = new FloatPUBGManager(mContext,mFloatingManager,mRelativeLayout,mParams);


    }

    void SelectKey()
    {


        floatSelectAlertDialog.Show();
    }

    public void show(String TableName) {

        hide();

        mParams = new WindowManager.LayoutParams();
        mParams.gravity = Gravity.BOTTOM | Gravity.LEFT;

        //总是出现在应用程序窗口之上
        mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        //设置图片格式，效果为背景透明
        mParams.format = PixelFormat.RGBA_8888;
        mParams.flags =  WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;

        mParams.width = LayoutParams.MATCH_PARENT;
        mParams.height = 200;//LayoutParams.WRAP_CONTENT;

        mRelativeLayout.setBackgroundColor(0x00FFFFFF);
        mFloatingManager.addView(mRelativeLayout, mParams);

        bt_float_manager.setText("管理");
        bt_float_add.setVisibility(GONE);
        bt_float_save.setVisibility(GONE);
        bt_float_close.setVisibility(GONE);

        mWindowStatus = WindowStatus.CLOSE;
        isChange = false;

        PUBG pubg = new PUBG();
        mFloatPUBGManager.Show(pubg);

//        if(TableName != null){
//
//            isChange = true;
//            mParams.height = LayoutParams.MATCH_PARENT;
//            mRelativeLayout.setBackgroundColor(0x60ebebeb);
//            mFloatingManager.updateView(mRelativeLayout, mParams);
//            bt_float_manager.setText("取消");
//            bt_float_add.setVisibility(VISIBLE);
//            bt_float_save.setVisibility(VISIBLE);
//            bt_float_close.setVisibility(VISIBLE);
//
//            mWindowStatus = WindowStatus.OPEN;
//
//            mFloatButtonManager.Load(TableName,dbControl);
//            ChangeTableName = TableName;
//
//        }
    }

    public void hide() {
        mFloatPUBGManager.RemoveAll();
        //mFloatButtonManager.RemoveAll();
        mFloatingManager.removeView(mRelativeLayout);

    }


//    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
//        @Override
//        public boolean onTouch(View view, MotionEvent event) {
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    mTouchStartX = (int) event.getRawX();
//                    mTouchStartY = (int) event.getRawY();
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    mParams.x += (int) event.getRawX() - mTouchStartX;
//                    mParams.y += (int) event.getRawY() - mTouchStartY;//相对于屏幕左上角的位置
//                    mWindowManager.updateView(relativeLayout, mParams);
//                    break;
//                case MotionEvent.ACTION_UP:
//                    break;
//            }
//            return true;
//        }
//    };

    private AdapterView.OnItemClickListener OnItemClickListenerItem = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            KeyMouse keyMouse = keyMouseList.get(i);

            mFloatButtonManager.Add(keyMouse);

            floatSelectAlertDialog.Cancel();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_float_manager:
                //Log.e("DEBUG","bt_float_manager");

                if(mWindowStatus == WindowStatus.CLOSE)
                {
                    mParams.height = LayoutParams.MATCH_PARENT;
                    mRelativeLayout.setBackgroundColor(0x60ebebeb);
                    mFloatingManager.updateView(mRelativeLayout, mParams);
                    bt_float_manager.setText("取消");
                    bt_float_add.setVisibility(VISIBLE);
                    bt_float_save.setVisibility(VISIBLE);
                    bt_float_close.setVisibility(VISIBLE);

                    mWindowStatus = WindowStatus.OPEN;

                }else{

                    mRelativeLayout.setBackgroundColor(0x00FFFFFF);
                    //mParams.height = LayoutParams.WRAP_CONTENT;

                    mParams.width = LayoutParams.MATCH_PARENT;
                    mParams.height = 200;//LayoutParams.WRAP_CONTENT;

                    mFloatingManager.updateView(mRelativeLayout, mParams);
                    bt_float_manager.setText("管理");
                    bt_float_add.setVisibility(GONE);
                    bt_float_save.setVisibility(GONE);
                    bt_float_close.setVisibility(GONE);

                    mWindowStatus = WindowStatus.CLOSE;
                }

                break;
            case R.id.bt_float_add:
                Log.e("DEBUG","bt_float_add");
                SelectKey();
                break;
            case R.id.bt_float_save:
                Log.e("DEBUG","bt_float_save");


                SaveKeyMap();

                break;
            case R.id.bt_float_close:
                Log.e("DEBUG","bt_float_close");
                hide();

                break;
            default:
                break;
        }
    }


    private void SaveKeyMap(){

        if(!isChange)
        {
            final EditText editText = new EditText(mContext);
            AlertDialog dialog = new AlertDialog.Builder(mContext)
                    //.setIcon(R.mipmap.icon)//设置标题的图片
                    .setTitle("请输入名称")//设置对话框的标题
                    .setView(editText)
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String TableName = editText.getText().toString();

                                mFloatPUBGManager.Save(TableName, dbControlPUBG);
                                //mFloatButtonManager.Save(TableName, dbControl);
                                hide();

                            //Toast.makeText(MainActivity.this, content, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }).create();

            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            dialog.show();
        }else{
            dbControl.ClearTable(ChangeTableName);
            mFloatButtonManager.Save(ChangeTableName, dbControl);
            hide();
        }


    }
}
