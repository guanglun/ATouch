package com.guanglun.atouch.DBManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBControl {

    private String DEBUG_TAG = "DBControl";

    private SQLHelper QSLhelper = null;
    private SQLiteDatabase database = null;

    public static String CREATE_TABLE1 = "CREATE TABLE ";
    public static String CREATE_TABLE2 = " (" +
            DatabaseStatic.KEY_MOUSE_ID + " INTEGER PRIMARY KEY," +
            DatabaseStatic.KEY_MOUSE_NAME + " VARCHAR(30)," +
            DatabaseStatic.KEY_MOUSE_DESCRIPTION + " VARCHAR(30)," +
            DatabaseStatic.KEY_MOUSE_KEYCODE + " INT," +
            DatabaseStatic.KEY_MOUSE_PX + " INT," +
            DatabaseStatic.KEY_MOUSE_PY + " INT)";    // 用于创建表的SQL语句

    public DBControl(Context context)   // 创建或者打开数据库
    {
        //QSLhelper = new SQLHelper(context);

        //QSLhelper.getWritableDatabase();

        SQLdm s = new SQLdm();
        database = s.openDatabase(context);
    }

    public ContentValues GetContentValues(KeyMouse keyMouse)
    {
        ContentValues cV = new ContentValues();
        cV.put(DatabaseStatic.KEY_MOUSE_ID,keyMouse.ID);
        cV.put(DatabaseStatic.KEY_MOUSE_NAME, keyMouse.Name);
        cV.put(DatabaseStatic.KEY_MOUSE_DESCRIPTION, keyMouse.Description);
        cV.put(DatabaseStatic.KEY_MOUSE_PX, keyMouse.PX);
        cV.put(DatabaseStatic.KEY_MOUSE_PY, keyMouse.PY);
        return cV;
    }

    public void InsertDatabase(String TableName,KeyMouse keyMouse)   // 向数据库中插入新数据
    {
        if(database == null)
        {
            return;
        }


        //database = QSLhelper.getWritableDatabase();

        try{

            database.insert(TableName, null, GetContentValues(keyMouse));

        }catch(SQLException e){

        }
    }


    public void UpdateDatabase(String TableName,KeyMouse keyMouse)   // 更新数据
    {
        if(database == null)
        {
            return;
        }

        //database = QSLhelper.getWritableDatabase();
        try{
            database.update(TableName, GetContentValues(keyMouse),
                DatabaseStatic.KEY_MOUSE_ID + "= ?", new String[]{String.valueOf(keyMouse.ID)});
        }catch(SQLException e){

        }
    }

    public void DeleteDatabase(String TableName,int id)   // 数据库中删除数据
    {
        if(database == null)
        {
            return;
        }

        //database = QSLhelper.getWritableDatabase();

        try{
            database.delete(TableName, DatabaseStatic.KEY_MOUSE_ID + " = ? ",
                new String[]{String.valueOf(id)});

        }catch(SQLException e){

        }

    }

    public void SearchDatabase(String TableName)   // 查询数据库中的数据
    {
        if(database == null)
        {
            return;
        }

        //database = QSLhelper.getWritableDatabase();

        Cursor cursor ;
        StringBuilder str = new StringBuilder();

        try{

            cursor  = database.query(TableName, null, null, null, null, null, null);


            if(cursor.moveToFirst())    // 显示数据库的内容
            {
                for(; !cursor.isAfterLast(); cursor.moveToNext())   // 获取查询游标中的数据
                {
                    str.append(cursor.getString(cursor.getColumnIndex(DatabaseStatic.KEY_MOUSE_ID)) + "  ");
                    str.append(cursor.getString(cursor.getColumnIndex(DatabaseStatic.KEY_MOUSE_NAME)) + "  ");
                    str.append(cursor.getString(cursor.getColumnIndex(DatabaseStatic.KEY_MOUSE_DESCRIPTION)) + "  ");
                    str.append(cursor.getString(cursor.getColumnIndex(DatabaseStatic.KEY_MOUSE_PX)) + "  ");
                    str.append(cursor.getString(cursor.getColumnIndex(DatabaseStatic.KEY_MOUSE_PY)) + "\n");
                }
            }
            cursor.close(); // 记得关闭游标对象

        }catch(SQLException e){

        }

        if(str.toString().equals(""))
        {
            str.append("数据库为空！");
        }

        Log.i(DEBUG_TAG, str.toString());
    }

    public List<KeyMouse> LoadTableDatabaseList(String TableName)
    {
        if(database == null)
        {
            return null;
        }

        List<KeyMouse> keyMouseList = new ArrayList<KeyMouse>();

        final Cursor cursor;

        try{

            cursor = database.query(TableName, null, null, null, null, null, null);

            if(cursor.moveToFirst())    // 显示数据库的内容
            {
                for(; !cursor.isAfterLast(); cursor.moveToNext())   // 获取查询游标中的数据
                {
                    KeyMouse keyMouse = new KeyMouse();
                    keyMouse.SetID(cursor.getInt(cursor.getColumnIndex(DatabaseStatic.KEY_MOUSE_ID)));
                    keyMouse.SetName(cursor.getString(cursor.getColumnIndex(DatabaseStatic.KEY_MOUSE_NAME)));
                    keyMouse.SetDescription(cursor.getString(cursor.getColumnIndex(DatabaseStatic.KEY_MOUSE_DESCRIPTION)));
                    keyMouse.SetPosition(cursor.getInt(cursor.getColumnIndex(DatabaseStatic.KEY_MOUSE_PX)),
                            cursor.getInt(cursor.getColumnIndex(DatabaseStatic.KEY_MOUSE_PY)));

                    keyMouseList.add(keyMouse);
                }
            }
            cursor.close(); // 记得关闭游标对象

        }catch(SQLException e){

        }

        if(keyMouseList.size() == 0)
        {
            return null;
        }

        return keyMouseList;
    }

    public List<String> LoadTableList()
    {
        List<String> TableNameList = new ArrayList<String>();
        Cursor cursor = database.rawQuery("select name from sqlite_master where type='table' order by name", null);
        while(cursor.moveToNext()){
            //遍历出表名
            String name = cursor.getString(0);

            if(!name.equals("sqlite_sequence") && !name.equals("android_metadata")&& !name.equals("mousekey"))
            {
                Log.i(DEBUG_TAG, name);
                TableNameList.add(name);
            }

        }

        return TableNameList;

    }

    public boolean CreatTable(String TableName)
    {
        if(database == null)
        {
            return false;
        }

        try{

            database.execSQL(CREATE_TABLE1 + TableName + CREATE_TABLE2);

        }catch(SQLException e){

            return false;
        }

        return true;
    }

    //删除某一个表
    public boolean DeleteTable(String TableName){

        if(database == null)
        {
            return false;
        }

        try{

            database.execSQL("drop table " + TableName);

        }catch(SQLException e){

            return false;
        }

        return true;
    }


    public boolean ClearTable(String TableName){
        if(database == null)
        {
            return false;
        }

        try{

            database.execSQL("delete from "+TableName);

        }catch(SQLException e){

            return false;
        }

        return true;

    }

}
