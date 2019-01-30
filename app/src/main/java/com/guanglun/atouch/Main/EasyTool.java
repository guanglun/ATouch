package com.guanglun.atouch.Main;

import android.content.Context;

import com.guanglun.atouch.DBManager.KeyMouse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class EasyTool {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static String bytes2hex(byte[] bytes,int len) {
        StringBuilder sb = new StringBuilder();
        String tmp = null;
        for (int i=0;i<len;i++) {
            // 将每个字节与0xFF进行与运算，然后转化为10进制，然后借助于Integer再转化为16进制
            tmp = Integer.toHexString(0xFF & bytes[i]);
            if (tmp.length() == 1) {
                tmp = "0" + tmp;
            }
            sb.append(tmp.toUpperCase()+" ");
        }
        return sb.toString();

    }

    public static int getStatusBarHeight(Context mContext) {

        Class<?> c = null;

        Object obj = null;

        Field field = null;

        int x = 0, sbar = 0;

        try {

            c = Class.forName("com.android.internal.R$dimen");

            obj = c.newInstance();

            field = c.getField("status_bar_height");

            x = Integer.parseInt(field.get(obj).toString());

            sbar = mContext.getResources().getDimensionPixelSize(x);

        } catch (Exception e1) {

            e1.printStackTrace();

        }

        return sbar;
    }

    public static void delay_ms(int time_ms)
    {
        try {
            Thread.sleep(time_ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static int byte2int(byte byte_h,byte byte_l)
    {
        return (((byte_h << 8) & 0xffff) | (byte_l & 0x00ff));
    }

}
