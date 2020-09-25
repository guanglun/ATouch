package com.guanglun.atouch.DBManager;

public class KeyBoardCode {

    public String Name;
    public String Description;
    public int KeyCode;

    public KeyBoardCode()
    {
        this.Name = "";
        this.Description = "";
        this.KeyCode = 0;
    }

    public KeyBoardCode(String name, String description, int kc, int px, int py)
    {
        this.Name = name;
        this.Description = description;
        this.KeyCode = kc;
    }


}