package com.guanglun.atouch.DBManager;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.guanglun.atouch.R;

import java.util.List;

public class MapAdapter extends ArrayAdapter<MapUnit> {
    private int resourceId;
    public MapAdapter(Context context, int resourceId,List<MapUnit> mapList){
        super(context, resourceId, mapList);
        this.resourceId = resourceId;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        MapUnit map = getItem(position);
        View view = LayoutInflater.from (getContext()).inflate (resourceId, parent, false);

        TextView name = view.findViewById (R.id.map_unit_name);
        TextView code = view.findViewById (R.id.map_unit_code);

        name.setText (String.valueOf(map.DeviceValue));
        code.setText (String.valueOf(map.KeyCode));

        return view;
    }
}
