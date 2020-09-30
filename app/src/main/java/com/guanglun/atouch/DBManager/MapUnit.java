package com.guanglun.atouch.DBManager;

import android.content.ContentValues;

import com.guanglun.atouch.Floating.FloatButtonMap;
import com.guanglun.atouch.Floating.FloatButtonMapATouch;
import com.guanglun.atouch.Floating.FloatButtonMapMouse;
import com.guanglun.atouch.Floating.FloatButtonMapRocker;
import com.guanglun.atouch.Floating.FloatButtonMapSlide;

public class MapUnit {

    final static public int DEVICE_VALUE_NULL      = 0;
    final static public int DEVICE_VALUE_MOUSE     = 1;
    final static public int DEVICE_VALUE_KEYBOARD  = 2;
    final static public int DEVICE_VALUE_JOYSTICK  = 3;

    final static public int MFV_NULL       = 0;
    final static public int MFV_NORMAL     = 1;
    final static public int MFV_PUBG       = 2;
    final static public int MFV_MOUSE      = 3;
    final static public int MFV_ROCKER      = 4;
    final static public int MFV_ATOUCH      = 5;

    final static public int FV0_NORMAL_NORMAL   = 0;
    final static public int FV0_NORMAL_LONG     = 1;

    final static public int FV0_SLIDE_TOP      = 0;
    final static public int FV0_SLIDE_LEFT     = 1;
    final static public int FV0_SLIDE_RIGHT    = 2;
    final static public int FV0_SLIDE_BACK     = 3;
    final static public int FV0_SLIDE_SUP      = 4;

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
    public int FV4 = 0;
    public int FV5 = 0;
    public int FV6 = 0;
    public int FV7 = 0;

    public String FS0 = "";
    public String FS1 = "";
    public String FS2 = "";
    public String FS3 = "";

    public FloatButtonMap bt = null;
    public FloatButtonMapSlide bts = null;
    public FloatButtonMapMouse btm = null;
    public FloatButtonMapRocker btr = null;
    public FloatButtonMapATouch bta = null;

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
        this.FV4 = 0;
        this.FV5 = 0;
        this.FV6 = 0;
        this.FV7 = 0;

        this.FS0 = "";
        this.FS1 = "";
        this.FS2 = "";
        this.FS3 = "";

        this.bt = null;
        this.bts = null;
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
        cv.put("FV4",FV4);
        cv.put("FV5",FV5);
        cv.put("FV6",FV6);
        cv.put("FV7",FV7);
        cv.put("FS0",FS0);
        cv.put("FS1",FS1);
        cv.put("FS2",FS2);
        cv.put("FS3",FS3);
        return cv;
    }

    public String toString()
    {
        return getContentValues().toString();
    }

}
