package com.guanglun.atouch.DBManager;

import android.content.ContentValues;

public class MapUnit {

    final static int DEVICE_VALUE_NULL      = 0;
    final static int DEVICE_VALUE_MOUSE     = 1;
    final static int DEVICE_VALUE_KEYBOARD  = 2;



    public String Name = "";
    public int DeviceValue = DEVICE_VALUE_NULL;
    public int KeyCode = 0;
    public int MFV = 0;
    public int FV0 = 0;
    public int FV1 = 0;
    public int FV2 = 0;
    public int FV3 = 0;
    public String Config = "";

    public MapUnit()
    {
        this.Name = "";
        this.DeviceValue = 0;
        this.KeyCode = 0;
        this.MFV = 0;
        this.FV0 = 0;
        this.FV1 = 0;
        this.FV2 = 0;
        this.FV3 = 0;
        this.Config = "";
    }

    public MapUnit(String Name,int DeviceValue,int KeyCode,int MFV,int FV0,int FV1,int FV2,int FV3,String Config)
    {
        this.Name = Name;
        this.DeviceValue = DeviceValue;
        this.KeyCode = KeyCode;
        this.MFV = MFV;
        this.FV0 = FV0;
        this.FV1 = FV1;
        this.FV2 = FV2;
        this.FV3 = FV3;
        this.Config = Config;
    }

    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put("Name",Name);
        cv.put("DeviceValue",DeviceValue);
        cv.put("KeyCode",KeyCode);
        cv.put("MFV",MFV);
        cv.put("FV0",FV0);
        cv.put("FV1",FV1);
        cv.put("FV2",FV2);
        cv.put("FV3",FV3);
        cv.put("Config",Config);
        return cv;
    }
    public void setKeyCode(int KeyCode)
    {
        this.KeyCode = KeyCode;
    }

    public String toString()
    {
        return "Name : "+Name+" DeviceValue : "+DeviceValue+" KeyCode : "+KeyCode+" MFV : "+MFV+" FV0:"+FV0+" FV1 : "+FV1+" FV2 : "+FV2+" FV3 : "+FV3+" Config : "+Config;
    }
}
