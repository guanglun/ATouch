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
    private TextView tv_fv0;
    private CheckBox cb_fv0;


    public DBManagerMapUnit(Context context, FloatMenu mFloatMenu)
    {
        mContext = context;
        this.mFloatMenu = mFloatMenu;

        dbControl = new DBControlMapUnit(mContext);

    }

    private void showSelectDeviceAdapterView()
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
                                break;
                            case 1:
                                showKeyBoardAdapterView();
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

    private void showKeyBoardAdapterView()
    {
        final AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle("操作")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();

        KeyBoardView kbv = new KeyBoardView(mContext, new KeyBoardView.KBCallback() {
            @Override
            public void onClick(Integer value) {

                tv_code.setText(String.valueOf(value));
                tv_device.setText(String.valueOf(MapUnit.DEVICE_VALUE_KEYBOARD));

                KeyBoardCode kbc = mFloatMenu.dbManager.dbControl.getKeyBoardCode(value);
                if(kbc != null) {
                    tv_name.setText(kbc.Name);
                    tv_des.setText(kbc.Description);
                }
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

        tv_code.setText(String.valueOf(map.KeyCode));
        tv_name.setText(map.KeyName);
        tv_des.setText(map.Describe);
        tv_device.setText(String.valueOf(map.DeviceValue));
        tv_mfv.setText(String.valueOf(map.MFV));
        tv_fv0.setText(String.valueOf(map.FV0));

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

        bt_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectDeviceAdapterView();

            }
        });

        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle("操作")
                .setView(v)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        map.KeyCode = Integer.parseInt(tv_code.getText().toString());
                        map.DeviceValue = Integer.parseInt(tv_device.getText().toString());
                        map.FV0 = Integer.parseInt(tv_fv0.getText().toString());

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

    private AdapterView.OnItemClickListener OnItemClickListenerItem = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {


        }
    };


}
