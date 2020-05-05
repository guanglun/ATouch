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

import com.guanglun.atouch.Main.EasyTool;

import java.util.Objects;

public class OpenVIO {

    private final static String TAG = "OPENVIO";

    private final static int DEFAULT_TIMEOUT = 1000;

    public boolean isOpen = false;

    private Context context = null;
    private UsbEndpoint usbEndpointIn = null;
    private UsbEndpoint usbEndpointOut = null;

    private Thread thread_client = null;
    private UsbDeviceConnection connection;
    private UsbInterface mInterface;

    public OpenVIOCallback sc = null;

    private final static int REQUEST_ATOUCH_START = 0xC0;
    private final static int REQUEST_ATOUCH_STOP  = 0xC1;

    private final static int LIBUSB_REQUEST_TYPE_VENDOR = (0x02 << 5);
    private final static int LIBUSB_ENDPOINT_IN = 0x80;

    public interface OpenVIOCallback {
        void on_connect_success();

        void on_connect_fail();

        void on_disconnect();

        void on_receive(byte[] buf, int len);
    }

    public OpenVIO(Context context, OpenVIOCallback sc) {
        this.context = context;
        this.sc = sc;
    }

    public boolean open(UsbManager mUsbManager, UsbDevice usbDevice) {
        if (!Objects.requireNonNull(usbDevice.getProductName()).contains("OPENVIO")) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    sc.on_connect_fail();
                }
            });
            return false;
        }

        Log.i(TAG, usbDevice.getProductName());

        connection = mUsbManager.openDevice(usbDevice);
        mInterface = usbDevice.getInterface(0);

        if (connection.claimInterface(mInterface, true)) {
            Log.i(TAG, "Interface succesfully claimed");
        } else {
            Log.i(TAG, "Interface could not be claimed");
            return false;
        }

        UsbInterface usbInterface = usbDevice.getInterface(0);

        for (int index = 0; index < usbInterface.getEndpointCount(); index++) {
            UsbEndpoint point = usbInterface.getEndpoint(index);
            if (point.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
                if (point.getDirection() == UsbConstants.USB_DIR_IN) {
                    if(point.getAddress() == 0x81)
                        usbEndpointIn = point;
                } else if (point.getDirection() == UsbConstants.USB_DIR_OUT) {
                    usbEndpointOut = point;
                }
            }
        }

        Log.i(TAG, "[usbEndpointIn : "+usbEndpointIn+"][usbEndpointOut : "+usbEndpointOut+"]");

        connection.controlTransfer(LIBUSB_REQUEST_TYPE_VENDOR+LIBUSB_ENDPOINT_IN, REQUEST_ATOUCH_START, 0, 0, null, 0, 1000);

        thread_client = new Thread(openvio_runnable);
        thread_client.start();

        return true;
    }

    public void close() {
        connection.controlTransfer(LIBUSB_REQUEST_TYPE_VENDOR+LIBUSB_ENDPOINT_IN, REQUEST_ATOUCH_STOP, 0, 0, null, 0, 1000);
        isOpen = false;

        connection.close();
    }

    public void send(byte[] bytes, int len) {
        connection.bulkTransfer(usbEndpointOut, bytes, len, DEFAULT_TIMEOUT);
    }

    private Runnable openvio_runnable = new Runnable() {
        @Override
        public void run() {

            int inMax = usbEndpointIn.getMaxPacketSize();
            byte[] recv_bytes = new byte[inMax];

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    // Log.i("DEBUG SERIAL ","sc.on_connect_success();");
                    sc.on_connect_success();
                }
            });

            isOpen = true;

            Log.i(TAG, "OPEN SUCCESS");

            while (isOpen) {
                int ret = connection.bulkTransfer(usbEndpointIn, recv_bytes, inMax, DEFAULT_TIMEOUT);
                if (ret > 0) {
                    final byte[] bytes = new byte[ret];
                    System.arraycopy(recv_bytes, 0, bytes, 0, ret);

                    // Log.i("DEBUG SERIAL",new String(bytes));

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        public void run() {
                            //Log.i(TAG, EasyTool.bytes2hex(bytes, bytes.length));
                            sc.on_receive(bytes, bytes.length);
                        }
                    });

                }
//                else if(ret != -7)
//                {
//
//                }
                else if (ret < 0) {
                    isOpen = false;
                    Log.i(TAG, "Error " + String.valueOf(ret));
                }
            }

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    Log.i(TAG, "Disconnect Thread Gone");
                    sc.on_disconnect();
                }
            });


        }
    };
}
