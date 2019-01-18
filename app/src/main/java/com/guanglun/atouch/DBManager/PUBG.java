package com.guanglun.atouch.DBManager;

public class PUBG {

    public String N1_Name = null;
    public String N2_Description = null;
    public int N3_AttackX = 400,N4_AttackY = 400;
    public int N5_MoveX = 400,N6_MoveY = 400;
    public int N7_JumpX = 400,N8_JumpY = 400;
    public int N9_SquatX = 400,N10_SquatY = 400;
    public int N11_LieX = 400,N12_LieY = 400;
    public int N13_FaceX = 400,N14_FaceY = 400;
    public int N15_WatchX = 400,N16_WatchY = 400;
    public int N17_PackageX = 400,N18_PackageY = 400;
    public int N19_ArmsLeftX = 400,N20_ArmsLeftY = 400;
    public int N21_ArmsRightX = 400,N22_ArmsRightY = 400;
    public int N23_MapX = 400,N24_MapY = 400;

    public void SetName(String name)
    {
        this.N1_Name = name;
    }
    public void SetDescription(String description)
    {
        this.N2_Description = description;
    }
    public void SetAttack(int px,int py) { this.N3_AttackX = px;this.N4_AttackY = py; }
    public void SetMove(int px,int py) { this.N5_MoveX = px;this.N6_MoveY = py; }
    public void SetJump(int px,int py) { this.N7_JumpX = px;this.N8_JumpY = py; }
    public void SetSquat(int px,int py) { this.N9_SquatX = px;this.N10_SquatY = py; }
    public void SetLie(int px,int py) { this.N11_LieX = px;this.N12_LieY = py; }
    public void SetFace(int px,int py) { this.N13_FaceX = px;this.N14_FaceY = py; }
    public void SetWatch(int px,int py) { this.N15_WatchX = px;this.N16_WatchY = py; }
    public void SetPackage(int px,int py) { this.N17_PackageX = px;this.N18_PackageY = py; }
    public void SetArmsLeft(int px,int py) { this.N19_ArmsLeftX = px;this.N20_ArmsLeftY = py; }
    public void SetArmsRight(int px,int py) { this.N21_ArmsRightX = px;this.N22_ArmsRightY = py; }
    public void SetMap(int px,int py) { this.N23_MapX = px;this.N24_MapY = py; }

}
