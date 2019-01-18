package com.guanglun.atouch.Floating;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.guanglun.atouch.DBManager.DBControl;
import com.guanglun.atouch.DBManager.DBControlPUBG;
import com.guanglun.atouch.DBManager.KeyMouse;
import com.guanglun.atouch.DBManager.PUBG;
import com.guanglun.atouch.R;

import java.util.ArrayList;
import java.util.List;

public class FloatPUBGManager {

    private Context mContext;
    private FloatingManager mFloatingManager;
    private RelativeLayout mRelativeLayout;
    private WindowManager.LayoutParams mParams;
    private int ScreenWidth,ScreenHigh;

    public int N3_AttackX,N4_AttackY;
    public int N5_MoveX,N6_MoveY;
    public int N7_JumpX,N8_JumpY;
    public int N9_SquatX,N10_SquatY;
    public int N11_LieX,N12_LieY;
    public int N13_FaceX,N14_FaceY;
    public int N15_WatchX,N16_WatchY;
    public int N17_PackageX,N18_PackageY;
    public int N19_ArmsLeftX,N20_ArmsLeftY;
    public int N21_ArmsRightX,N22_ArmsRightY;
    public int N23_MapX,N24_MapY;

    private final String DEBUG_TAG = "FloatPUBGManager";

    FloatButtonPUBG bt_attack,bt_move,bt_jump,bt_squat,bt_lie,bt_face,bt_watch,
    bt_package,bt_armsleft,bt_armsright,bt_map;


    public FloatPUBGManager(Context context, FloatingManager floatingmanager, RelativeLayout relativeLayout, WindowManager.LayoutParams params)
    {
        mContext = context;
        mFloatingManager = floatingmanager;
        mRelativeLayout = relativeLayout;
        mParams = params;

        WindowManager mWindowManager  = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(metrics);
        ScreenWidth = metrics.widthPixels;//获取到的是px，像素，绝对像素，需要转化为dpi
        ScreenHigh = metrics.heightPixels;
    }

    public void Show(PUBG pubg)
    {
        //攻击
        bt_attack = new FloatButtonPUBG(mContext,mFloatingManager,mRelativeLayout,mParams,
                pubg.N3_AttackX,pubg.N4_AttackY,R.drawable.pubg_attack);
        //移动
        bt_move = new FloatButtonPUBG(mContext,mFloatingManager,mRelativeLayout,mParams,
                pubg.N5_MoveX,pubg.N6_MoveY,R.drawable.pubg_move);
        //跳跃
        bt_jump = new FloatButtonPUBG(mContext,mFloatingManager,mRelativeLayout,mParams,
                pubg.N7_JumpX,pubg.N8_JumpY,R.drawable.pubg_jump);
        //蹲下
        bt_squat = new FloatButtonPUBG(mContext,mFloatingManager,mRelativeLayout,mParams,
                pubg.N9_SquatX,pubg.N10_SquatY,R.drawable.pubg_squat);

        //趴下
        bt_lie = new FloatButtonPUBG(mContext,mFloatingManager,mRelativeLayout,mParams,
                pubg.N11_LieX,pubg.N12_LieY,R.drawable.pubg_lie);

        //朝向
        bt_face = new FloatButtonPUBG(mContext,mFloatingManager,mRelativeLayout,mParams,
                pubg.N13_FaceX,pubg.N14_FaceY,R.drawable.pubg_face);

        //观察
        bt_watch = new FloatButtonPUBG(mContext,mFloatingManager,mRelativeLayout,mParams,
                pubg.N15_WatchX,pubg.N16_WatchY,R.drawable.pubg_watch);

        //背包
        bt_package = new FloatButtonPUBG(mContext,mFloatingManager,mRelativeLayout,mParams,
                pubg.N17_PackageX,pubg.N18_PackageY,R.drawable.pubg_package);

        //左武器
        bt_armsleft = new FloatButtonPUBG(mContext,mFloatingManager,mRelativeLayout,mParams,
                pubg.N19_ArmsLeftX,pubg.N20_ArmsLeftY,R.drawable.pubg_armsleft);

        //右武器
        bt_armsright = new FloatButtonPUBG(mContext,mFloatingManager,mRelativeLayout,mParams,
                pubg.N21_ArmsRightX,pubg.N22_ArmsRightY,R.drawable.pubg_armsright);

        //右武器
        bt_map = new FloatButtonPUBG(mContext,mFloatingManager,mRelativeLayout,mParams,
                pubg.N23_MapX,pubg.N24_MapY,R.drawable.pubg_map);
    }

    public void RemoveAll()
    {
        if(bt_attack != null)
        {

            Log.i(DEBUG_TAG, "RemoveAll");
            bt_attack.Remove();
            bt_move.Remove();
            bt_jump.Remove();
            bt_squat.Remove();
            bt_lie.Remove();
            bt_face.Remove();
            bt_watch.Remove();
            bt_package.Remove();
            bt_armsleft.Remove();
            bt_armsright.Remove();
            bt_map.Remove();

            bt_attack = null;
        }

    }

    public void Save(String Name,DBControlPUBG mDBControlPUBG){

        PUBG pubg = new PUBG();
        pubg.SetName(Name);
        pubg.SetDescription("NULL");
        pubg.SetAttack(bt_attack.PositionX,bt_attack.PositionY);
        pubg.SetMove(bt_move.PositionX,bt_move.PositionY);
        pubg.SetJump(bt_jump.PositionX,bt_jump.PositionY);
        pubg.SetSquat(bt_squat.PositionX,bt_squat.PositionY);
        pubg.SetLie(bt_lie.PositionX,bt_lie.PositionY);
        pubg.SetFace(bt_face.PositionX,bt_face.PositionY);
        pubg.SetWatch(bt_watch.PositionX,bt_watch.PositionY);
        pubg.SetPackage(bt_package.PositionX,bt_package.PositionY);
        pubg.SetArmsLeft(bt_armsleft.PositionX,bt_armsleft.PositionY);
        pubg.SetArmsRight(bt_armsright.PositionX,bt_armsright.PositionY);
        pubg.SetMap(bt_map.PositionX,bt_map.PositionY);

        mDBControlPUBG.InsertDatabase(pubg);

    }

}
