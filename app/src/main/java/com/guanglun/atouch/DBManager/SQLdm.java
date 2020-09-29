package com.guanglun.atouch.DBManager;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.guanglun.atouch.Main.EasyTool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 这个类就是实现从assets目录读取数据库文件然后写入SDcard中,如果在SDcard中存在，就打开数据库，不存在就从assets目录下复制过去
 * @author Big_Adamapple
 *
 */
public class SQLdm {

    private String DEBUG_TAG = "SQLdm";

    private final String filePath = "/ATouch";
    private final String DBName = "KeyboardMouse.db";
    private final String SettingName = "AppConfig.properties";

    //private final boolean isReloadDB = false;
    private final boolean isReloadDB = true;


    private String sd_path = null;
    private SQLiteDatabase database;
    private Context context;

    public SQLiteDatabase openDatabase(Context context){
        this.context = context;

        sd_path = Environment.getExternalStorageDirectory().getAbsolutePath();

        EasyTool.createDir(sd_path + filePath + "/App");
        EasyTool.createDir(sd_path + filePath + "/Service");
        EasyTool.createDir(sd_path + filePath + "/Firmware");

        if(isReloadDB){

            File file = new File( sd_path + filePath + "/" + DBName);

            if(file.exists()){
                //Log.i(DEBUG_TAG, "isReloadDB == true Delet file");
                file.delete();
            }
        }

        AssetManager am= context.getAssets();

        String[] nameList = null;

        try {
            nameList = am.list("");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(nameList != null)
        {
            for(String name : nameList)
            {
                //Log.i(DEBUG_TAG, "find file " + name);
                if(name.contains("atouch"))
                {
                    check_copy(name,false);
                }
            }
        }

        check_copy(SettingName,false);

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

        if(file.exists() && is_must_copy)
        {
            //Log.i(DEBUG_TAG, "存在"+fileName+"删除重新拷贝");
            file.delete();
        }else if(file.exists() && !is_must_copy){

            //Log.i(DEBUG_TAG, "存在"+fileName);
            return file;

        }

        file = new File(sd_path + filePath + "/" + fileName);
        {

            //Log.i(DEBUG_TAG, "开始拷贝"+fileName);

            //不存在先创建文件夹
            File path = new File(sd_path + filePath);

            //Log.i(DEBUG_TAG, "pathStr= "+sd_path + filePath);

            if(!path.exists())
            {
                if (path.mkdir()){

                    //Log.i(DEBUG_TAG, "创建成功");

                }else{

                    //Log.i(DEBUG_TAG, "创建失败");

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