package com.guanglun.atouch.Serial;

import android.content.Context;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.Objects;


public class SerialPort {

    private final static int DEFAULT_TIMEOUT = 0;

    public boolean isOpen = false;

    private Context context = null;
    private UsbEndpoint usbEndpointIn = null;
    private UsbEndpoint usbEndpointOut = null;

    private Thread thread_client = null;
    private UsbDeviceConnection connection;
    private CP2102SerialDevice cp2102;

    public serialCallback sc = null;

    public interface serialCallback {
        void on_connect_success();
        void on_connect_fail();
        void on_disconnect();
        void on_receive(byte[] buf,int len);
    }

    public SerialPort(Context context,serialCallback sc)
    {
        this.context = context;
        this.sc = sc;
    }

    public boolean open(UsbManager mUsbManager, UsbDevice usbDevice)
    {
        if(!Objects.requireNonNull(usbDevice.getProductName()).contains("CP2102"))
        {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    sc.on_connect_fail();
                }});
            return false;
        }

        Log.e("DEBUG SERIAL",usbDevice.getProductName());


        connection = mUsbManager.openDevice(usbDevice);


        cp2102 = new CP2102SerialDevice(usbDevice,connection);


        UsbInterface usbInterface = usbDevice.getInterface(0);

        for (int index = 0; index < usbInterface.getEndpointCount(); index++) {
            UsbEndpoint point = usbInterface.getEndpoint(index);
            if (point.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
                if (point.getDirection() == UsbConstants.USB_DIR_IN) {
                    usbEndpointIn = point;
                } else if (point.getDirection() == UsbConstants.USB_DIR_OUT) {
                    usbEndpointOut = point;
                }
            }
        }

        cp2102.open();

        thread_client = new Thread(serial_runnable);
        thread_client.start();

        return true;
    }

    public void close()
    {
        cp2102.close();
        isOpen = false;
        connection.close();
    }

    public void send(byte[] bytes,int len)
    {
        connection.bulkTransfer(usbEndpointOut, bytes, len, DEFAULT_TIMEOUT);
    }

    private Runnable serial_runnable = new Runnable() {
        @Override
        public void run() {


            int inMax = usbEndpointIn.getMaxPacketSize();
            byte[] recv_bytes = new byte[inMax];

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    //Log.e("DEBUG SERIAL ","sc.on_connect_success();");
                    sc.on_connect_success();
                }});

            isOpen = true;

            String str_open = "open";
            send(str_open.getBytes(),str_open.getBytes().length);

            Log.e("DEBUG SERIAL ","OPEN SUCCESS");

            while(isOpen)
            {
                int ret = connection.bulkTransfer(usbEndpointIn,recv_bytes, inMax, DEFAULT_TIMEOUT);
                if(ret > 0)
                {
                    final byte[] bytes = new byte[ret];
                    System.arraycopy(recv_bytes, 0, bytes, 0, ret);

                    //Log.e("DEBUG SERIAL",new String(bytes));

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        public void run() {
                            sc.on_receive(bytes,bytes.length);
                        }});


                }else if(ret < 0){
                    isOpen = false;
                    Log.e("DEBUG SERIAL Error ",String.valueOf(ret));
                }
                //Log.e("DEBUG SERIAL","wait");
            }

            str_open = "clos";
            send(str_open.getBytes(),str_open.getBytes().length);

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    sc.on_disconnect();
                }});

            close();
        }
    };
}
