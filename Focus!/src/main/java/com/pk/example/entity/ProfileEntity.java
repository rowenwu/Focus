package com.pk.example.entity;

import com.pk.example.Profile;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.ArrayList;

@Entity
public class ProfileEntity extends Profile {

    @PrimaryKey
    private String _name;
    private ArrayList<String> _appsToBlock;

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    @Override
    public ArrayList<String> getAppsToBlock() {
        return _appsToBlock;
    }

    public void setAppsToBlock(ArrayList<String> appsToBlock) {
        this._appsToBlock = appsToBlock;
    }

    public ProfileEntity(){
    }

    public ProfileEntity(Profile profile){
        this._name = profile.getName();
        this._appsToBlock = profile.getAppsToBlock();
    }

}
