package com.guanglun.atouch.DBManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBControl {

    private String DEBUG_TAG = "DBControl";

    private SQLHelper QSLhelper = null;
    private SQLiteDatabase database = null;

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

        return cV;
    }

    public void InsertDatabase(String TableName,KeyMouse keyMouse)   // 向数据库中插入新数据
    {
        if(QSLhelper == null)
        {
            return;
        }

        //database = QSLhelper.getWritableDatabase();


        database.insert(TableName, null, GetContentValues(keyMouse));

    }


    public void UpdateDatabase(String TableName,KeyMouse keyMouse)   // 更新数据
    {
        if(QSLhelper == null)
        {
            return;
        }

        //database = QSLhelper.getWritableDatabase();

        database.update(TableName, GetContentValues(keyMouse),
                DatabaseStatic.KEY_MOUSE_ID + "= ?", new String[]{String.valueOf(keyMouse.ID)});

    }

    public void DeleteDatabase(String TableName,int id)   // 数据库中删除数据
    {
        if(QSLhelper == null)
        {
            return;
        }

        //database = QSLhelper.getWritableDatabase();


        database.delete(TableName, DatabaseStatic.KEY_MOUSE_ID + " = ? ",
                new String[]{String.valueOf(id)});

    }

    public void SearchDatabase(String TableName)   // 查询数据库中的数据
    {
        if(QSLhelper == null)
        {
            return;
        }

        //database = QSLhelper.getWritableDatabase();

        Cursor cursor = database.query(TableName, null, null, null, null, null, null);

        StringBuilder str = new StringBuilder();
        if(cursor.moveToFirst())    // 显示数据库的内容
        {
            for(; !cursor.isAfterLast(); cursor.moveToNext())   // 获取查询游标中的数据
            {
                str.append(cursor.getString(cursor.getColumnIndex(DatabaseStatic.KEY_MOUSE_ID)) + "  ");
                str.append(cursor.getString(cursor.getColumnIndex(DatabaseStatic.KEY_MOUSE_NAME)) + "  ");
                str.append(cursor.getString(cursor.getColumnIndex(DatabaseStatic.KEY_MOUSE_DESCRIPTION)) + "\n");
            }
        }
        cursor.close(); // 记得关闭游标对象

        if(str.toString().equals(""))
        {
            str.append("数据库为空！");
        }

        Log.i(DEBUG_TAG,str.toString());
    }

}
