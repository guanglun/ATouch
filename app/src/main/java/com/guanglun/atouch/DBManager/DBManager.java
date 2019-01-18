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
    private List<String> name_list;
    private DBControl dbControl;
    private DBControlPUBG dbControl_pubg;
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

        dbControl_pubg = new DBControlPUBG(mContext);

        PUBG pubg = new PUBG();
        pubg.SetName("Hell1");
        pubg.SetDescription("这是一个测试用的行");
        //dbControl_pubg.InsertDatabase(pubg);
        //dbControl_pubg.ClearTable();
        dbControl_pubg.LoadNameList();
        dbControl_pubg.PrintfTable();



        //KeyMouse ky = new KeyMouse(1,"测试","一个测试用例");
        //dbControl.CreatTable("你好");
        //dbControl.InsertDatabase("ttttttt",ky);
        //keyMouseList = dbControl.LoadTableDatabaseList("ttttttt");


        //LoadTableList();
    }

    public void LoadTableList()
    {
        name_list = dbControl_pubg.LoadNameList();

        adapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_list_item_1,name_list);
        mListView.setAdapter(adapter);//设置适配器
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
                                List<KeyMouse> list = dbControl.LoadTableDatabaseList(SelectName);
                                cb.on_update_use_table_now(SelectName,list);
                                break;
                            case "修改":

                                Intent intent = new Intent(mContext, FloatService.class);
                                intent.putExtra(FloatService.ACTION, FloatService.SHOW);
                                intent.putExtra("TableName", SelectName);
                                mContext.startService(intent);

                                break;
                            case "删除":

                                AlertDialog dialog2 = new AlertDialog.Builder(mContext).setTitle("确认删除\""+SelectName+"\"?")
                                        .setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                dbControl_pubg.DeleteRaw(SelectName);
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


    public byte[] GetUseTable(List<KeyMouse> list)
    {
        byte[] buf = new byte[list.size()*5];

        for(int i=0;i<list.size();i++)
        {
            buf[i*5] = (byte)list.get(i).KeyCode;
            buf[i*5+1] = (byte)(list.get(i).PX>>8);
            buf[i*5+2] = (byte)(list.get(i).PX);
            buf[i*5+3] = (byte)(list.get(i).PY>>8);
            buf[i*5+4] = (byte)(list.get(i).PY);
        }

        return buf;
    }
}
