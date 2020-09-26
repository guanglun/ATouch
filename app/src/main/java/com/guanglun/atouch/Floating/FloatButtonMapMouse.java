package com.guanglun.atouch.Floating;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.guanglun.atouch.DBManager.MapUnit;
import com.guanglun.atouch.Main.EasyTool;
import com.guanglun.atouch.R;

import static android.view.View.VISIBLE;

public class FloatButtonMapMouse {
    private final int ROUNDD = 30;

    private Context mContext;
    private FloatingManager mFloatingManager;
    private RelativeLayout mRelativeLayout;
    private WindowManager.LayoutParams mParams;

    private Button button,bt_sup;
    private MapUnit map;
    public int PositionX,PositionY,PositionX2,PositionY2;
    private FloatMenu mFloatMenu;

    private boolean isLongClick = false;
    private boolean isLongClick2 = false;
    public FloatButtonMapMouse(Context context, FloatMenu mFloatMenu, MapUnit map)
    {
        mContext = context;
        this.map = map;
        this.mFloatMenu = mFloatMenu;
        mFloatingManager = mFloatMenu.mFloatingManager;
        mRelativeLayout = mFloatMenu.mRelativeLayoutEdit;
        mParams = mFloatMenu.mParamsEdit;

        RelativeLayout.LayoutParams mLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);

        button = new Button(mContext);
        bt_sup = new Button(mContext);

        mLayoutParams.width = EasyTool.dip2px(mContext,ROUNDD);
        mLayoutParams.height  = EasyTool.dip2px(mContext,ROUNDD);

        mLayoutParams.setMargins(0,0,0,0);
        mLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
        mLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);

        button.setBackground(mContext.getResources().getDrawable(R.drawable.pubg_face));
        button.setX(map.PX - mFloatMenu.mFloatMapManager.offset[0]- EasyTool.dip2px(mContext,ROUNDD/2));
        button.setY(map.PY - mFloatMenu.mFloatMapManager.offset[1]- EasyTool.dip2px(mContext,ROUNDD/2));
        PositionX = map.PX;
        PositionY = map.PY;
        button.setPadding(0,0,0,0);
        button.setLayoutParams(mLayoutParams);
        button.setOnTouchListener(ButtonOnTouchListener);
        button.setOnClickListener(ButtonOnClickListener);
        button.setOnLongClickListener(ButtonOnLongClickListener);

        bt_sup.setBackground(mContext.getResources().getDrawable(R.drawable.pubg_watch));
        bt_sup.setX(map.FV1 - mFloatMenu.mFloatMapManager.offset[0]- EasyTool.dip2px(mContext,ROUNDD/2));
        bt_sup.setY(map.FV2 - mFloatMenu.mFloatMapManager.offset[1]- EasyTool.dip2px(mContext,ROUNDD/2));
        PositionX2 = map.FV1;
        PositionY2 = map.FV2;
        bt_sup.setPadding(0,0,0,0);
        bt_sup.setLayoutParams(mLayoutParams);
        bt_sup.setOnTouchListener(BSUPOnTouchListener);
        bt_sup.setOnClickListener(ButtonOnClickListener);
        bt_sup.setOnLongClickListener(BSUPOnLongClickListener);

        mRelativeLayout.addView(button);
        mRelativeLayout.addView(bt_sup);
        mFloatingManager.updateView(mRelativeLayout,mParams);
    }

    public void setText(String text)
    {
        button.setText(text);
    }

    public void Remove()
    {
        if(button != null)
        {
            mRelativeLayout.removeView(button);
            button = null;
        }

        if(bt_sup != null)
        {
            mRelativeLayout.removeView(bt_sup);
            bt_sup = null;
        }
    }

    public void Show()
    {
        if(button != null)
        {
            button.setVisibility(VISIBLE);
        }
    }

    public void Hide()
    {
        if(button != null)
        {
            button.setVisibility(View.GONE);
        }
    }

    View.OnLongClickListener ButtonOnLongClickListener = new View.OnLongClickListener(){


        @Override
        public boolean onLongClick(View v) {
            isLongClick = true;
            return true;
        }
    };

    View.OnLongClickListener BSUPOnLongClickListener = new View.OnLongClickListener(){


        @Override
        public boolean onLongClick(View v) {
            isLongClick2 = true;
            return true;
        }
    };

    View.OnClickListener ButtonOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {

            mFloatMenu.dbManager.showMapMouseAdapterView(map);
        }
    };


    View.OnTouchListener ButtonOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if(isLongClick)
            {
                PositionX = (int)event.getRawX();
                PositionY = (int)event.getRawY();
                button.setX(PositionX  - mFloatMenu.mFloatMapManager.offset[0] - EasyTool.dip2px(mContext,ROUNDD/2));
                button.setY(PositionY  - mFloatMenu.mFloatMapManager.offset[1] - EasyTool.dip2px(mContext,ROUNDD/2));
                map.PX = PositionX;
                map.PY = PositionY;
            }

            if(event.getAction() == MotionEvent.ACTION_UP)
            {
                isLongClick = false;
            }

            return false;
        }
    };

    View.OnTouchListener BSUPOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if(isLongClick2)
            {
                PositionX2 = (int)event.getRawX();
                PositionY2 = (int)event.getRawY();
                bt_sup.setX(PositionX2  - mFloatMenu.mFloatMapManager.offset[0] - EasyTool.dip2px(mContext,ROUNDD/2));
                bt_sup.setY(PositionY2 - mFloatMenu.mFloatMapManager.offset[1] - EasyTool.dip2px(mContext,ROUNDD/2));
                map.FV1 = PositionX2;
                map.FV2 = PositionY2;
            }

            if(event.getAction() == MotionEvent.ACTION_UP)
            {
                isLongClick2 = false;
            }

            return false;
        }
    };
}
