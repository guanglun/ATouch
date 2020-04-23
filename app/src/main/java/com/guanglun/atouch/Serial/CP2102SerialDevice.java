package com.guanglun.atouch.Serial;

import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbRequest;
import android.util.Log;

public class CP2102SerialDevice {
    private static final int DATA_BITS_5 = 5;
    private static final int DATA_BITS_6 = 6;
    private static final int DATA_BITS_7 = 7;
    private static final int DATA_BITS_8 = 8;

    private static final int STOP_BITS_1 = 1;
    private static final int STOP_BITS_15 = 3;
    private static final int STOP_BITS_2 = 2;

    private static final int PARITY_NONE = 0;
    private static final int PARITY_ODD = 1;
    private static final int PARITY_EVEN = 2;
    private static final int PARITY_MARK = 3;
    private static final int PARITY_SPACE = 4;

    private static final int FLOW_CONTROL_OFF = 0;
    private static final int FLOW_CONTROL_RTS_CTS = 1;
    private static final int FLOW_CONTROL_DSR_DTR = 2;
    private static final int FLOW_CONTROL_XON_XOFF = 3;

    private static final String CLASS_ID = CP2102SerialDevice.class.getSimpleName();


    private static final int CP210x_PURGE = 0x12;
    private static final int CP210x_IFC_ENABLE = 0x00;
    private static final int CP210x_SET_BAUDDIV = 0x01;
    private static final int CP210x_SET_LINE_CTL = 0x03;
    private static final int CP210x_GET_LINE_CTL = 0x04;
    private static final int CP210X_SET_BREAK = 0x05;
    private static final int CP210x_SET_MHS = 0x07;
    private static final int CP210x_SET_BAUDRATE = 0x1E;
    private static final int CP210x_SET_FLOW = 0x13;
    private static final int CP210x_SET_XON = 0x09;
    private static final int CP210x_SET_XOFF = 0x0A;
    private static final int CP210x_SET_CHARS = 0x19;
    private static final int CP210x_GET_MDMSTS = 0x08;
    private static final int CP210x_GET_COMM_STATUS = 0x10;

    private static final int CP210x_REQTYPE_HOST2DEVICE = 0x41;
    private static final int CP210x_REQTYPE_DEVICE2HOST = 0xC1;

    private static final int CP210x_BREAK_ON = 0x0001;
    private static final int CP210x_BREAK_OFF = 0x0000;

    private static final int CP210x_MHS_RTS_ON = 0x202;
    private static final int CP210x_MHS_RTS_OFF = 0x200;
    private static final int CP210x_MHS_DTR_ON = 0x101;
    private static final int CP210x_MHS_DTR_OFF = 0x100;

    private static final int CP210x_PURGE_ALL = 0x000f;

    /***
     *  Default Serial Configuration
     *  Baud rate: 9600
     *  Data bits: 8
     *  Stop bits: 1
     *  Parity: None
     *  Flow Control: Off
     */
    private static final int CP210x_UART_ENABLE = 0x0001;
    private static final int CP210x_UART_DISABLE = 0x0000;
    private static final int CP210x_LINE_CTL_DEFAULT = 0x0800;
    private static final int CP210x_MHS_DEFAULT = 0x0000;
    private static final int CP210x_MHS_DTR = 0x0001;
    private static final int CP210x_MHS_RTS = 0x0010;
    private static final int CP210x_MHS_ALL = 0x0011;
    private static final int CP210x_XON = 0x0000;
    private static final int CP210x_XOFF = 0x0000;
    private static final int DEFAULT_BAUDRATE = 115200;

    /**
     * Flow control variables
     */
    private boolean rtsCtsEnabled;
    private boolean dtrDsrEnabled;
    private boolean ctsState;
    private boolean dsrState;


    private UsbEndpoint inEndpoint;
    private UsbEndpoint outEndpoint;

    private final static int USB_TIMEOUT = 1000;
    private UsbDeviceConnection connection;
    private UsbInterface mInterface;

    public CP2102SerialDevice(UsbDevice device, UsbDeviceConnection connection) {
        this.connection = connection;
        mInterface = device.getInterface(0);
    }

    public void setBaudRate(int baudRate) {
        byte[] data = new byte[]{
                (byte) (baudRate & 0xff),
                (byte) (baudRate >> 8 & 0xff),
                (byte) (baudRate >> 16 & 0xff),
                (byte) (baudRate >> 24 & 0xff)
        };
        setControlCommand(CP210x_SET_BAUDRATE, 0, data);
    }

    public void setDataBits(int dataBits) {
        short wValue = getCTL();
        wValue &= ~0x0F00;
        switch (dataBits) {
            case DATA_BITS_5:
                wValue |= 0x0500;
                break;
            case DATA_BITS_6:
                wValue |= 0x0600;
                break;
            case DATA_BITS_7:
                wValue |= 0x0700;
                break;
            case DATA_BITS_8:
                wValue |= 0x0800;
                break;
            default:
                return;
        }
        setControlCommand(CP210x_SET_LINE_CTL, wValue, null);
    }

    public void setStopBits(int stopBits) {
        short wValue = getCTL();
        wValue &= ~0x0003;
        switch (stopBits) {
            case STOP_BITS_1:
                wValue |= 0;
                break;
            case STOP_BITS_15:
                wValue |= 1;
                break;
            case STOP_BITS_2:
                wValue |= 2;
                break;
            default:
                return;
        }
        setControlCommand(CP210x_SET_LINE_CTL, wValue, null);
    }

    public void setParity(int parity) {
        short wValue = getCTL();
        wValue &= ~0x00F0;
        switch (parity) {
            case PARITY_NONE:
                wValue |= 0x0000;
                break;
            case PARITY_ODD:
                wValue |= 0x0010;
                break;
            case PARITY_EVEN:
                wValue |= 0x0020;
                break;
            case PARITY_MARK:
                wValue |= 0x0030;
                break;
            case PARITY_SPACE:
                wValue |= 0x0040;
                break;
            default:
                return;
        }
        setControlCommand(CP210x_SET_LINE_CTL, wValue, null);
    }

    public void setFlowControl(int flowControl) {
        switch (flowControl) {
            case FLOW_CONTROL_OFF:
                byte[] dataOff = new byte[]{
                        (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                        (byte) 0x40, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                        (byte) 0x00, (byte) 0x80, (byte) 0x00, (byte) 0x00,
                        (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x00
                };
                rtsCtsEnabled = false;
                dtrDsrEnabled = false;
                setControlCommand(CP210x_SET_FLOW, 0, dataOff);
                break;
            default:
                return;
        }
    }

    public void setBreak(boolean state) {
        if (state) {
            setControlCommand(CP210X_SET_BREAK, CP210x_BREAK_ON, null);
        } else {
            setControlCommand(CP210X_SET_BREAK, CP210x_BREAK_OFF, null);
        }
    }

    public void setRTS(boolean state) {
        if (state) {
            setControlCommand(CP210x_SET_MHS, CP210x_MHS_RTS_ON, null);
        } else {
            setControlCommand(CP210x_SET_MHS, CP210x_MHS_RTS_OFF, null);
        }
    }

    public void setDTR(boolean state) {
        if (state) {
            setControlCommand(CP210x_SET_MHS, CP210x_MHS_DTR_ON, null);
        } else {
            setControlCommand(CP210x_SET_MHS, CP210x_MHS_DTR_OFF, null);
        }
    }

    public boolean open() {

        if (connection.claimInterface(mInterface, true)) {
            Log.i(CLASS_ID, "Interface succesfully claimed");
        } else {
            Log.i(CLASS_ID, "Interface could not be claimed");
            return false;
        }

        // Default Setup
        if (setControlCommand(CP210x_IFC_ENABLE, CP210x_UART_ENABLE, null) < 0)
            return false;
        setBaudRate(DEFAULT_BAUDRATE);
        if (setControlCommand(CP210x_SET_LINE_CTL, CP210x_LINE_CTL_DEFAULT, null) < 0)
            return false;
        setFlowControl(FLOW_CONTROL_OFF);
        if (setControlCommand(CP210x_SET_MHS, CP210x_MHS_DEFAULT, null) < 0)
            return false;

        return true;
    }

    public void close()
    {
        setControlCommand(CP210x_PURGE, CP210x_PURGE_ALL, null);
        setControlCommand(CP210x_IFC_ENABLE, CP210x_UART_DISABLE, null);
        connection.releaseInterface(mInterface);
    }

    private int setControlCommand(int request, int value, byte[] data) {
        int dataLength = 0;
        if (data != null) {
            dataLength = data.length;
        }
        int response = connection.controlTransfer(CP210x_REQTYPE_HOST2DEVICE, request, value, mInterface.getId(), data, dataLength, USB_TIMEOUT);
        Log.i(CLASS_ID, "Control Transfer Response: " + String.valueOf(response));
        return response;
    }
    private byte[] getModemState()
    {
        byte[] data = new byte[1];
        connection.controlTransfer(CP210x_REQTYPE_DEVICE2HOST, CP210x_GET_MDMSTS, 0, mInterface.getId(), data, 1, USB_TIMEOUT);
        return data;
    }

    private byte[] getCommStatus()
    {
        byte[] data = new byte[19];
        int response = connection.controlTransfer(CP210x_REQTYPE_DEVICE2HOST, CP210x_GET_COMM_STATUS, 0, mInterface.getId(), data, 19, USB_TIMEOUT);
        Log.i(CLASS_ID, "Control Transfer Response (Comm status): " + String.valueOf(response));
        return data;
    }

    private short getCTL()
    {
        byte[] data = new byte[2];
        int response = connection.controlTransfer(CP210x_REQTYPE_DEVICE2HOST, CP210x_GET_LINE_CTL, 0, mInterface.getId(), data, data.length, USB_TIMEOUT);
        Log.i(CLASS_ID,"Control Transfer Response: " + String.valueOf(response));
        return (short)((data[1] << 8) | (data[0] & 0xFF));
    }
}
