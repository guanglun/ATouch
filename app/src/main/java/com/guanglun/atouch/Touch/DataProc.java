package com.guanglun.atouch.Touch;

import android.util.Log;

import com.guanglun.atouch.DBManager.KeyMouse;
import com.guanglun.atouch.Main.EasyTool;

import java.util.ArrayList;
import java.util.List;



public class DataProc {

    public interface DataProcCallBack {
        void OnSetKeyMap(List<KeyMouse> list);

    }

    private DataProcCallBack dp_cb;

    public void DataProc(DataProcCallBack dp_cb)
    {
        this.dp_cb = dp_cb;
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
                Log.i("DEBUG",EasyTool.bytes2hex(data_buffer,data_len));
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

}
