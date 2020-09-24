package com.guanglun.atouch.DBManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.guanglun.atouch.Floating.FloatService;
import com.guanglun.atouch.R;

import java.util.List;

public class DBManagerMapUnit {

    private Context mContext;
    public DBControlMapUnit dbControl;
    private ArrayAdapter<String> adapter;
    private DBManagerMapUnitCallBack cb;
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

    public void showDialogListByName(String Name)
    {
        List<MapUnit> list = dbControl.getRawByName(Name);

        ListView mListView = new ListView(mContext);
        MapAdapter mapa = new MapAdapter(mContext, R.layout.map_item_layout,list);
        mListView.setAdapter(mapa);

        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle("选择操作 "+Name)
                .setView(mListView)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("添加", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

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
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            //DialogShow(name_list.get(i));
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
