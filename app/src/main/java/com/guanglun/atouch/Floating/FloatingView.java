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
public class FloatingView extends FrameLayout implements View.OnClickListener {

    private Context mContext;
    private View select_view;
    private Button bt_float_manager,bt_float_chose,bt_float_save,bt_float_close;

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
    private String SelectName;
    private List<String> NameList;
    private final String DEBUG_TAG = "FloatingView";
    private FloatingViewCallBack cb;

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

        this.cb = cb;

        mContext = context.getApplicationContext();

        LayoutInflater mLayoutInflater = LayoutInflater.from(context);

        //mRelativeLayout = (RelativeLayout) mLayoutInflater.inflate(R.layout.floating_view, null);

        mRelativeLayout = new RelativeLayout(mContext);

        select_view = mLayoutInflater.inflate(R.layout.float_controller_volume, null);
        select_listview = select_view.findViewById(R.id.listview);
        floatSelectAlertDialog = new FloatSelectAlertDialog(mContext, select_view, new FloatSelectAlertDialog.FloatSelectAlertDialogCallBack() {
            @Override
            public void NewCreat() {
                SelectName = null;
                mFloatPUBGManager.RemoveAll();
                mFloatPUBGManager.Show(new PUBG());
            }
        });

        //dbControl = new DBControl(context);
        //keyMouseList = dbControl.LoadTableDatabaseList(DatabaseStatic.TABLE_NAME);

        dbControlPUBG = new DBControlPUBG(context);

//        bt_float_manager = (Button) mRelativeLayout.findViewById(R.id.bt_float_manager);
//        bt_float_chose = (Button) mRelativeLayout.findViewById(R.id.bt_float_chose);
//        bt_float_save = (Button) mRelativeLayout.findViewById(R.id.bt_float_save);
//        bt_float_close = (Button) mRelativeLayout.findViewById(R.id.bt_float_close);
//
//        bt_float_manager.setOnClickListener(this);
//        bt_float_chose.setOnClickListener(this);
//        bt_float_save.setOnClickListener(this);
//        bt_float_close.setOnClickListener(this);

        mFloatingManager = FloatingManager.getInstance(mContext);
        mFloatButtonManager = new FloatButtonManager(mContext,mFloatingManager,mRelativeLayout,mParams);
        mFloatPUBGManager = new FloatPUBGManager(mContext,mFloatingManager,mRelativeLayout,mParams);


        FloatMenu mFloatMenu = new FloatMenu(mContext,mFloatingManager,mRelativeLayout,mParams);
    }

    void SelectName()
    {
        NameList = dbControlPUBG.LoadNameList();
        FloatListAdapter floatListAdapter = new FloatListAdapter(mContext,NameList);

        select_listview.setAdapter(floatListAdapter);
        select_listview.setOnItemClickListener(OnItemClickListenerItem);
        floatSelectAlertDialog.Show();
    }

    public void show(String SelectName,String IsStartUp) {

//        this.SelectName = SelectName;
//
//        hide();
//
//        mParams = new WindowManager.LayoutParams();
//        mParams.gravity = Gravity.BOTTOM | Gravity.LEFT;
//
//        //总是出现在应用程序窗口之上
//        mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
//        //设置图片格式，效果为背景透明
//        mParams.format = PixelFormat.RGBA_8888;
//        mParams.flags =  WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                | WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
//
//        mParams.width = LayoutParams.MATCH_PARENT;
//        mParams.height = 200;//LayoutParams.WRAP_CONTENT;
//
//        mRelativeLayout.setBackgroundColor(0x00FFFFFF);
//        mFloatingManager.addView(mRelativeLayout, mParams);
//
//        bt_float_manager.setText("管理");
//        bt_float_chose.setVisibility(GONE);
//        bt_float_save.setVisibility(GONE);
//        bt_float_close.setVisibility(GONE);
//
//        mWindowStatus = WindowStatus.CLOSE;
//        isChange = false;
//
//        PUBG pubg = null;
//
//        Log.i(DEBUG_TAG, "ShowShow");
//
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


    private AdapterView.OnItemClickListener OnItemClickListenerItem = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            DialogShow(NameList.get(i));

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
                    bt_float_chose.setVisibility(VISIBLE);
                    bt_float_save.setVisibility(VISIBLE);
                    bt_float_close.setVisibility(VISIBLE);
                    mFloatPUBGManager.ShowAll();
                    mWindowStatus = WindowStatus.OPEN;

                } else {

                    mRelativeLayout.setBackgroundColor(0x00FFFFFF);
                    //mParams.height = LayoutParams.WRAP_CONTENT;

                    mParams.width = LayoutParams.MATCH_PARENT;
                    mParams.height = 200;//LayoutParams.WRAP_CONTENT;

                    mFloatingManager.updateView(mRelativeLayout, mParams);
                    bt_float_manager.setText("管理");
                    bt_float_chose.setVisibility(GONE);
                    bt_float_save.setVisibility(GONE);
                    bt_float_close.setVisibility(GONE);
                    mFloatPUBGManager.HideAll();
                    mWindowStatus = WindowStatus.CLOSE;
                }

                break;
            case R.id.bt_float_chose:
                Log.e("DEBUG","bt_float_chose");
                SelectName();


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

        if(SelectName == null)
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
                            SelectName = editText.getText().toString();

                                mFloatPUBGManager.Save(SelectName, dbControlPUBG);
                                //mFloatButtonManager.Save(TableName, dbControl);
                                //hide();

                            //Toast.makeText(MainActivity.this, content, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }).create();

            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            dialog.show();
        }else{
            AlertDialog dialog = new AlertDialog.Builder(mContext)
                    //.setIcon(R.mipmap.icon)//设置标题的图片
                    .setTitle("确认保存？")//设置对话框的标题
                    .setNeutralButton("保存并使用", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dbControlPUBG.DeleteRaw(SelectName);
                            mFloatPUBGManager.Save(SelectName, dbControlPUBG);
                            cb.ChoseName(SelectName);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dbControlPUBG.DeleteRaw(SelectName);
                            mFloatPUBGManager.Save(SelectName, dbControlPUBG);

                            dialog.dismiss();
                        }
                    }).create();

            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            dialog.show();

        }


    }

    private void DialogShow(final String DialogSelectName){

        final String items[] = {"使用", "修改", "删除"};
        AlertDialog dialog = new AlertDialog.Builder(mContext)
                //.setIcon(R.mipmap.icon)//设置标题的图片
                .setTitle("选择对\"" + DialogSelectName + "\"的操作")//设置对话框的标题
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (items[which])
                        {
                            case "使用":
                                cb.ChoseName(DialogSelectName);
                                break;
                            case "修改":

                                SelectName = DialogSelectName;
                                mFloatPUBGManager.RemoveAll();
                                mFloatPUBGManager.Show(dbControlPUBG.GetRawByName(SelectName));

                                break;
                            case "删除":

                                AlertDialog dialog2 = new AlertDialog.Builder(mContext).setTitle("确认删除\"" + DialogSelectName + "\"?")
                                        .setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dbControlPUBG.DeleteRaw(DialogSelectName);
                                                if(DialogSelectName.equals(SelectName))
                                                {
                                                    SelectName = null;
                                                    mFloatPUBGManager.RemoveAll();
                                                    mFloatPUBGManager.Show(new PUBG());
                                                }


                                            }
                                        }).create();

                                dialog2.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                                dialog2.show();


                                break;
                            default:
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();

        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
    }

    private void showToast(String str)
    {

        Toast.makeText(mContext, str,
                Toast.LENGTH_SHORT).show();

    }
}
