package com.guanglun.atouch.Floating;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.guanglun.atouch.DBManager.DBManagerMapUnit;
import com.guanglun.atouch.Main.EasyTool;
import com.guanglun.atouch.R;

public class FloatMenu {

    private Button button;
    private Context mContext;
    public FloatingManager mFloatingManager;
    public RelativeLayout mRelativeLayoutMenu,mRelativeLayoutEdit;
    public WindowManager.LayoutParams mParamsMenu,mParamsEdit;
    private final int ROUNDD = 50;
    private int StartX = 0,StartY = 0;
    private FloatMenuButton fmb_menu,fmb_config,fmb_database,fmb_revise,fmb_save,fmb_add;
    public FloatMapManager mFloatMapManager;
    public DBManagerMapUnit dbManager;
    private FloatSelectAlertDialog floatSelectAlertDialog;

    private ListView select_listview;
    public FloatMenuStatus mFloatMenuStatus;
    public FloatingView.FloatingViewCallBack cb;
    public FloatMouse mFloatMouse;

    private int[] offset = new int[2],offset2 = new int[2];

    @SuppressLint("ResourceType")
    public FloatMenu(Context context,FloatingView.FloatingViewCallBack cb)
    {
        mContext = context;
        this.cb = cb;

        mRelativeLayoutMenu = new RelativeLayout(mContext);
        mRelativeLayoutEdit = new RelativeLayout(mContext);
        mParamsMenu = new WindowManager.LayoutParams();
        mParamsEdit = new WindowManager.LayoutParams();

        mFloatingManager = FloatingManager.getInstance(mContext);


        /****/

        dbManager = new DBManagerMapUnit(mContext,this);

        mFloatMapManager = new FloatMapManager(mContext, this, mRelativeLayoutEdit, mParamsEdit);


        View select_view = LayoutInflater.from(mContext).inflate(R.layout.float_controller_volume, null);
        select_listview = select_view.findViewById(R.id.listview);
        floatSelectAlertDialog = new FloatSelectAlertDialog(mContext, select_view, new FloatSelectAlertDialog.FloatSelectAlertDialogCallBack() {
            @Override
            public void NewCreat() {
                mFloatMapManager.ShowWindow();
                mFloatMapManager.removeAll();
                mFloatMapManager.creatNew();
            }
        });

        /****/
        mRelativeLayoutEdit.setBackgroundColor(0x60ebebeb);

        mParamsEdit.gravity = Gravity.LEFT|Gravity.TOP;

        //总是出现在应用程序窗口之上
        if (Build.VERSION.SDK_INT>=26) {//8.0新特性
            mParamsEdit.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else{
            mParamsEdit.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }

        //设置图片格式，效果为背景透明
        mParamsEdit.format = PixelFormat.RGBA_8888;
//        mParamsEdit.flags =  WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                | WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        mParamsEdit.flags =  WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        mParamsEdit.width = 0;
        mParamsEdit.height = 0;

        mFloatingManager.addView(mRelativeLayoutEdit, mParamsEdit);


        ////////////////////////////////////////////////////////////

        mParamsMenu.gravity = Gravity.LEFT|Gravity.TOP;

        mParamsMenu.x = (StartX);
        mParamsMenu.y = (StartY + EasyTool.getStatusBarHeight(mContext));


        if (Build.VERSION.SDK_INT>=26) {//8.0新特性
            mParamsMenu.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else{
            mParamsMenu.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }

        //设置图片格式，效果为背景透明
        mParamsMenu.format = PixelFormat.RGBA_8888;
        mParamsMenu.flags =  WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        mParamsMenu.width = FrameLayout.LayoutParams.WRAP_CONTENT;//MATCH_PARENT;
        mParamsMenu.height = FrameLayout.LayoutParams.WRAP_CONTENT;//MATCH_PARENT;

        //mParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
        //mParams.height = FrameLayout.LayoutParams.MATCH_PARENT;

        //mRelativeLayoutMenu.setBackgroundColor(0x60FF0000);
        mRelativeLayoutMenu.setBackgroundColor(0x00000000);


        //fmb_menu = new FloatMenuButton(mContext,mRelativeLayoutMenu,FloatMenuButton.MENU_MAIN_BUTTON,1,R.drawable.float_menu_add);
        //fmb_menu.SetOnTouchListener(mOnTouchListener);

        mFloatMenuStatus = new FloatMenuStatus(mContext,mFloatingManager,mRelativeLayoutMenu,mOnTouchListener);
        mFloatingManager.addView(mRelativeLayoutMenu, mParamsMenu);


        mFloatMouse = new FloatMouse(mContext, this, new FloatMouse.MouseCallback() {
            @Override
            public void onClick(boolean down) {
            }
        });

    }

    private int temp_x = 0,temp_y = 0;
    boolean isMoveTouch = false;

    View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    temp_x = (int)event.getRawX();
                    temp_y = (int)event.getRawY();
                    isMoveTouch = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                        if(!isMoveTouch)
                        {
                            if(Math.abs((int)event.getRawX() - temp_x) > 80 ||
                                    Math.abs((int)event.getRawY() - temp_y) > 80)
                            {
                                isMoveTouch = true;
                                mParamsMenu.x += ((int)event.getRawX() - temp_x);
                                mParamsMenu.y += ((int)event.getRawY() - temp_y);
                                temp_x = (int)event.getRawX();
                                temp_y = (int)event.getRawY();
                                mFloatingManager.updateView(mRelativeLayoutMenu, mParamsMenu);
                            }
                        }else{
                            mParamsMenu.x += ((int)event.getRawX() - temp_x);
                            mParamsMenu.y += ((int)event.getRawY() - temp_y);
                            temp_x = (int)event.getRawX();
                            temp_y = (int)event.getRawY();
                            mFloatingManager.updateView(mRelativeLayoutMenu, mParamsMenu);
                        }

                    break;
                case MotionEvent.ACTION_UP:
                    if(!isMoveTouch)
                    {
                        MenuClick();
                    }
                    break;
            }



            //Log.i("DEBUG","touch "+(int)event.getRawX()+" "+(int)event.getRawY());
            return false;
        }
    };

    private boolean isMenuClick = true;
    private void MenuClick()
    {
        if(isMenuClick)
        {
            isMenuClick = false;
            mFloatMenuStatus.startRotateAnimation(0,45);

            fmb_config = new FloatMenuButton(mContext,mRelativeLayoutMenu,FloatMenuButton.MENU_RIGHT_BUTTON,mFloatMenuStatus.id,R.drawable.float_menu_config);
            fmb_database = new FloatMenuButton(mContext,mRelativeLayoutMenu,FloatMenuButton.MENU_RIGHT_BUTTON,fmb_config.id,R.drawable.float_menu_database);
            fmb_revise = new FloatMenuButton(mContext,mRelativeLayoutMenu,FloatMenuButton.MENU_RIGHT_BUTTON,fmb_database.id,R.drawable.float_menu_revise);
            fmb_save = new FloatMenuButton(mContext,mRelativeLayoutMenu,FloatMenuButton.MENU_RIGHT_BUTTON,fmb_revise.id,R.drawable.float_menu_save);
            fmb_add = new FloatMenuButton(mContext,mRelativeLayoutMenu,FloatMenuButton.MENU_RIGHT_BUTTON,fmb_save.id,R.drawable.float_menu_add);

            fmb_config.SetOnClickListener(FloatMenuConfigOnClickListener);
            fmb_database.SetOnClickListener(FloatMenuDatabaseOnClickListener);
            fmb_revise.SetOnClickListener(FloatMenuReviseOnClickListener);
            fmb_save.SetOnClickListener(FloatMenuSaveOnClickListener);
            fmb_add.SetOnClickListener(FloatMenuAddOnClickListener);

            fmb_config.startScaleAnimationAnimation(0,1);
            fmb_database.startScaleAnimationAnimation(0,1);
            fmb_revise.startScaleAnimationAnimation(0,1);
            fmb_save.startScaleAnimationAnimation(0,1);
            fmb_add.startScaleAnimationAnimation(0,1);

            mFloatingManager.updateView(mRelativeLayoutMenu, mParamsMenu);

        }else{

            isMenuClick = true;
            mFloatMenuStatus.startRotateAnimation(45,0);

            fmb_config.startScaleAnimationAnimation(1,0);
            fmb_database.startScaleAnimationAnimation(1,0);
            fmb_revise.startScaleAnimationAnimation(1,0);
            fmb_save.startScaleAnimationAnimation(1,0);
            fmb_add.startScaleAnimationAnimation(1,0);

            mFloatMapManager.HideWindow();
            mFloatingManager.updateView(mRelativeLayoutEdit, mParamsEdit);
            mFloatingManager.updateView(mRelativeLayoutMenu, mParamsMenu);

        }
    }

    View.OnClickListener FloatMenuConfigOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // ConfigDialog configDialog = new ConfigDialog(new ConfigDialog.ConfigDialogCallback() {
            //     @Override
            //     public void onOffsetUpdate() {
            //         mFloatPUBGManager.reload();
            //     }

            //     @Override
            //     public void onStartCalibr() {
            //     }
            // });
            // configDialog.mainView(mContext);
        }
    };

    View.OnClickListener FloatMenuDatabaseOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            mFloatMapManager.DBNameList = dbManager.dbControl.loadNameList();
            FloatListAdapter floatListAdapter = new FloatListAdapter(mContext,mFloatMapManager.DBNameList);

            select_listview.setAdapter(floatListAdapter);
            select_listview.setOnItemClickListener(OnItemClickListenerItem);
            floatSelectAlertDialog.Show();
        }
    };

    View.OnClickListener FloatMenuReviseOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mFloatMapManager.isShowEditWindow)
            {
                mFloatMapManager.HideWindow();
            }else{
                mFloatMapManager.ShowWindow();
            }
        }
    };

    View.OnClickListener FloatMenuSaveOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mFloatMapManager.SaveMap();
        }
    };

    View.OnClickListener FloatMenuAddOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mFloatMapManager.isShowEditWindow) {
                mFloatMapManager.creatNew();
            }
        }
    };

    private AdapterView.OnItemClickListener OnItemClickListenerItem = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            DialogShow(mFloatMapManager.DBNameList.get(i));
            floatSelectAlertDialog.Cancel();
        }
    };


    private void DialogShow(final String DialogSelectName){


        final String items[] = {"使用", "查看", "删除"};
        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle("选择对映射 " + DialogSelectName + " 的操作")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which)
                        {
                            case 0:
                                cb.ChoseName(DialogSelectName);
                                break;
                            case 1:
                                mFloatMapManager.ShowWindow();
                                mFloatMapManager.removeAll();
                                mFloatMapManager.showAll(DialogSelectName);

                                break;
                            case 2:

                                AlertDialog dialog2 = new AlertDialog.Builder(mContext).setTitle("确认删除\"" + DialogSelectName + "\"?")
                                        .setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                mFloatMapManager.removeAll();
                                                dbManager.dbControl.deleteName(DialogSelectName);
                                            }
                                        }).create();
                                if (Build.VERSION.SDK_INT >= 26) {//8.0新特性
                                    dialog2.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
                                }else{
                                    dialog2.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                                }

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
        if (Build.VERSION.SDK_INT>=26) {//8.0新特性
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        }else{
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }

        dialog.show();
    }

}
