package com.guanglun.atouch.DBManager;

import android.content.ContentValues;

import com.guanglun.atouch.Floating.FloatButtonMap;

public class MapUnit {

    final static public int DEVICE_VALUE_NULL      = 0;
    final static public int DEVICE_VALUE_MOUSE     = 1;
    final static public int DEVICE_VALUE_KEYBOARD  = 2;

    final static public int MFV_NULL       = 0;
    final static public int MFV_NORMAL     = 1;
    final static public int MFV_PUBG       = 2;

    final static public int FV0_NORMAL_NORMAL   = 0;
    final static public int FV0_NORMAL_LONG     = 1;

    public String Name = "";
    public int DeviceValue = DEVICE_VALUE_NULL;
    public int KeyCode = 0;
    public String KeyName = "";
    public int PX = 0;
    public int PY = 0;
    public String Describe = "";
    public int MFV = 0;
    public int FV0 = 0;
    public int FV1 = 0;
    public int FV2 = 0;
    public int FV3 = 0;
    public String Config = "";


    public FloatButtonMap bt = null;

    public MapUnit()
    {
        this.Name = "";
        this.DeviceValue = 0;
        this.KeyCode = 0;
        this.KeyName = "";
        this.PX = 0;
        this.PY = 0;
        this.Describe = "";
        this.MFV = 0;
        this.FV0 = 0;
        this.FV1 = 0;
        this.FV2 = 0;
        this.FV3 = 0;
        this.Config = "";
        this.bt = null;
    }

    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put("Name",Name);
        cv.put("DeviceValue",DeviceValue);
        cv.put("KeyCode",KeyCode);
        cv.put("KeyName",KeyName);
        cv.put("PX",PX);
        cv.put("PY",PY);
        cv.put("Describe",Describe);
        cv.put("MFV",MFV);
        cv.put("FV0",FV0);
        cv.put("FV1",FV1);
        cv.put("FV2",FV2);
        cv.put("FV3",FV3);
        cv.put("Config",Config);
        return cv;
    }

}
