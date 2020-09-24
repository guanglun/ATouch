package com.guanglun.atouch.Floating;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.guanglun.atouch.R;

import java.math.BigInteger;

public class KeyBoardView extends LinearLayout implements View.OnClickListener {

    private KBCallback cb;

    public interface KBCallback {
        void onClick(Integer value);
    }

    public KeyBoardView(Context context, KBCallback cb) {
        super(context);
        this.cb = cb;

        LayoutInflater.from(context).inflate(R.layout.keyboard, this);
        ViewGroup vg = this.findViewById(R.id.kblayout);

        for (int i = 0; i < vg.getChildCount(); i++) {
            View viewchild = vg.getChildAt(i);
            if (viewchild instanceof ViewGroup)
            {
                ViewGroup viewchild1 = (ViewGroup) viewchild;
                for (int ii = 0; ii < viewchild1.getChildCount(); ii++) {

                    View viewchild2 = viewchild1.getChildAt(ii);
                    Log.i("KB", String.valueOf(viewchild2.getId()));
                    viewchild2.setOnClickListener(this);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {

        String tag = (String)v.getTag();
        if(tag != null) {
            new BigInteger(tag,16);
            Integer intValue = Integer.parseInt(tag, 16);
            Log.i("KB", intValue.toString());
            cb.onClick(intValue);
        }
    };
}
