package com.guanglun.atouch.DBManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.guanglun.atouch.Floating.FloatService;
import com.guanglun.atouch.Floating.KeyBoardView;
import com.guanglun.atouch.R;
import com.guanglun.atouch.upgrade.AppDownloadManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class DBManagerMapUnit {

    private Context mContext;
    public DBControlMapUnit dbControl;
    private ArrayAdapter<String> adapter;
    private DBManagerMapUnitCallBack cb;
    private List<MapUnit> map_list = new ArrayList<MapUnit>();
    private MapAdapter mapa;
    private String DEBUG_TAG = "DBManagerMapUnit";

    public interface DBManagerMapUnitCallBack {
        void on_update_use_table_now(String Name);
    }

    public DBManagerMapUnit(Context context)
    {
        mContext = context;

        this.cb = cb;

        dbControl = new DBControlMapUnit(mContext);

    }

    public void showDialogListByName(final String Name)
    {
        if(Name.equals("新建映射"))
        {
            map_list = new ArrayList<MapUnit>();
        }else{
            map_list = dbControl.getRawByName(Name);

        }

        ListView mListView = new ListView(mContext);
        mapa = new MapAdapter(mContext, R.layout.map_item_layout,map_list);
        mListView.setAdapter(mapa);
        mListView.setOnItemClickListener(OnItemClickListenerItem);

        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle("正在操作 " + Name)
                .setView(mListView)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(Name.equals("新建映射") || dbControl.getRawByName(Name) == null)
                        {
                            final EditText editText = new EditText(mContext);
                            AlertDialog dialog2 = new AlertDialog.Builder(mContext)
                                    .setTitle("请输入新的映射名称")
                                    .setView(editText)
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String NewName = editText.getText().toString();
                                            if(NewName.length() > 0)
                                            {
                                                if(dbControl.getRawByName(NewName) != null)
                                                {
                                                    Toast.makeText(mContext,"名称已存在",Toast.LENGTH_SHORT);
                                                }else{
                                                    for(MapUnit map:map_list)
                                                    {
                                                        map.Name = editText.getText().toString();
                                                    }
                                                    dbControl.insertList(map_list);
                                                }
                                            }else{
                                                Toast.makeText(mContext,"名称不能为空",Toast.LENGTH_SHORT);
                                            }
                                        }
                                    }).create();
                            if (Build.VERSION.SDK_INT>=26) {//8.0新特性
                                dialog2.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
                            }else{
                                dialog2.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                            }
                            dialog2.show();
                        }else{
                            dbControl.deleteName(Name);
                            dbControl.insertList(map_list);
                        }

                        dialog.dismiss();
                    }
                }).setNeutralButton("添加", null)
                .create();
        dialog.setCancelable(false);
        if (Build.VERSION.SDK_INT>=26) {//8.0新特性
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        }else{
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMapAdapterView(true,0);
            }
        });
    }

    private void showMapAdapterView(final boolean isNew, final int i)
    {
        final MapUnit map = new MapUnit();
        View v = View.inflate(mContext, R.layout.map_layout, null);

        Button bt_kb = ((Button)v.findViewById(R.id.bt_kb));
        Button bt_joystick = ((Button)v.findViewById(R.id.bt_joystick));
        Button bt_mouse = ((Button)v.findViewById(R.id.bt_mouse));
        final TextView tv_code = ((TextView)v.findViewById(R.id.tv_code));

        if(isNew)
        {
            tv_code.setText(String.valueOf(map.KeyCode));
        }else{
            tv_code.setText(String.valueOf(map_list.get(i).KeyCode));
        }


        bt_kb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
        });

        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle("操作")
                .setView(v)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(isNew)
                        {
                            map.setKeyCode(Integer.parseInt(tv_code.getText().toString()));
                            map_list.add(map);
                        }else{
                            map_list.get(i).setKeyCode(Integer.parseInt(tv_code.getText().toString()));
                        }

                        mapa.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();

        dialog.setCancelable(false);                                        // 设置是否可以通过点击Back键取消
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
            showMapAdapterView(false,i);

        }
    };

    private void DialogShow(final String SelectName){

        final String items[] = {"使用", "修改", "删除"};
        AlertDialog dialog = new AlertDialog.Builder(mContext)
                //.setIcon(R.mipmap.icon)//设置标题的图片
                .setTitle("选择对\""+SelectName+"\"的操作")//设置对话框的标题
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(MainActivity.this, items[which], Toast.LENGTH_SHORT).show();

                        switch (items[which])
                        {
                            case "使用":
                                //List<KeyMouse> list = dbControl.LoadTableDatabaseList(SelectName);
                                //cb.on_update_use_table_now(SelectName);
                                break;
                            case "修改":

//                                Intent intent = new Intent(mContext, FloatService.class);
//                                intent.putExtra(FloatService.ACTION, FloatService.SHOW);
//                                intent.putExtra("SelectName", SelectName);
//                                mContext.startService(intent);

                                break;
                            case "删除":

                                AlertDialog dialog2 = new AlertDialog.Builder(mContext).setTitle("确认删除\""+SelectName+"\"?")
                                        .setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                dbControl.deleteName(SelectName);
                                            }
                                        }).create();
                                //dialog2.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
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
        dialog.show();
    }


}
