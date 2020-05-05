package com.guanglun.atouch.Serial;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class USBPermission {

    Context context = null;

    private SerialPort serialPort = null;
    private OpenVIO openvio = null;

    public USBPermission(Context context,SerialPort serialPort,OpenVIO openvio)
    {
        this.context = context;
        this.serialPort = serialPort;
        this.openvio = openvio;
    }

    /**
     * 获得 usb 权限
     */
    private void openUsbDevice(){
        //before open usb device
        //should try to get usb permission
        tryGetUsbPermission();
    }
    UsbManager mUsbManager;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

    public void tryGetUsbPermission(){
        mUsbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);

        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        context.registerReceiver(mUsbPermissionActionReceiver, filter);

        PendingIntent mPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);

        if(mUsbManager.getDeviceList().isEmpty())
        {
            serialPort.sc.on_connect_fail();
            return;
        }

        Log.e("DEBUG USB ","DEVICE NUM "+mUsbManager.getDeviceList().size());
        int device_num = 1;

        //here do emulation to ask all connected usb device for permission
        for (final UsbDevice usbDevice : mUsbManager.getDeviceList().values()) {
            //add some conditional check if necessary
            //if(isWeCaredUsbDevice(usbDevice)){
            if(mUsbManager.hasPermission(usbDevice)){
                //if has already got permission, just goto connect it
                //that means: user has choose yes for your previously popup window asking for grant perssion for this usb device
                //and also choose option: not ask again
                //afterGetUsbPermission(usbDevice);
                Log.e("DEBUG USB ",String.valueOf(device_num) + " " + usbDevice.getProductName());
                device_num++;

                if(Objects.requireNonNull(usbDevice.getProductName()).contains("CP2102"))
                {
                    if(serialPort.open(mUsbManager,usbDevice))
                    {
                        break;
                    }
                }else if(Objects.requireNonNull(usbDevice.getProductName()).contains("OPENVIO"))
                {
                    if(openvio.open(mUsbManager,usbDevice))
                    {
                        break;
                    }
                }

            }else{
                //this line will let android popup window, ask user whether to allow this app to have permission to operate this usb device
                mUsbManager.requestPermission(usbDevice, mPermissionIntent);
            }
            //}
        }


    }


    private void afterGetUsbPermission(UsbDevice usbDevice){
        //call method to set up device communication
        //Toast.makeText(context, String.valueOf("Got permission for usb device: " + usbDevice), Toast.LENGTH_LONG).show();
        //Toast.makeText(context, String.valueOf("Found USB device: VID=" + usbDevice.getVendorId() + " PID=" + usbDevice.getProductId()), Toast.LENGTH_LONG).show();
        Log.e("DEBUG USB ",usbDevice.getProductName());
        doYourOpenUsbDevice(usbDevice);
    }

    private void doYourOpenUsbDevice(UsbDevice usbDevice){
        //now follow line will NOT show: User has not given permission to device UsbDevice

        serialPort.open(mUsbManager,usbDevice);

//        try {
//            serialPort = new SerialPort(new File(usbDevice.getDeviceName()), 9600, 0);
//            Toast.makeText(context, usbDevice.getDeviceName()+"Success", Toast.LENGTH_LONG).show();
//        } catch (IOException e) {
//            Toast.makeText(context, usbDevice.getDeviceName() +"Fail", Toast.LENGTH_LONG).show();
//            e.printStackTrace();
//        }

        //String str = connection.getSerial();

        //add your operation code here
    }

    private final BroadcastReceiver mUsbPermissionActionReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice usbDevice = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        //user choose YES for your previously popup window asking for grant perssion for this usb device
                        if(null != usbDevice){
                            afterGetUsbPermission(usbDevice);
                        }
                    }
                    else {
                        //user choose NO for your previously popup window asking for grant perssion for this usb device
                        Toast.makeText(context, String.valueOf("Permission denied for device" + usbDevice), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    };

}
