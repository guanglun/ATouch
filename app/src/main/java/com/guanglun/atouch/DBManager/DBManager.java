package com.guanglun.atouch.DBManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.guanglun.atouch.Floating.FloatService;
import com.guanglun.atouch.R;

import java.util.List;

public class DBManager {

    private Context mContext;
    private ListView mListView;
    private List<String> table_list;
    private DBControl dbControl;
    private ArrayAdapter<String> adapter;
    private DBManagerCallBack cb;

    public interface DBManagerCallBack {
        void on_update_use_table_now(String table,List<KeyMouse> list);
    }

    public DBManager(Context context,ListView listview,DBManagerCallBack cb)
    {
        mContext = context;
        mListView = listview;
        this.cb = cb;

        mListView.setOnItemClickListener(OnItemClickListenerItem);

        dbControl = new DBControl(mContext);

        List<KeyMouse> keyMouseList = dbControl.LoadTableDatabaseList(DatabaseStatic.TABLE_NAME);

        //KeyMouse ky = new KeyMouse(1,"测试","一个测试用例");
        //dbControl.CreatTable("你好");
        //dbControl.InsertDatabase("ttttttt",ky);
        //keyMouseList = dbControl.LoadTableDatabaseList("ttttttt");


        LoadTableList();
    }

    public void LoadTableList()
    {
        table_list = dbControl.LoadTableList();

        adapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_list_item_1,table_list);
        mListView.setAdapter(adapter);//设置适配器
    }

    private AdapterView.OnItemClickListener OnItemClickListenerItem = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            String SelectTable = table_list.get(i);
            DialogShow(SelectTable);
        }
    };

    private void DialogShow(final String SelectTable){

        final String items[] = {"使用", "修改", "删除"};
        AlertDialog dialog = new AlertDialog.Builder(mContext)
                //.setIcon(R.mipmap.icon)//设置标题的图片
                .setTitle("选择对\""+SelectTable+"\"的操作")//设置对话框的标题
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(MainActivity.this, items[which], Toast.LENGTH_SHORT).show();

                        switch (items[which])
                        {
                            case "使用":
                                List<KeyMouse> list = dbControl.LoadTableDatabaseList(SelectTable);
                                cb.on_update_use_table_now(SelectTable,list);
                                break;
                            case "修改":

                                Intent intent = new Intent(mContext, FloatService.class);
                                intent.putExtra(FloatService.ACTION, FloatService.SHOW);
                                intent.putExtra("TableName", SelectTable);
                                mContext.startService(intent);

                                break;
                            case "删除":

                                AlertDialog dialog2 = new AlertDialog.Builder(mContext).setTitle("确认删除\""+SelectTable+"\"?")
                                        .setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                dbControl.DeleteTable(SelectTable);
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
