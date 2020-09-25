package com.guanglun.atouch.Floating;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guanglun.atouch.R;

import java.util.List;

public class FloatListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<String> NameList;

    public FloatListAdapter(Context context, List<String> NameList) {

        mInflater = LayoutInflater.from(context);
        this.NameList = NameList;

    }

    //返回数据集的长度
    @Override
    public int getCount() {
        return NameList.size();
    }

    @Override
    public Object getItem(int position) {
        return NameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //这个方法才是重点，我们要为它编写一个ViewHolder
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FloatListAdapter.ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.float_select_key_listview, parent, false); //加载布局
            holder = new FloatListAdapter.ViewHolder();

            holder.tv_float_select_key_name = (TextView) convertView.findViewById(R.id.tv_float_select_key_name);
            //holder.tv_float_select_key_description = (TextView) convertView.findViewById(R.id.tv_float_select_key_description);

            convertView.setTag(holder);
        } else {   //else里面说明，convertView已经被复用了，说明convertView中已经设置过tag了，即holder
            holder = (FloatListAdapter.ViewHolder) convertView.getTag();
        }

        holder.tv_float_select_key_name.setText(NameList.get(position));
        //holder.tv_float_select_key_description.setText("NULL");

        return convertView;
    }

    //这个ViewHolder只能服务于当前这个特定的adapter，因为ViewHolder里会指定item的控件，不同的ListView，item可能不同，所以ViewHolder写成一个私有的类
    private class ViewHolder {
        TextView tv_float_select_key_name;
        //TextView tv_float_select_key_description;
    }
}
