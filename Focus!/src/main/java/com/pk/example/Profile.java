package com.pk.example;

//import android.arch.persistence.room.Entity;
//import android.arch.persistence.room.PrimaryKey;

public class Profile {

//    @PrimaryKey
//    int id;
    public String name;
    public String[] appsToBlock;

    public Profile(String name, String[] appsToBlock){
        this.name = name;
        this.appsToBlock = appsToBlock;
    }

}
