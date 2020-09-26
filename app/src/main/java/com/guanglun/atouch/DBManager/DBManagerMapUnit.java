package com.guanglun.atouch.DBManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.guanglun.atouch.Floating.FloatMenu;
import com.guanglun.atouch.Floating.KeyBoardView;
import com.guanglun.atouch.Floating.MouseView;
import com.guanglun.atouch.R;

import java.util.ArrayList;
import java.util.List;

public class DBManagerMapUnit {

    private Context mContext;
    public DBControlMapUnit dbControl;
    private ArrayAdapter<String> adapter;
    private List<MapUnit> map_list = new ArrayList<MapUnit>();
    private MapAdapter mapa;
    private String DEBUG_TAG = "DBManagerMapUnit";
    private FloatMenu mFloatMenu;

    private TextView tv_code;
    private TextView tv_name;
    private TextView tv_des;
    private TextView tv_device;
    private TextView tv_mfv;
    private TextView tv_fv0,tv_fv1;
    private CheckBox cb_fv0,cb_fv1;

    public interface SelectCallBack {
        void Select(int device,int value);
    }

    public DBManagerMapUnit(Context context, FloatMenu mFloatMenu)
    {
        mContext = context;
        this.mFloatMenu = mFloatMenu;

        dbControl = new DBControlMapUnit(mContext);

    }

    private void showSelectDeviceAdapterView(SelectCallBack scb)
    {
        final String items[] = {"鼠标","键盘","手柄"};
        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle("选择添加的类型")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which)
                        {
                            case 0:
                                showMouseAdapterView(scb);
                                break;
                            case 1:
                                showKeyBoardAdapterView(scb);
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

    private void showMouseAdapterView(SelectCallBack scb)
    {

        final AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle("选择映射的鼠标按键")
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();

        MouseView mv = new MouseView(mContext, new MouseView.MCallback() {
            @Override
            public void onClick(Integer value) {
                scb.Select(MapUnit.DEVICE_VALUE_MOUSE,value);
                dialog.dismiss();

            }
        });
        dialog.setView(mv);
        dialog.setCancelable(false);                                        // 设置是否可以通过点击Back键取消
        if (Build.VERSION.SDK_INT>=26) {//8.0新特性
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        }else{
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        dialog.show();
    }

    private void showKeyBoardAdapterView(SelectCallBack scb)
    {

        final AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle("选择映射的键盘按键")
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();

        KeyBoardView kbv = new KeyBoardView(mContext, new KeyBoardView.KBCallback() {
            @Override
            public void onClick(Integer value) {

                scb.Select(MapUnit.DEVICE_VALUE_KEYBOARD,value);
                dialog.dismiss();

            }
        });
        dialog.setView(kbv);
        dialog.setCancelable(false);                                        // 设置是否可以通过点击Back键取消
        if (Build.VERSION.SDK_INT>=26) {//8.0新特性
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        }else{
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        dialog.show();
    }

    public void showMapAdapterView(MapUnit map)
    {

        View v = View.inflate(mContext, R.layout.map_layout, null);

        Button bt_select = ((Button)v.findViewById(R.id.bt_select));

        tv_code = ((TextView)v.findViewById(R.id.tv_code));
        tv_name = ((TextView)v.findViewById(R.id.tv_name));
        tv_des = ((TextView)v.findViewById(R.id.tv_des));
        tv_device = ((TextView)v.findViewById(R.id.tv_device));
        tv_mfv = ((TextView)v.findViewById(R.id.tv_mfv));
        tv_fv0 = ((TextView)v.findViewById(R.id.tv_fv0));
        cb_fv0 = ((CheckBox)v.findViewById(R.id.cb_fv0));
        tv_fv1 = ((TextView)v.findViewById(R.id.tv_fv1));
        cb_fv1 = ((CheckBox)v.findViewById(R.id.cb_fv1));

        tv_code.setText(String.valueOf(map.KeyCode));
        tv_name.setText(map.KeyName);
        tv_des.setText(map.Describe);
        tv_device.setText(String.valueOf(map.DeviceValue));
        tv_mfv.setText(String.valueOf(map.MFV));
        tv_fv0.setText(String.valueOf(map.FV0));
        tv_fv1.setText(String.valueOf(map.FV1));

        if(map.FV0 == 0)
        {
            cb_fv0.setChecked(false);
        }else{
            cb_fv0.setChecked(true);
        }

        cb_fv0.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    tv_fv0.setText("1");
                }else{
                    tv_fv0.setText("0");
                }
            }
        });

        if(map.FV1 == 0)
        {
            cb_fv1.setChecked(false);
        }else{
            cb_fv1.setChecked(true);
        }

        cb_fv1.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    tv_fv1.setText("1");
                }else{
                    tv_fv1.setText("0");
                }
            }
        });

        bt_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectDeviceAdapterView(new SelectCallBack() {
                    @Override
                    public void Select(int device, int value) {
                        tv_code.setText(String.valueOf(value));
                        tv_device.setText(String.valueOf(device));

                        KeyBoardCode kbc = mFloatMenu.dbManager.dbControl.getKeyBoardCode(value);
                        if(kbc != null) {
                            tv_name.setText(kbc.Name);
                            tv_des.setText(kbc.Description);
                        }
                    }
                });

            }
        });

        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle("普通按键")
                .setView(v)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        map.KeyCode = Integer.parseInt(tv_code.getText().toString());
                        map.DeviceValue = Integer.parseInt(tv_device.getText().toString());
                        map.FV0 = Integer.parseInt(tv_fv0.getText().toString());
                        map.FV1 = Integer.parseInt(tv_fv1.getText().toString());

                        KeyBoardCode kbc = mFloatMenu.dbManager.dbControl.getKeyBoardCode(map.KeyCode);
                        if(kbc != null) {
                            map.bt.setText(kbc.Name);

                            map.KeyName = kbc.Name;
                            map.Describe = kbc.Description;
                        }

                        dialog.dismiss();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setNeutralButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mFloatMenu.mFloatMapManager.maplist.size() == 1)
                        {
                            Toast.makeText(mContext,"无法删除！必须保留一个按键",Toast.LENGTH_SHORT).show();
                        }else{
                            map.bt.Remove();
                            mFloatMenu.mFloatMapManager.maplist.remove(map);
                            Toast.makeText(mContext,"删除成功",Toast.LENGTH_SHORT).show();
                        }

                        dialog.dismiss();
                    }
                }).create();

        dialog.setCancelable(false);
        if (Build.VERSION.SDK_INT>=26) {//8.0新特性
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        }else{
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        dialog.show();
    }

    public void showMapSlideAdapterView(MapUnit map)
    {

        View v = View.inflate(mContext, R.layout.map_slide_layout, null);

        Button bt_top = ((Button)v.findViewById(R.id.bt_top));
        Button bt_left = ((Button)v.findViewById(R.id.bt_left));
        Button bt_right = ((Button)v.findViewById(R.id.bt_right));
        Button bt_back = ((Button)v.findViewById(R.id.bt_back));
        Button bt_sup = ((Button)v.findViewById(R.id.bt_sup));

        if(map.KeyCode != 0)
        {
            bt_top.setText(map.KeyName);
            bt_top.setTag(map.KeyCode );
        }else{
            bt_top.setTag(0);
        }

        if(map.FV2 != 0)
        {
            bt_left.setText(map.FS0);
            bt_left.setTag(map.FV2 );
        }else{
            bt_left.setTag(0);
        }

        if(map.FV3 != 0)
        {
            bt_right.setText(map.FS1);
            bt_right.setTag(map.FV3 );
        }else{
            bt_right.setTag(0);
        }

        if(map.FV4 != 0)
        {
            bt_back.setText(map.FS2);
            bt_back.setTag(map.FV4 );
        }else{
            bt_back.setTag(0);
        }

        if(map.FV5 != 0)
        {
            bt_sup.setText(map.FS3);
            bt_sup.setTag(map.FV5 );
        }else{
            bt_sup.setTag(0);
        }

        bt_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectDeviceAdapterView(new SelectCallBack() {
                    @Override
                    public void Select(int device, int value) {
                        KeyBoardCode kbc = mFloatMenu.dbManager.dbControl.getKeyBoardCode(value);
                        if(kbc != null) {
                            bt_top.setText(kbc.Name);
                            bt_top.setTag(value);
                        }
                    }
                });
            }
        });

        bt_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectDeviceAdapterView(new SelectCallBack() {
                    @Override
                    public void Select(int device, int value) {
                        KeyBoardCode kbc = mFloatMenu.dbManager.dbControl.getKeyBoardCode(value);
                        if(kbc != null) {
                            bt_left.setText(kbc.Name);
                            bt_left.setTag(value);
                        }
                    }
                });
            }
        });
        bt_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectDeviceAdapterView(new SelectCallBack() {
                    @Override
                    public void Select(int device, int value) {
                        KeyBoardCode kbc = mFloatMenu.dbManager.dbControl.getKeyBoardCode(value);
                        if(kbc != null) {
                            bt_right.setText(kbc.Name);
                            bt_right.setTag(value);
                        }
                    }
                });
            }
        });
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectDeviceAdapterView(new SelectCallBack() {
                    @Override
                    public void Select(int device, int value) {
                        KeyBoardCode kbc = mFloatMenu.dbManager.dbControl.getKeyBoardCode(value);
                        if(kbc != null) {
                            bt_back.setText(kbc.Name);
                            bt_back.setTag(value);
                        }
                    }
                });
            }
        });
        bt_sup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectDeviceAdapterView(new SelectCallBack() {
                    @Override
                    public void Select(int device, int value) {
                        KeyBoardCode kbc = mFloatMenu.dbManager.dbControl.getKeyBoardCode(value);
                        if(kbc != null) {
                            bt_sup.setText(kbc.Name);
                            bt_sup.setTag(value);
                        }
                    }
                });
            }
        });

        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle("吃鸡移动滑盘")
                .setView(v)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        map.KeyName = bt_top.getText().toString();
                        map.KeyCode = (Integer) bt_top.getTag();

                        map.FS0 = bt_left.getText().toString();
                        map.FV2 = (Integer) bt_left.getTag();

                        map.FS1 = bt_right.getText().toString();
                        map.FV3 = (Integer) bt_right.getTag();

                        map.FS2 = bt_back.getText().toString();
                        map.FV4 = (Integer) bt_back.getTag();

                        map.FS3 = bt_sup.getText().toString();
                        map.FV5 = (Integer) bt_sup.getTag();

                        //Log.i("ATService",map.toString());
                        dialog.dismiss();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setNeutralButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mFloatMenu.mFloatMapManager.maplist.size() == 1)
                        {
                            Toast.makeText(mContext,"无法删除！必须保留一个按键",Toast.LENGTH_SHORT).show();
                        }else{
                            map.bts.Remove();
                            mFloatMenu.mFloatMapManager.maplist.remove(map);
                            Toast.makeText(mContext,"删除成功",Toast.LENGTH_SHORT).show();
                        }

                        dialog.dismiss();
                    }
                }).create();

        dialog.setCancelable(false);
        if (Build.VERSION.SDK_INT>=26) {//8.0新特性
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        }else{
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        dialog.show();
    }

    public void showMapMouseAdapterView(MapUnit map)
    {

        View v = View.inflate(mContext, R.layout.map_mouse_layout, null);

        Button bt_change = ((Button)v.findViewById(R.id.bt_change));
        Button bt_eye = ((Button)v.findViewById(R.id.bt_eye));

        if(map.KeyCode != 0 ) {
            bt_change.setText(map.KeyName);
            bt_change.setTag(map.KeyCode);
        }else{
            bt_change.setTag(0);
        }

        if(map.FV0 != 0 ) {
            bt_eye.setText(map.FS0);
            bt_eye.setTag(map.FV0);
        }else{
            bt_eye.setTag(0);
        }

        bt_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectDeviceAdapterView(new SelectCallBack() {
                    @Override
                    public void Select(int device, int value) {
                        KeyBoardCode kbc = mFloatMenu.dbManager.dbControl.getKeyBoardCode(value);
                        if(kbc != null) {
                            bt_change.setText(kbc.Name);
                            bt_change.setTag(value);
                        }
                    }
                });
            }
        });
        bt_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectDeviceAdapterView(new SelectCallBack() {
                    @Override
                    public void Select(int device, int value) {
                        KeyBoardCode kbc = mFloatMenu.dbManager.dbControl.getKeyBoardCode(value);
                        if(kbc != null) {
                            bt_eye.setText(kbc.Name);
                            bt_eye.setTag(value);
                        }
                    }
                });
            }
        });
        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle("吃鸡鼠标滑动")
                .setView(v)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                            map.KeyName = bt_change.getText().toString();
                            map.KeyCode = (Integer) bt_change.getTag();

                            map.FS0 = bt_eye.getText().toString();
                            map.FV0 = (Integer) bt_eye.getTag();


                        dialog.dismiss();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setNeutralButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mFloatMenu.mFloatMapManager.maplist.size() == 1)
                        {
                            Toast.makeText(mContext,"无法删除！必须保留一个按键",Toast.LENGTH_SHORT).show();
                        }else{
                            map.btm.Remove();
                            mFloatMenu.mFloatMapManager.maplist.remove(map);
                            Toast.makeText(mContext,"删除成功",Toast.LENGTH_SHORT).show();
                        }

                        dialog.dismiss();
                    }
                }).create();

        dialog.setCancelable(false);
        if (Build.VERSION.SDK_INT>=26) {//8.0新特性
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        }else{
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        dialog.show();
    }


    private AdapterView.OnItemClickListener OnItemClickListenerItem = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {


        }
    };


}
