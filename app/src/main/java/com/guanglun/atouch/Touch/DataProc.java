package com.guanglun.atouch.Touch;

import android.content.Context;
import android.util.Log;

import com.guanglun.atouch.DBManager.KeyMouse;
import com.guanglun.atouch.Main.ActivityServiceMessage;
import com.guanglun.atouch.Main.EasyTool;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class DataProc {

    private TCPClient mTCPClient;
    public boolean isADBConnect = false,isADBHeart = true;
    ActivityServiceMessage mActivityServiceMessage;
    private MousePointer mMousePointer;
    public DataProc(Context mContext,TCPClient mTCPClient,ActivityServiceMessage mActivityServiceMessage)
    {
        this.mTCPClient = mTCPClient;
        Timer timer = new Timer();
        timer.schedule(task_2s, 2000,2000);
        mMousePointer = new MousePointer(mContext,mTCPClient);
        this.mActivityServiceMessage = mActivityServiceMessage;
    }

    public void OnBlueReceive(byte[] buf)
    {
        int receive_flag = 0,i=0;
        byte data_len = 0;
        byte[] data_buffer = new byte[200];
        byte check = 0;
        Log.i("DEBUG",EasyTool.bytes2hex(buf,buf.length));
        for(i=0; i<buf.length; i++)
        {
            if(receive_flag == 0 && buf[i] == (byte)0xAA)
            {
                receive_flag = 1;
            }else if(receive_flag == 1 && buf[i] == (byte)0xBB)
            {
                data_len = 0;
                check = 0;
                receive_flag = 2;
            }else if(receive_flag == 2)
            {
                data_len = buf[i];
                receive_flag = 3;
                check+=buf[i];
            }else if(receive_flag >=3 && receive_flag < data_len + 3)
            {
                data_buffer[receive_flag-3] = buf[i];
                receive_flag++;
                check+=buf[i];
            }else if(receive_flag == data_len+3 && check == buf[i])
            {
                DataControl(data_buffer,data_len);
                //Log.i("DEBUG",EasyTool.bytes2hex(data_buffer,data_len));
                receive_flag = 0;
            }else if(buf[i] == 0xAA)
            {
                receive_flag = 1;
            }else{
                receive_flag = 0;
            }
        }
    }

    private void DataControl(byte[] buf,int len)
    {
        switch (buf[0])
        {
            case 0x02:


                break;
            case 0x03:


                break;

            default:
                break;
        }
    }

    public static byte[] Creat(byte cmd,byte[] buf,int len)
    {
        int check_sum = 0;
        byte[] temp = new byte[len + 6];

        temp[0] = (byte)0xCC;
        temp[1] = (byte)0xDD;
        temp[2] = (byte)((len+1)>>8);
        temp[3] = (byte)((len+1));
        temp[4] = cmd;

        System.arraycopy(buf,0,temp,5,len);

        for(int i=0;i<len+3;i++)
        {
            check_sum += temp[i+2];
        }

        temp[5+len] = (byte)check_sum;

        return temp;
    }

    int receive_flag = 0,data_len = 0;
    byte[] data_buffer = new byte[200];
    byte check = 0;
    public boolean OnATouchReceive(byte[] buf,int len)
    {
        boolean isSuccess = false;

        for(int i=0; i<len; i++)
        {
            if(receive_flag == 0 && buf[i] == (byte)0xCC)
            {
                receive_flag = 1;
            }else if(receive_flag == 1 && buf[i] == (byte)0xDD)
            {
                data_len = 0;
                check = 0;
                receive_flag = 2;

            }else if(receive_flag == 2)
            {
                data_len = ((buf[i] << 8) & 0xffff);

                receive_flag = 3;
                check += buf[i];

            }else if(receive_flag == 3)
            {

                data_len |= (buf[i] & 0x00ff);
                receive_flag = 4;
                check += buf[i];
            }else if((receive_flag >= 4) && (receive_flag < (data_len + 4)))
            {

                data_buffer[receive_flag-4] = buf[i];
                receive_flag++;
                check += buf[i];
            }else if((receive_flag == (data_len + 4)) && (check == buf[i]))
            {
                isSuccess = true;
                DataControl_ATouch(data_buffer,data_len);
                receive_flag = 0;
            }else if(buf[i] == 0xCC)
            {
                receive_flag = 1;
            }else{
                receive_flag = 0;
            }
        }

        return isSuccess;
    }

    private boolean isfirstconnect = true;
    public void DataControl_ATouch(byte[] buf,int len)
    {
        switch (buf[0])
        {
            case 0x00:

                isADBConnect = true;
                isADBHeart = true;


                if(buf[1] == (byte)0x00)
                {
                    mActivityServiceMessage.SendToServiceADBStatus(false);
                }else{
                    mActivityServiceMessage.SendToServiceADBStatus(true);
                }

                if(buf[2] == (byte)0x00)
                {
                    mActivityServiceMessage.SendToServiceKeyBoardStatus(false);
                }else{
                    mActivityServiceMessage.SendToServiceKeyBoardStatus(true);
                }

                if(buf[3] == (byte)0x00)
                {
                    mActivityServiceMessage.SendToServiceMouseStatus(false);
                }else{

                    mActivityServiceMessage.SendToServiceMouseStatus(true);
                }

                if(buf[4] == (byte)0x00)
                {
                    mActivityServiceMessage.SendToServiceBLUEStatus(false);
                }else{

                    mActivityServiceMessage.SendToServiceBLUEStatus(true);
                }
                //Log.e("DEBUG",EasyTool.bytes2hex(buf,len));
                break;

            case 0x01:
                if(!isfirstconnect) {
                    mMousePointer.MouseDataProc(buf, len);
                    if(mMousePointer.isClickDown)
                    {
                        mMousePointer.isClickDown = false;
                        mActivityServiceMessage.SendToServiceMouseClick(true);
                    }else if(mMousePointer.isClickUp)
                    {
                        mMousePointer.isClickUp = false;
                        mActivityServiceMessage.SendToServiceMouseClick(false);
                    }

                    mActivityServiceMessage.SendToServiceMouseData(mMousePointer.mouse_x, mMousePointer.mouse_y);
                }
                //Log.i("DEBUG",EasyTool.bytes2hex(buf,len));
                break;
            case 0x02:
                if(buf[1] == 0x00)
                {
                    if(!isfirstconnect) {
                        mActivityServiceMessage.SendToServiceMouseIsShow(false);
                    }
                }else if(buf[1] == 0x01){
                    if(isfirstconnect) {
                        isfirstconnect = false;
                    }
                    mActivityServiceMessage.SendToServiceMouseIsShow(true);
                }

                //Log.i("DEBUG",EasyTool.bytes2hex(buf,len));
                break;
            default:
                break;
        }
    }

    TimerTask task_2s = new TimerTask(){
        public void run() {

            if(isADBConnect)
            {
                if(!isADBHeart)
                {
                    isADBConnect = false;
                    mActivityServiceMessage.SendToServiceADBStatus(false);
                    mActivityServiceMessage.SendToServiceKeyBoardStatus(false);
                    mActivityServiceMessage.SendToServiceMouseStatus(false);
                    mActivityServiceMessage.SendToServiceBLUEStatus(false);
                }else{
                    isADBHeart = false;
                }

            }

        }

    };

}
