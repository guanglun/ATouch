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
                Log.i(DEBUG_TAG, name);
                NameList.add(name);
            }

        }

        return NameList;

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
