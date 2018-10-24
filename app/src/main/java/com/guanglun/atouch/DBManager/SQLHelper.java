package com.guanglun.atouch.DBManager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLHelper extends SQLiteOpenHelper {

    private String DEBUG_TAG = "SQLHelper";

    public static String CREATE_TABLE = "create table "+ DatabaseStatic.TABLE_NAME +"(" +
            DatabaseStatic.KEY_MOUSE_ID + " Integer," +
            DatabaseStatic.KEY_MOUSE_NAME + " varchar(30)," +
            DatabaseStatic.KEY_MOUSE_DESCRIPTION + " varchar(30))" ;    // 用于创建表的SQL语句


    public SQLHelper(Context context, String name,
                     SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "KeyboardMouse.db", null, 1);
    }

    public SQLHelper(Context context)
    {
        super(context, "KeyboardMouse.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(DEBUG_TAG,CREATE_TABLE);
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

    }
}