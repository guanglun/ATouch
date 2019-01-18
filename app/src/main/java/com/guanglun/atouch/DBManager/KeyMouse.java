package com.guanglun.atouch.DBManager;

public class KeyMouse {

    public int ID;
    public String Name = null;
    public String Description = null;
    public int KeyCode;
    public int PX;
    public int PY;

    public void SetID(int id)
    {
        this.ID = id;
    }

    public void SetName(String name)
    {
        this.Name = name;
    }
    public void SetKeyCode(int kc)
    {
        this.KeyCode = kc;
    }
    public void SetDescription(String description)
    {
        this.Description = description;
    }
    public void SetPosition(int px,int py)
    {
        this.PX = px;
        this.PY = py;
    }

    public KeyMouse()
    {

    }

    public KeyMouse(int id,String name,String description,int kc,int px,int py)
    {
        this.ID = id;
        this.Name = name;
        this.Description = description;
        this.KeyCode = kc;
        this.PX = px;
        this.PY = py;
    }


}