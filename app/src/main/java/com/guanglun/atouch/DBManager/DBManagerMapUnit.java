package com.guanglun.atouch.DBManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.guanglun.atouch.Floating.FloatService;

import java.util.List;

public class DBManagerMapUnit {

    private Context mContext;
    private ListView mListView;
    private List<String> name_list;
    private DBControlMapUnit dbControlMapUnit;
    private ArrayAdapter<String> adapter;
    private DBManagerMapUnitCallBack cb;
    private String DEBUG_TAG = "DBManagerMapUnit";

    public interface DBManagerMapUnitCallBack {
        void on_update_use_table_now(String Name);
    }

    public DBManagerMapUnit(Context context, ListView listview, DBManagerMapUnitCallBack cb)
    {
        mContext = context;
        mListView = listview;
        this.cb = cb;

        mListView.setOnItemClickListener(OnItemClickListenerItem);

        dbControlMapUnit = new DBControlMapUnit(mContext);

    }

    public void LoadTableList()
    {
        name_list = dbControlMapUnit.loadNameList();

        adapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_list_item_1,name_list);
        mListView.setAdapter(adapter);                          //设置适配器
    }

    private AdapterView.OnItemClickListener OnItemClickListenerItem = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            DialogShow(name_list.get(i));
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

                                                dbControlMapUnit.deleteName(SelectName);
                                                LoadTableList();
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
