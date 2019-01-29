package com.guanglun.atouch.DBManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

/**
 * 这个类就是实现从assets目录读取数据库文件然后写入SDcard中,如果在SDcard中存在，就打开数据库，不存在就从assets目录下复制过去
 * @author Big_Adamapple
 *
 */
public class SQLdm {

    private String DEBUG_TAG = "SQLdm";

    private final boolean isReloadDB = false;
    //private final boolean isReloadDB = true;

    //数据库存储路径
    private final String filePath = "/ATouch";
    private final String fileName = "KeyboardMouse.db";

    SQLiteDatabase database;

    public  SQLiteDatabase openDatabase(Context context){

        String sd_path = Environment.getExternalStorageDirectory().getAbsolutePath();

        Log.i(DEBUG_TAG, "filePath: " + sd_path + filePath + "/" + fileName);

        if(isReloadDB == true){

            File file = new File( sd_path + filePath + "/" + fileName);

            if(file.exists()){
                Log.i(DEBUG_TAG, "isReloadDB == true Delet file");
                file.delete();
            }
        }

        File jhPath = new File( sd_path + filePath + "/" + fileName);

        //查看数据库文件是否存在
        if(jhPath.exists()){

            Log.i(DEBUG_TAG, "存在数据库");

            //存在则直接返回打开的数据库
            return SQLiteDatabase.openOrCreateDatabase(jhPath, null);

        }else{

            Log.i(DEBUG_TAG, "不存在数据库");

            //不存在先创建文件夹
            File path = new File(sd_path + filePath);

            Log.i(DEBUG_TAG, "pathStr= "+sd_path + filePath);

            if(!path.exists())
            {
                if (path.mkdir()){

                    Log.i(DEBUG_TAG, "创建成功");

                }else{

                    Log.i(DEBUG_TAG, "创建失败");

                };
            }

            try {

                //得到资源
                AssetManager am= context.getAssets();
                //得到数据库的输入流
                InputStream is=am.open("KeyboardMouse.db");

                //用输出流写到SDcard上面
                FileOutputStream fos = new FileOutputStream(jhPath);

                Log.i(DEBUG_TAG, "fos=" + fos);
                Log.i(DEBUG_TAG, "jhPath=" + jhPath);

                //创建byte数组  用于1KB写一次
                byte[] buffer = new byte[1024];
                int count = 0;

                while((count = is.read(buffer))>0){

                    fos.write(buffer,0,count);
                }

                //最后关闭就可以了
                fos.flush();
                fos.close();
                is.close();

                return SQLiteDatabase.openOrCreateDatabase(jhPath, null);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
        }
    }
}