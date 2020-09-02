package com.guanglun.atouch.Main;

import android.content.Context;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

    private Properties props;

    public void init(Context c)
    {
        props = new Properties();

        try {
            InputStream in = c.getAssets().open("AppConfig.properties");
            props.load(in);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public int getOffsetConfig()
    {
        String str = props.getProperty("OffsetConfig");
        if(str != null)
            return Integer.parseInt(str);
        else
            return 0;

    }

    public void setOffsetConfig(int v)
    {
        props.setProperty("OffsetConfig",String.valueOf(v));

    }

    public void store()
    {
        try {
            FileOutputStream output = new FileOutputStream("AppConfig.properties");
            props.store(output,"This is overwrite file");
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

}
