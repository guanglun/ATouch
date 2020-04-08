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
    //数据库存储路径
    private final String filePath = "/ATouch";
    private final String DBName = "KeyboardMouse.db";
    private final String ServerName = "ATouchService";
    private final String BinName = "Atouch.bin";

    private final boolean isReloadDB = false;
    //private final boolean isReloadDB = true;
    private String sd_path = null;
    private SQLiteDatabase database;
    private Context context;

    public SQLiteDatabase openDatabase(Context context){
        this.context = context;

        sd_path = Environment.getExternalStorageDirectory().getAbsolutePath();

        Log.i(DEBUG_TAG, "filePath: " + sd_path + filePath + "/" + DBName);

        if(isReloadDB == true){

            File file = new File( sd_path + filePath + "/" + DBName);

            if(file.exists()){
                Log.i(DEBUG_TAG, "isReloadDB == true Delet file");
                file.delete();
            }
        }

        check_copy(ServerName,true);
        check_copy(BinName,true);

        File db_file = check_copy(DBName,false);
        if(db_file != null)
        {
            return SQLiteDatabase.openOrCreateDatabase(db_file, null);
        }

        return null;

    }

    public File check_copy(String fileName,boolean is_must_copy)
    {
        File file = new File(sd_path + filePath + "/" + fileName);

        if(file.exists() && !is_must_copy){
            Log.i(DEBUG_TAG, "存在"+fileName);
            return file;

        }else{

            Log.i(DEBUG_TAG, "不存在"+fileName);

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
                InputStream is=am.open(fileName);

                //用输出流写到SDcard上面
                FileOutputStream fos = new FileOutputStream(file);

//                Log.i(DEBUG_TAG, "fos=" + fos);
//                Log.i(DEBUG_TAG, "jhPath=" + file);

                //创建byte数组  用于4KB写一次
                byte[] buffer = new byte[4096];
                int count;

                while((count = is.read(buffer))>0){

                    fos.write(buffer,0,count);
                }

                //最后关闭就可以了
                fos.flush();
                fos.close();
                is.close();

                return file;

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
        }
    }
}