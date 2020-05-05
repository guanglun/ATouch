package com.guanglun.atouch.Touch;

import android.content.Context;
import android.util.Log;

import com.guanglun.atouch.Main.EasyTool;

import static com.guanglun.atouch.Floating.FloatMouse.HIGHT;
import static com.guanglun.atouch.Floating.FloatMouse.LENGTH;

public class MousePointer {
    private Context mContext;
    public int mouse_x,mouse_y,mouse_l,mouse_h;
    private TCPClient mTCPClient;

    public MousePointer(Context mContext,TCPClient mTCPClient)
    {
        this.mContext = mContext;

        mouse_l = EasyTool.dip2px(mContext,LENGTH);
        mouse_h = EasyTool.dip2px(mContext,HIGHT);

        mouse_x = EasyTool.getScreenWidth(mContext)/2;
        mouse_y = EasyTool.getScreenHeight(mContext)/2;
        this.mTCPClient = mTCPClient;
    }

    private boolean ischeckdown = false;

    public void MouseDataProc(byte[] buf,int len)
    {
        if(((buf[1] & 0x01) == 0x01))
        {
            SendMouseClick((byte)0x01,mouse_x-1,mouse_y-1);
            ischeckdown = true;
        }else if(((buf[1] & 0x01) == 0x00) && ischeckdown)
        {
            SendMouseClick((byte)0x00,mouse_x-1,mouse_y-1);
            ischeckdown = false;
        }

        if(buf[2] > 100)
        {
            buf[2] = 100;
        }else if(buf[2] < -100)
        {
            buf[2] = -100;
        }

        if(buf[3] > 100)
        {
            buf[3] = 100;
        }else if(buf[3] < -100)
        {
            buf[3] = -100;
        }

        mouse_x += buf[2]*2;
        mouse_y += buf[3]*2;

        mouse_x = EasyTool.limit(mouse_x,0,EasyTool.getScreenWidth(mContext) - mouse_l);
        mouse_y = EasyTool.limit(mouse_y,0,EasyTool.getScreenHeight(mContext) - mouse_h);
    }

    private void SendMouseClick(byte chick_flag,int x,int y)
    {
        byte[] temp = new byte[5];
        temp[0] = chick_flag;
        temp[1] = (byte)(x>>8);
        temp[2] = (byte)(x);
        temp[3] = (byte)(y>>8);
        temp[4] = (byte)(y);

        byte[] temp2 = DataProc.Creat((byte)0x02,temp,5);

        //Log.e("DEBUG",EasyTool.bytes2hex(temp2,temp2.length));

        mTCPClient.socket_send(temp2,temp2.length);
    }

}
