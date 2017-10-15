//package com.pk.example.entity;
//
//import com.pk.example.Profile;
//
//import android.arch.persistence.room.Entity;
//import android.arch.persistence.room.PrimaryKey;
//
//@Entity
//public class ProfileEntity extends Profile {
//
//    @PrimaryKey
//    public String name;
//
//    public String[] appsToBlock;
//
//    @Override
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    @Override
//    public String[] getAppsToBlock() {
//        return appsToBlock;
//    }
//
//    public void setAppsToBlock(String[] appsToBlock) {
//        this.appsToBlock = appsToBlock;
//    }
//
//    public ProfileEntity(){
//    }
//
//    public ProfileEntity(Profile profile){
//        this.name = profile.getName();
//        this.appsToBlock = profile.getAppsToBlock();
//    }
//
//}
