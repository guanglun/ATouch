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
    bt_package,bt_armsleft,bt_armsright,bt_map,bt_aim,bt_checkpackage,bt_door,bt_drive,
    bt_getoff,bt_grenade,bt_medicine,bt_reload,bt_save,bt_sprint,bt_follow,bt_pick,bt_pick1,
    bt_pick2,bt_pick3,bt_ride;


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
/****/
        //瞄准
        bt_aim = new FloatButtonPUBG(mContext,mFloatingManager,mRelativeLayout,mParams,
                pubg.N25_AimX,pubg.N26_AimY,R.drawable.pubg_aim);

        //舔包
        bt_checkpackage = new FloatButtonPUBG(mContext,mFloatingManager,mRelativeLayout,mParams,
                pubg.N27_CheckPackageX,pubg.N28_CheckPackageY,R.drawable.pubg_checkpackage);

        //开门
        bt_door = new FloatButtonPUBG(mContext,mFloatingManager,mRelativeLayout,mParams,
                pubg.N29_DoorX,pubg.N30_DoorY,R.drawable.pubg_door);

        //驾驶
        bt_drive = new FloatButtonPUBG(mContext,mFloatingManager,mRelativeLayout,mParams,
                pubg.N31_DriveX,pubg.N32_DriveY,R.drawable.pubg_drive);

        //下车
        bt_getoff = new FloatButtonPUBG(mContext,mFloatingManager,mRelativeLayout,mParams,
                pubg.N33_GetOffX,pubg.N34_GetOffY,R.drawable.pubg_getoff);

        //手雷
        bt_grenade = new FloatButtonPUBG(mContext,mFloatingManager,mRelativeLayout,mParams,
                pubg.N35_GrenadeX,pubg.N36_GrenadeY,R.drawable.pubg_grenade);

        //用药
        bt_medicine = new FloatButtonPUBG(mContext,mFloatingManager,mRelativeLayout,mParams,
                pubg.N37_MedicineX,pubg.N38_MedicineY,R.drawable.pubg_medicine);

        //重装
        bt_reload = new FloatButtonPUBG(mContext,mFloatingManager,mRelativeLayout,mParams,
                pubg.N39_ReloadX,pubg.N40_ReloadY,R.drawable.pubg_reload);

        //救援
        bt_save = new FloatButtonPUBG(mContext,mFloatingManager,mRelativeLayout,mParams,
                pubg.N41_SaveX,pubg.N42_SaveY,R.drawable.pubg_save);

        //冲刺
        bt_sprint = new FloatButtonPUBG(mContext,mFloatingManager,mRelativeLayout,mParams,
                pubg.N43_SprintX,pubg.N44_SprintY,R.drawable.pubg_sprint);
        //跳伞跟随
        bt_follow = new FloatButtonPUBG(mContext,mFloatingManager,mRelativeLayout,mParams,
                pubg.N45_FollowX,pubg.N46_FollowY,R.drawable.pubg_follow);

        //拾取
        bt_pick = new FloatButtonPUBG(mContext,mFloatingManager,mRelativeLayout,mParams,
                pubg.N47_PickX,pubg.N48_PickY,R.drawable.pubg_pick);

        //拾取
        bt_ride = new FloatButtonPUBG(mContext,mFloatingManager,mRelativeLayout,mParams,
                pubg.N49_RideX,pubg.N50_RideY,R.drawable.pubg_ride);

        //拾取1
        bt_pick1 = new FloatButtonPUBG(mContext,mFloatingManager,mRelativeLayout,mParams,
                pubg.N51_Pick1X,pubg.N52_Pick1Y,R.drawable.pubg_pick1);

        //拾取2
        bt_pick2 = new FloatButtonPUBG(mContext,mFloatingManager,mRelativeLayout,mParams,
                pubg.N53_Pick2X,pubg.N54_Pick2Y,R.drawable.pubg_pick2);

        //拾取3
        bt_pick3 = new FloatButtonPUBG(mContext,mFloatingManager,mRelativeLayout,mParams,
                pubg.N55_Pick3X,pubg.N56_Pick3Y,R.drawable.pubg_pick3);

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


            bt_aim.Remove();
            bt_checkpackage.Remove();
            bt_door.Remove();
            bt_drive.Remove();
            bt_getoff.Remove();
            bt_grenade.Remove();
            bt_medicine.Remove();
            bt_reload.Remove();
            bt_save.Remove();
            bt_sprint.Remove();
            bt_follow.Remove();
            bt_pick.Remove();
            bt_pick1.Remove();
            bt_pick2.Remove();
            bt_pick3.Remove();
            bt_ride.Remove();

            bt_attack = null;
        }

    }

    public void HideAll()
    {

        Log.i(DEBUG_TAG, "HideAll");
        bt_attack.Hide();
        bt_move.Hide();
        bt_jump.Hide();
        bt_squat.Hide();
        bt_lie.Hide();
        bt_face.Hide();
        bt_watch.Hide();
        bt_package.Hide();
        bt_armsleft.Hide();
        bt_armsright.Hide();
        bt_map.Hide();

        bt_aim.Hide();
        bt_checkpackage.Hide();
        bt_door.Hide();
        bt_drive.Hide();
        bt_getoff.Hide();
        bt_grenade.Hide();
        bt_medicine.Hide();
        bt_reload.Hide();
        bt_save.Hide();
        bt_sprint.Hide();
        bt_follow.Hide();
        bt_pick.Hide();
        bt_pick1.Hide();
        bt_pick2.Hide();
        bt_pick3.Hide();
        bt_ride.Hide();
    }

    public void ShowAll()
    {

        Log.i(DEBUG_TAG, "HideAll");
        bt_attack.Show();
        bt_move.Show();
        bt_jump.Show();
        bt_squat.Show();
        bt_lie.Show();
        bt_face.Show();
        bt_watch.Show();
        bt_package.Show();
        bt_armsleft.Show();
        bt_armsright.Show();
        bt_map.Show();

        bt_aim.Show();
        bt_checkpackage.Show();
        bt_door.Show();
        bt_drive.Show();
        bt_getoff.Show();
        bt_grenade.Show();
        bt_medicine.Show();
        bt_reload.Show();
        bt_save.Show();
        bt_sprint.Show();
        bt_follow.Show();
        bt_pick.Show();
        bt_pick1.Show();
        bt_pick2.Show();
        bt_pick3.Show();
        bt_ride.Show();

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

        bt_aim.Show();
        bt_checkpackage.Show();
        bt_door.Show();
        bt_drive.Show();
        bt_getoff.Show();
        bt_grenade.Show();
        bt_medicine.Show();
        bt_reload.Show();
        bt_save.Show();
        bt_sprint.Show();
        bt_follow.Show();
        bt_pick.Show();
        bt_pick1.Show();
        bt_pick2.Show();
        bt_pick3.Show();
        bt_ride.Show();

        pubg.SetAim(bt_aim.PositionX,bt_aim.PositionY);
        pubg.SetCheckPackage(bt_checkpackage.PositionX,bt_checkpackage.PositionY);
        pubg.SetDoor(bt_door.PositionX,bt_door.PositionY);
        pubg.SetDrive(bt_drive.PositionX,bt_drive.PositionY);
        pubg.SetGetOff(bt_getoff.PositionX,bt_getoff.PositionY);
        pubg.SetGrenade(bt_grenade.PositionX,bt_grenade.PositionY);
        pubg.SetMedicine(bt_medicine.PositionX,bt_medicine.PositionY);
        pubg.SetReload(bt_reload.PositionX,bt_reload.PositionY);
        pubg.SetSave(bt_save.PositionX,bt_save.PositionY);
        pubg.SetSprint(bt_sprint.PositionX,bt_sprint.PositionY);
        pubg.SetFollow(bt_follow.PositionX,bt_follow.PositionY);
        pubg.SetPick(bt_pick.PositionX,bt_pick.PositionY);
        pubg.SetRide(bt_ride.PositionX,bt_ride.PositionY);
        pubg.SetPick1(bt_pick1.PositionX,bt_pick1.PositionY);
        pubg.SetPick2(bt_pick2.PositionX,bt_pick2.PositionY);
        pubg.SetPick3(bt_pick3.PositionX,bt_pick3.PositionY);


        Log.i(DEBUG_TAG, "AttackX:"+pubg.N3_AttackX+" AttackY:"+pubg.N4_AttackY);

        mDBControlPUBG.InsertDatabase(pubg);

    }

}
