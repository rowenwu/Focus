package com.pk.example;

import java.util.ArrayList;

public class Profile {


    public String name;

    public ArrayList<String> appsToBlock;

    public boolean active;

    //public Schedule[] schedules;

    public Profile(){

    }

    public Profile(String name, ArrayList<String> appsToBlock, boolean active){
        this.name = name;
        this.appsToBlock = appsToBlock;
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getAppsToBlock() {
        return appsToBlock;
    }

    public boolean getActive() {
        return active;
    }

}