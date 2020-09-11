package com.guanglun.atouch.DBManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
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
    private String DEBUG_TAG = "DBManager";

    public interface DBManagerCallBack {
        void on_update_use_table_now(String Name);
    }

    public DBManager(Context context,ListView listview,DBManagerCallBack cb)
    {
        mContext = context;
        mListView = listview;
        this.cb = cb;

        mListView.setOnItemClickListener(OnItemClickListenerItem);

        dbControl_pubg = new DBControlPUBG(mContext);

        //dbControl_pubg.PrintfTable();



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
                                //List<KeyMouse> list = dbControl.LoadTableDatabaseList(SelectName);
                                cb.on_update_use_table_now(SelectName);
                                break;
                            case "修改":

                                Intent intent = new Intent(mContext, FloatService.class);
                                intent.putExtra(FloatService.ACTION, FloatService.SHOW);
                                intent.putExtra("SelectName", SelectName);
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

    public byte[] GetByteFromPUBG(String Name)
    {
        byte[] buf = new byte[54 * 2];
        int i = 0;

        PUBG pubg = dbControl_pubg.GetRawByName(Name);

        buf[i++] = (byte)(pubg.N3_AttackX>>8);
        buf[i++] = (byte)(pubg.N3_AttackX);
        buf[i++] = (byte)(pubg.N4_AttackY>>8);
        buf[i++] = (byte)(pubg.N4_AttackY);
        buf[i++] = (byte)(pubg.N5_MoveX>>8);
        buf[i++] = (byte)(pubg.N5_MoveX);
        buf[i++] = (byte)(pubg.N6_MoveY>>8);
        buf[i++] = (byte)(pubg.N6_MoveY);
        buf[i++] = (byte)(pubg.N7_JumpX>>8);
        buf[i++] = (byte)(pubg.N7_JumpX);
        buf[i++] = (byte)(pubg.N8_JumpY>>8);
        buf[i++] = (byte)(pubg.N8_JumpY);
        buf[i++] = (byte)(pubg.N9_SquatX>>8);
        buf[i++] = (byte)(pubg.N9_SquatX);
        buf[i++] = (byte)(pubg.N10_SquatY>>8);
        buf[i++] = (byte)(pubg.N10_SquatY);
        buf[i++] = (byte)(pubg.N11_LieX>>8);
        buf[i++] = (byte)(pubg.N11_LieX);
        buf[i++] = (byte)(pubg.N12_LieY>>8);
        buf[i++] = (byte)(pubg.N12_LieY);
        buf[i++] = (byte)(pubg.N13_FaceX>>8);
        buf[i++] = (byte)(pubg.N13_FaceX);
        buf[i++] = (byte)(pubg.N14_FaceY>>8);
        buf[i++] = (byte)(pubg.N14_FaceY);
        buf[i++] = (byte)(pubg.N15_WatchX>>8);
        buf[i++] = (byte)(pubg.N15_WatchX);
        buf[i++] = (byte)(pubg.N16_WatchY>>8);
        buf[i++] = (byte)(pubg.N16_WatchY);
        buf[i++] = (byte)(pubg.N17_PackageX>>8);
        buf[i++] = (byte)(pubg.N17_PackageX);
        buf[i++] = (byte)(pubg.N18_PackageY>>8);
        buf[i++] = (byte)(pubg.N18_PackageY);
        buf[i++] = (byte)(pubg.N19_ArmsLeftX>>8);
        buf[i++] = (byte)(pubg.N19_ArmsLeftX);
        buf[i++] = (byte)(pubg.N20_ArmsLeftY>>8);
        buf[i++] = (byte)(pubg.N20_ArmsLeftY);
        buf[i++] = (byte)(pubg.N21_ArmsRightX>>8);
        buf[i++] = (byte)(pubg.N21_ArmsRightX);
        buf[i++] = (byte)(pubg.N22_ArmsRightY>>8);
        buf[i++] = (byte)(pubg.N22_ArmsRightY);
        buf[i++] = (byte)(pubg.N23_MapX>>8);
        buf[i++] = (byte)(pubg.N23_MapX);
        buf[i++] = (byte)(pubg.N24_MapY>>8);
        buf[i++] = (byte)(pubg.N24_MapY);

        buf[i++] = (byte)(pubg.N25_AimX>>8);
        buf[i++] = (byte)(pubg.N25_AimX);
        buf[i++] = (byte)(pubg.N26_AimY>>8);
        buf[i++] = (byte)(pubg.N26_AimY);
        buf[i++] = (byte)(pubg.N27_CheckPackageX>>8);
        buf[i++] = (byte)(pubg.N27_CheckPackageX);
        buf[i++] = (byte)(pubg.N28_CheckPackageY>>8);
        buf[i++] = (byte)(pubg.N28_CheckPackageY);
        buf[i++] = (byte)(pubg.N29_DoorX>>8);
        buf[i++] = (byte)(pubg.N29_DoorX);
        buf[i++] = (byte)(pubg.N30_DoorY>>8);
        buf[i++] = (byte)(pubg.N30_DoorY);
        buf[i++] = (byte)(pubg.N31_DriveX>>8);
        buf[i++] = (byte)(pubg.N31_DriveX);
        buf[i++] = (byte)(pubg.N32_DriveY>>8);
        buf[i++] = (byte)(pubg.N32_DriveY);
        buf[i++] = (byte)(pubg.N33_GetOffX>>8);
        buf[i++] = (byte)(pubg.N33_GetOffX);
        buf[i++] = (byte)(pubg.N34_GetOffY>>8);
        buf[i++] = (byte)(pubg.N34_GetOffY);
        buf[i++] = (byte)(pubg.N35_GrenadeX>>8);
        buf[i++] = (byte)(pubg.N35_GrenadeX);
        buf[i++] = (byte)(pubg.N36_GrenadeY>>8);
        buf[i++] = (byte)(pubg.N36_GrenadeY);
        buf[i++] = (byte)(pubg.N37_MedicineX>>8);
        buf[i++] = (byte)(pubg.N37_MedicineX);
        buf[i++] = (byte)(pubg.N38_MedicineY>>8);
        buf[i++] = (byte)(pubg.N38_MedicineY);
        buf[i++] = (byte)(pubg.N39_ReloadX>>8);
        buf[i++] = (byte)(pubg.N39_ReloadX);
        buf[i++] = (byte)(pubg.N40_ReloadY>>8);
        buf[i++] = (byte)(pubg.N40_ReloadY);
        buf[i++] = (byte)(pubg.N41_SaveX>>8);
        buf[i++] = (byte)(pubg.N41_SaveX);
        buf[i++] = (byte)(pubg.N42_SaveY>>8);
        buf[i++] = (byte)(pubg.N42_SaveY);
        buf[i++] = (byte)(pubg.N43_SprintX>>8);
        buf[i++] = (byte)(pubg.N43_SprintX);
        buf[i++] = (byte)(pubg.N44_SprintY>>8);
        buf[i++] = (byte)(pubg.N44_SprintY);
        buf[i++] = (byte)(pubg.N45_FollowX>>8);
        buf[i++] = (byte)(pubg.N45_FollowX);
        buf[i++] = (byte)(pubg.N46_FollowY>>8);
        buf[i++] = (byte)(pubg.N46_FollowY);
        buf[i++] = (byte)(pubg.N47_PickX>>8);
        buf[i++] = (byte)(pubg.N47_PickX);
        buf[i++] = (byte)(pubg.N48_PickY>>8);
        buf[i++] = (byte)(pubg.N48_PickY);
        buf[i++] = (byte)(pubg.N49_RideX>>8);
        buf[i++] = (byte)(pubg.N49_RideX);
        buf[i++] = (byte)(pubg.N50_RideY>>8);
        buf[i++] = (byte)(pubg.N50_RideY);
        buf[i++] = (byte)(pubg.N51_Pick1X>>8);
        buf[i++] = (byte)(pubg.N51_Pick1X);
        buf[i++] = (byte)(pubg.N52_Pick1Y>>8);
        buf[i++] = (byte)(pubg.N52_Pick1Y);
        buf[i++] = (byte)(pubg.N53_Pick2X>>8);
        buf[i++] = (byte)(pubg.N53_Pick2X);
        buf[i++] = (byte)(pubg.N54_Pick2Y>>8);
        buf[i++] = (byte)(pubg.N54_Pick2Y);
        buf[i++] = (byte)(pubg.N55_Pick3X>>8);
        buf[i++] = (byte)(pubg.N55_Pick3X);
        buf[i++] = (byte)(pubg.N56_Pick3Y>>8);
        buf[i++] = (byte)(pubg.N56_Pick3Y);

        //Log.i("SetAttack", String.valueOf(buf[0])+" "+String.valueOf(buf[1])+" "+String.valueOf(buf[2])+" "+String.valueOf(buf[3]));
        //Log.i(DEBUG_TAG, "AttackX:"+pubg.N3_AttackX+" AttackY:"+pubg.N4_AttackY);
        return buf;
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
