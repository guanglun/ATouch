package com.guanglun.atouch.Bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guanglun.atouch.R;

import java.util.List;

public class BluelistAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<BluetoothDevice> blue_list;


    public BluelistAdapter(Context context, List<BluetoothDevice> blue_list) {

        mInflater = LayoutInflater.from(context);
        this.blue_list = blue_list;
    }

    //返回数据集的长度
    @Override
    public int getCount() {
        return blue_list.size();
    }

    @Override
    public Object getItem(int position) {
        return blue_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //这个方法才是重点，我们要为它编写一个ViewHolder
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.scan_blue_listview, parent, false); //加载布局
            holder = new ViewHolder();

            holder.blue_name = (TextView) convertView.findViewById(R.id.blue_name);
            holder.blue_address = (TextView) convertView.findViewById(R.id.blue_address);

            convertView.setTag(holder);
        } else {   //else里面说明，convertView已经被复用了，说明convertView中已经设置过tag了，即holder
            holder = (ViewHolder) convertView.getTag();
        }

        BluetoothDevice blue_device = blue_list.get(position);
        holder.blue_name.setText(blue_device.getName());
        holder.blue_address.setText(blue_device.getAddress());

        return convertView;
    }

    //这个ViewHolder只能服务于当前这个特定的adapter，因为ViewHolder里会指定item的控件，不同的ListView，item可能不同，所以ViewHolder写成一个私有的类
    private class ViewHolder {
        TextView blue_name;
        TextView blue_address;
    }
}
