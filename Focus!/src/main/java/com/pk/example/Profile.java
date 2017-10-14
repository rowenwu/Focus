package com.pk.example;

import java.util.ArrayList;

public class Profile {


    private String name;

    private ArrayList<String> appsToBlock;

    //public Schedule[] schedules;

    public Profile(){

    }

    public Profile(String name, ArrayList<String> appsToBlock){
        this.name = name;
        this.appsToBlock = appsToBlock;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getAppsToBlock() {
        return appsToBlock;
    }

}
