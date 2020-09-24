package com.guanglun.atouch.DBManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBControlMapUnit {

    private String TableName = "MapUnit";
    private String DEBUG_TAG = "DBControlMapUnit";

    private SQLiteDatabase database = null;

    public DBControlMapUnit(Context context)   // 创建或者打开数据库
    {
        SQLdm s = new SQLdm();
        database = s.openDatabase(context);
    }

    public void insertDatabase(MapUnit map)   // 向数据库中插入新数据
    {
        if(database == null)
        {
            return;
        }

        try{
            database.insert(TableName, null, map.getContentValues());
        }catch(SQLException e){
            Log.i(DEBUG_TAG, e.toString());
        }
    }

    public List<String> loadNameList()
    {
        List<String> NameList = new ArrayList<String>();
        Cursor cursor = database.rawQuery("select Name from " + TableName, null);
        while(cursor.moveToNext()){

            String name = cursor.getString(0);
            boolean isAdd = true;

            for(String n : NameList){
                if(n.equals(name))
                {
                    isAdd = false;
                }
            }

            if(isAdd && name != null)
            {
                NameList.add(name);
            }
        }
        return NameList;
    }

    public List<MapUnit> getRawByName(String Name)
    {
        List<MapUnit> MapList = new ArrayList<MapUnit>();

        Cursor cursor ;

        if(database == null)
        {
            return null;
        }

        try{


            cursor = database.query(TableName,null,"Name =?",new String[]{Name},null,null,null);
            if(cursor.getCount() == 0)
                return null;

            cursor.moveToFirst();

            do{
                    MapUnit map = new MapUnit();
                    map.Name = cursor.getString(cursor.getColumnIndex("Name"));
                    map.DeviceValue = cursor.getInt(cursor.getColumnIndex("DeviceValue"));
                    map.KeyCode = cursor.getInt(cursor.getColumnIndex("KeyCode"));
                    map.MFV = cursor.getInt(cursor.getColumnIndex("MFV"));
                    map.FV0 = cursor.getInt(cursor.getColumnIndex("FV0"));
                    map.FV1 = cursor.getInt(cursor.getColumnIndex("FV1"));
                    map.FV2 = cursor.getInt(cursor.getColumnIndex("FV2"));
                    map.FV3 = cursor.getInt(cursor.getColumnIndex("FV3"));
                    map.Config = cursor.getString(cursor.getColumnIndex("Config"));

                MapList.add(map);
            }while(cursor.moveToNext());

            cursor.close(); // 记得关闭游标对象

        }catch(SQLException e){

            Log.i(DEBUG_TAG, e.toString());

            return null;
        }

        if(MapList.size() == 0)
            return null;

        return MapList;
    }

    public boolean clearTable()
    {
        if(database == null)
        {
            return false;
        }

        try{

            database.execSQL("delete from " + TableName);

        }catch(SQLException e){
            Log.i(DEBUG_TAG, e.toString());
            return false;
        }

        return true;

    }

    public void insertList(List<MapUnit> mapList)
    {
        for(MapUnit map:mapList)
        {
            insertDatabase(map);
        }
    }

    public boolean deleteName(String DeleteName)
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

    public void printfTable()   // 查询数据库中的数据
    {
        if(database == null)
        {
            return;
        }

        Cursor cursor ;
        StringBuilder str = new StringBuilder();
        str.append(TableName + ":\n");

        try{

            cursor  = database.query(TableName, null, null, null, null, null, null);


            if(cursor.moveToFirst())                                    // 显示数据库的内容
            {
                for(; !cursor.isAfterLast(); cursor.moveToNext())       // 获取查询游标中的数据
                {
                    str.append(cursor.getString(cursor.getColumnIndex("Name")) + "  ");
                    str.append(cursor.getInt(cursor.getColumnIndex("DeviceValue")) + " ");
                    str.append(cursor.getInt(cursor.getColumnIndex("KeyCode")) + " ");
                    str.append(cursor.getInt(cursor.getColumnIndex("MFV")) + " ");
                    str.append(cursor.getInt(cursor.getColumnIndex("FV0")) + " ");
                    str.append(cursor.getInt(cursor.getColumnIndex("FV1")) + " ");
                    str.append(cursor.getInt(cursor.getColumnIndex("FV2")) + " ");
                    str.append(cursor.getInt(cursor.getColumnIndex("FV3")) + " ");
                    str.append(cursor.getString(cursor.getColumnIndex("Config")) + "\n");
                }
            }
            cursor.close(); // 记得关闭游标对象

        }catch(SQLException e){
            Log.i(DEBUG_TAG, e.toString());
        }

        if(str.toString().equals(TableName + ":\n"))
        {
            str.append("数据库为空！");
        }

        Log.i(DEBUG_TAG, str.toString());
    }

}
