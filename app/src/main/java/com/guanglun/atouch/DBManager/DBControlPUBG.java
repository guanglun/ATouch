package com.guanglun.atouch.DBManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBControlPUBG {

    private String TableName = "GAME_PUBG";
    private String DEBUG_TAG = "DBControlPUBG";

    private SQLiteDatabase database = null;

    public DBControlPUBG(Context context)   // 创建或者打开数据库
    {
        SQLdm s = new SQLdm();
        database = s.openDatabase(context);
    }

    public ContentValues GetContentValues(PUBG pubg)
    {
        ContentValues cV = new ContentValues();

        cV.put("Name",pubg.N1_Name);
        cV.put("Description",pubg.N2_Description);

        cV.put("AttackX",pubg.N3_AttackX);
        cV.put("AttackY",pubg.N4_AttackY);

        cV.put("MoveX",pubg.N5_MoveX);
        cV.put("MoveY",pubg.N6_MoveY);

        cV.put("JumpX",pubg.N7_JumpX);
        cV.put("JumpY",pubg.N8_JumpY);

        cV.put("SquatX",pubg.N9_SquatX);
        cV.put("SquatY",pubg.N10_SquatY);

        cV.put("LieX",pubg.N11_LieX);
        cV.put("LieY",pubg.N12_LieY);

        cV.put("FaceX",pubg.N13_FaceX);
        cV.put("FaceY",pubg.N14_FaceY);

        cV.put("WatchX",pubg.N15_WatchX);
        cV.put("WatchY",pubg.N16_WatchY);

        cV.put("PackageX",pubg.N17_PackageX);
        cV.put("PackageY",pubg.N18_PackageY);

        cV.put("ArmsLeftX",pubg.N19_ArmsLeftX);
        cV.put("ArmsLeftY",pubg.N20_ArmsLeftY);

        cV.put("ArmsRightX",pubg.N21_ArmsRightX);
        cV.put("ArmsRightY",pubg.N22_ArmsRightY);

        cV.put("MapX",pubg.N23_MapX);
        cV.put("MapY",pubg.N24_MapY);

        cV.put("AimX",pubg.N25_AimX);
        cV.put("AimY",pubg.N26_AimY);

        cV.put("CheckPackageX",pubg.N27_CheckPackageX);
        cV.put("CheckPackageY",pubg.N28_CheckPackageY);

        cV.put("DoorX",pubg.N29_DoorX);
        cV.put("DoorY",pubg.N30_DoorY);

        cV.put("DriveX",pubg.N31_DriveX);
        cV.put("DriveY",pubg.N32_DriveY);

        cV.put("GetOffX",pubg.N33_GetOffX);
        cV.put("GetOffY",pubg.N34_GetOffY);

        cV.put("GrenadeX",pubg.N35_GrenadeX);
        cV.put("GrenadeY",pubg.N36_GrenadeY);

        cV.put("MedicineX",pubg.N37_MedicineX);
        cV.put("MedicineY",pubg.N38_MedicineY);

        cV.put("ReloadX",pubg.N39_ReloadX);
        cV.put("ReloadY",pubg.N40_ReloadY);

        cV.put("SaveX",pubg.N41_SaveX);
        cV.put("SaveY",pubg.N42_SaveY);

        cV.put("SprintX",pubg.N43_SprintX);
        cV.put("SprintY",pubg.N44_SprintY);

        cV.put("FollowX",pubg.N45_FollowX);
        cV.put("FollowY",pubg.N46_FollowY);

        cV.put("PickX",pubg.N47_PickX);
        cV.put("PickY",pubg.N48_PickY);

        cV.put("RideX",pubg.N49_RideX);
        cV.put("RideY",pubg.N50_RideY);

        cV.put("Pick1X",pubg.N51_Pick1X);
        cV.put("Pick1Y",pubg.N52_Pick1Y);

        cV.put("Pick2X",pubg.N53_Pick2X);
        cV.put("Pick2Y",pubg.N54_Pick2Y);

        cV.put("Pick3X",pubg.N55_Pick3X);
        cV.put("Pick3Y",pubg.N56_Pick3Y);

        return cV;
    }

    public void InsertDatabase(PUBG pubg)   // 向数据库中插入新数据
    {
        if(database == null)
        {
            return;
        }


        try{

            database.insert(TableName, null, GetContentValues(pubg));

        }catch(SQLException e){
            Log.i(DEBUG_TAG, e.toString());
        }
    }

    public List<String> LoadNameList()
    {
        List<String> NameList = new ArrayList<String>();
        Cursor cursor = database.rawQuery("select Name from "+TableName, null);
        while(cursor.moveToNext()){
            //遍历出表名
            String name = cursor.getString(0);

            //if(!name.equals("sqlite_sequence") && !name.equals("android_metadata")&& !name.equals("mousekey"))
            {
                //Log.i(DEBUG_TAG, name);
                NameList.add(name);
            }

        }

        return NameList;
    }

    public PUBG GetRawByName(String Name)
    {

        PUBG pubg = new PUBG();
        Cursor cursor ;

        if(database == null)
        {
            return null;
        }

        try{

            cursor = database.query(TableName,null,"Name =?",new String[]{Name},null,null,null);

            if(cursor.moveToFirst())    // 显示数据库的内容
            {
                pubg.SetName(cursor.getString(cursor.getColumnIndex("Name")));
                pubg.SetDescription(cursor.getString(cursor.getColumnIndex("Description")));
                pubg.SetAttack(cursor.getInt(cursor.getColumnIndex("AttackX")),cursor.getInt(cursor.getColumnIndex("AttackY")));
                pubg.SetMove(cursor.getInt(cursor.getColumnIndex("MoveX")),cursor.getInt(cursor.getColumnIndex("MoveY")));
                pubg.SetJump(cursor.getInt(cursor.getColumnIndex("JumpX")),cursor.getInt(cursor.getColumnIndex("JumpY")));
                pubg.SetSquat(cursor.getInt(cursor.getColumnIndex("SquatX")),cursor.getInt(cursor.getColumnIndex("SquatY")));
                pubg.SetLie(cursor.getInt(cursor.getColumnIndex("LieX")),cursor.getInt(cursor.getColumnIndex("LieY")));
                pubg.SetFace(cursor.getInt(cursor.getColumnIndex("FaceX")),cursor.getInt(cursor.getColumnIndex("FaceY")));
                pubg.SetWatch(cursor.getInt(cursor.getColumnIndex("WatchX")),cursor.getInt(cursor.getColumnIndex("WatchY")));
                pubg.SetPackage(cursor.getInt(cursor.getColumnIndex("PackageX")),cursor.getInt(cursor.getColumnIndex("PackageY")));
                pubg.SetArmsLeft(cursor.getInt(cursor.getColumnIndex("ArmsLeftX")),cursor.getInt(cursor.getColumnIndex("ArmsLeftY")));
                pubg.SetArmsRight(cursor.getInt(cursor.getColumnIndex("ArmsRightX")),cursor.getInt(cursor.getColumnIndex("ArmsRightY")));
                pubg.SetMap(cursor.getInt(cursor.getColumnIndex("MapX")),cursor.getInt(cursor.getColumnIndex("MapY")));

                pubg.SetAim(cursor.getInt(cursor.getColumnIndex("AimX")),cursor.getInt(cursor.getColumnIndex("AimY")));
                pubg.SetCheckPackage(cursor.getInt(cursor.getColumnIndex("CheckPackageX")),cursor.getInt(cursor.getColumnIndex("CheckPackageY")));
                pubg.SetDoor(cursor.getInt(cursor.getColumnIndex("DoorX")),cursor.getInt(cursor.getColumnIndex("DoorY")));
                pubg.SetDrive(cursor.getInt(cursor.getColumnIndex("DriveX")),cursor.getInt(cursor.getColumnIndex("DriveY")));
                pubg.SetGetOff(cursor.getInt(cursor.getColumnIndex("GetOffX")),cursor.getInt(cursor.getColumnIndex("GetOffY")));
                pubg.SetGrenade(cursor.getInt(cursor.getColumnIndex("GrenadeX")),cursor.getInt(cursor.getColumnIndex("GrenadeY")));
                pubg.SetMedicine(cursor.getInt(cursor.getColumnIndex("MedicineX")),cursor.getInt(cursor.getColumnIndex("MedicineY")));
                pubg.SetReload(cursor.getInt(cursor.getColumnIndex("ReloadX")),cursor.getInt(cursor.getColumnIndex("ReloadY")));
                pubg.SetSave(cursor.getInt(cursor.getColumnIndex("SaveX")),cursor.getInt(cursor.getColumnIndex("SaveY")));
                pubg.SetSprint(cursor.getInt(cursor.getColumnIndex("SprintX")),cursor.getInt(cursor.getColumnIndex("SprintY")));
                pubg.SetFollow(cursor.getInt(cursor.getColumnIndex("FollowX")),cursor.getInt(cursor.getColumnIndex("FollowY")));
                pubg.SetPick(cursor.getInt(cursor.getColumnIndex("PickX")),cursor.getInt(cursor.getColumnIndex("PickY")));
                pubg.SetRide(cursor.getInt(cursor.getColumnIndex("RideX")),cursor.getInt(cursor.getColumnIndex("RideY")));
                pubg.SetPick1(cursor.getInt(cursor.getColumnIndex("Pick1X")),cursor.getInt(cursor.getColumnIndex("Pick1Y")));
                pubg.SetPick2(cursor.getInt(cursor.getColumnIndex("Pick2X")),cursor.getInt(cursor.getColumnIndex("Pick2Y")));
                pubg.SetPick3(cursor.getInt(cursor.getColumnIndex("Pick3X")),cursor.getInt(cursor.getColumnIndex("Pick3Y")));
            }

            cursor.close(); // 记得关闭游标对象

        }catch(SQLException e){

            Log.i(DEBUG_TAG, e.toString());

            return null;
        }

        return pubg;
    }

    public boolean ClearTable()
    {
        if(database == null)
        {
            return false;
        }

        try{

            database.execSQL("delete from "+TableName);

        }catch(SQLException e){
            Log.i(DEBUG_TAG, e.toString());
            return false;
        }

        return true;

    }

    public boolean DeleteRaw(String DeleteName)
    {
        if(database == null)
        {
            return false;
        }

        try{

            database.execSQL("delete from " + TableName + " where Name = '" + DeleteName + "'");

        }catch(SQLException e){
            Log.i(DEBUG_TAG, e.toString());
            return false;
        }

        return true;
    }

    public void PrintfTable()   // 查询数据库中的数据
    {
        if(database == null)
        {
            return;
        }

        Cursor cursor ;
        StringBuilder str = new StringBuilder();

        try{

            cursor  = database.query(TableName, null, null, null, null, null, null);


            if(cursor.moveToFirst())    // 显示数据库的内容
            {
                for(; !cursor.isAfterLast(); cursor.moveToNext())   // 获取查询游标中的数据
                {
                    str.append(cursor.getString(cursor.getColumnIndex("Name")) + "  ");
                    str.append(cursor.getString(cursor.getColumnIndex("Description")) + "\n");
                }
            }
            cursor.close(); // 记得关闭游标对象

        }catch(SQLException e){
            Log.i(DEBUG_TAG, e.toString());
        }

        if(str.toString().equals(""))
        {
            str.append("数据库为空！");
        }

        Log.i(DEBUG_TAG, str.toString());
    }

}
