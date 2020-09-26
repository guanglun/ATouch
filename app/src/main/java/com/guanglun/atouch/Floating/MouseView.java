package com.guanglun.atouch.Floating;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.guanglun.atouch.R;

public class MouseView extends LinearLayout implements View.OnClickListener {

    private MCallback cb;

    public interface MCallback {
        void onClick(Integer value);
    }

    public MouseView(Context context, MCallback cb) {
        super(context);
        this.cb = cb;

        LayoutInflater.from(context).inflate(R.layout.mouse, this);
        ViewGroup vg = this.findViewById(R.id.mouse_layout);

        for (int i = 0; i < vg.getChildCount(); i++) {
            View viewchild = vg.getChildAt(i);
            viewchild.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {

        String tag = (String)v.getTag();
        if(tag != null) {
            Integer intValue = Integer.parseInt(tag);
            cb.onClick(intValue);
        }
    };
}
