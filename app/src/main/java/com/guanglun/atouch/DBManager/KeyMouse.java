package com.guanglun.atouch.DBManager;

public class KeyMouse {

    public int ID;
    public String Name = null;
    public String Description = null;

    public void SetID(int id)
    {
        this.ID = id;
    }

    public void SetName(String name)
    {
        this.Name = name;
    }

    public void SetDescription(String description)
    {
        this.Description = description;
    }

    public KeyMouse()
    {

    }

    public KeyMouse(int id,String name,String description)
    {
        this.ID = id;
        this.Name = name;
        this.Description = description;
    }


}
