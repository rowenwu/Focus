package com.pk.example;

public class Profile {


    public String name;

    public String[] appsToBlock;

    //public Schedule[] schedules;

    public Profile(){

    }

    public Profile(String name, String[] appsToBlock){
        this.name = name;
        this.appsToBlock = appsToBlock;
    }

    public String getName() {
        return name;
    }

    public String[] getAppsToBlock() {
        return appsToBlock;
    }

}
