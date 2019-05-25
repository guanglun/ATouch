package com.guanglun.atouch.upgrade;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.guanglun.atouch.Main.EasyTool;
import com.guanglun.atouch.Touch.DataProc;
import com.guanglun.atouch.Touch.TCPClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class UpgradeHardware {

    private Context context = null;
    private String file_path = null;
    private final int BUFFER_SIZE = 1024;
    private final String TAG = "UpgradeHardware";
    private String ip = null;
    private int port = 4756;

    private Socket socket_client = null;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;

    private Thread thread_client = null;


    private boolean is_connect = false,is_auto_connect = true;

    private upgrade_callback gc = null;
    public interface upgrade_callback {
        void set_status_text(String str);
    }

    public void start()
    {
        this.ip = EasyTool.getWifiRouteIPAddress(context);
        Log.i(TAG,"get ip: " + this.ip);

        connect();
    }

    public UpgradeHardware(Context context,String path,upgrade_callback gc)
    {
        this.context = context;
        this.file_path = path;
        this.gc = gc;
    }

    public void connect()
    {
        if(socket_client == null && thread_client == null)
        {
            is_auto_connect = true;
            thread_client = new Thread(socket_runnable);
            thread_client.start();
        }
    }

    private Runnable socket_runnable = new Runnable() {
        @Override
        public void run() {
            byte receive_buffer[] = new byte[BUFFER_SIZE];
            int receive_len = 0,count= 0;

                try {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        public void run() {
                            gc.set_status_text("开始升级");
                        }});

                    Log.i(TAG,"socket connect to " + ip + " " + port);
                    socket_client = new Socket(ip, port);
                    is_connect = true;

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        public void run() {
                            gc.set_status_text("连接成功");
                        }});

                    Log.i(TAG,"socket connect success");

                    inputStream = socket_client.getInputStream();
                    outputStream = socket_client.getOutputStream();

                    File file = new File(file_path);

                    InputStream is_file = new FileInputStream(file);;


                    final int size = is_file.available();

                    byte[] buf = new byte[8];
                    buf[0] = 0;
                    buf[1] = 1;
                    buf[2] = 2;
                    buf[3] = 3;
                    buf[4] = (byte)(size>>24);
                    buf[5] = (byte)(size>>16);
                    buf[6] = (byte)(size>>8);
                    buf[7] = (byte)(size);

                    socket_send(buf,buf.length);
                    Log.i(TAG,"file size:" + size);
                    receive_len = inputStream.read(receive_buffer);

                    if(receive_len == 8)
                    {
                        Thread.sleep(500);
                        Log.i(TAG,"start transmission");
                        while(count < size){
                            if((receive_len = is_file.read(receive_buffer)) > 0)
                            {
                                count += receive_len;
                                socket_send(receive_buffer,receive_len);
                                Log.i(TAG,"send "+receive_len + " " + EasyTool.bytes2hex(receive_buffer,10));



                                receive_len = inputStream.read(receive_buffer);
                                if(receive_len != 8)
                                {
                                    break;
                                }


                                final int finalCount = count;
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    public void run() {
                                        gc.set_status_text("正在升级 " + finalCount *100/size + "%");
                                    }});
                            }else{
                                break;
                            }


                        }

                        if(count == size)
                        {
                            receive_len = inputStream.read(receive_buffer);
                            if(receive_len == 2 && receive_buffer[0] == 'o' && receive_buffer[1] == 'k')
                            {
                                Log.i(TAG,"upgrade success");
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    public void run() {
                                        gc.set_status_text("升级完成");
                                    }});
                            }else if(receive_len == 4 && receive_buffer[0] == 'f' && receive_buffer[1] == 'a' && receive_buffer[2] == 'i' && receive_buffer[3] == 'l')
                            {
                                Log.i(TAG,"upgrade fail");
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    public void run() {
                                        gc.set_status_text("升级失败");
                                    }});
                            }
                        }else{
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                public void run() {
                                    gc.set_status_text("升级失败");
                                }});
                        }


                        Log.i(TAG,"end transmission");


                    }else{
                        Log.i(TAG,"head fail");
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            public void run() {
                                gc.set_status_text("升级失败");
                            }});

                    }


                    is_file.close();

                    socket_client = null;
                    is_connect = false;


                }catch (IOException e) {

                    socket_client = null;
                    if(is_connect)
                    {
                        is_connect = false;

                    }else{

                    }
                    Log.i(TAG,"socket error");
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            thread_client = null;
        }
    };

    public void disconnect()
    {
        if(socket_client != null)
        {
            try {
                socket_client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            socket_client = null;
        }
    }

    public void socket_send(final byte[] buffer, final int len)
    {
        if(socket_client != null)
        {
            if(socket_client.isConnected())
            {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            outputStream.write(buffer,0,len);
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    }

    public void socket_send_not_creat_task(final byte[] buffer, final int len)
    {
        if(socket_client != null)
        {
            if(socket_client.isConnected())
            {
                try {
                    outputStream.write(buffer,0,len);
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
