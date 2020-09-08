package com.guanglun.atouch.Touch;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.guanglun.atouch.Main.EasyTool;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


public class TCPClient {

    private String ip = null;
    private int port = 0;

    private Socket socket_client = null;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;

    private Thread thread_client = null;

    private socket_callback sc = null;

    private boolean is_connect = false,is_auto_connect = true;

    public interface socket_callback {
        void on_connect_success();
        void on_connect_fail();
        void on_disconnect();
        void on_receive(byte[] buf,int len);
    }

    public TCPClient(String ip,int port,socket_callback sc)
    {
        this.ip = ip;
        this.port = port;
        this.sc = sc;

        connect();
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
            byte receive_buffer[] = new byte[4 * 1024];
            int receive_len = 0;

            while(is_auto_connect)
            {


                try {
                    socket_client = new Socket(ip, port);
                    is_connect = true;
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        public void run() {
                            sc.on_connect_success();
                        }});


                    inputStream = socket_client.getInputStream();
                    outputStream = socket_client.getOutputStream();

                    byte[] buf = new byte[]{0x01,0x02,0x03};
                    byte[] buf2 = DataProc.Creat((byte)0x00,buf,3);
                    socket_send(buf2,buf2.length);

                    while ((receive_len = inputStream.read(receive_buffer)) != -1) {
                        sc.on_receive(receive_buffer, receive_len);

                        //Log.e("DEBUG",new String(receive_buffer, 0, receive_len));
                    }

                    socket_client = null;
                    is_connect = false;
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        public void run() {
                            sc.on_disconnect();
                        }});


                }catch (IOException e) {
                    socket_client = null;
                    if(is_connect)
                    {
                        is_connect = false;

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            public void run() {
                                sc.on_disconnect();
                            }});
                    }else{

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            public void run() {
                                sc.on_connect_fail();
                            }});
                    }
                    //e.printStackTrace();
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
