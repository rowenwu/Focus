package com.pk.example.converter;

/**
 * Created by Andy on 10/14/2017.
 */

import android.arch.persistence.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;


public class DateArrayConverter {
    @TypeConverter
    public static ArrayList<Date> fromString(String value) {

        //Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        //return new Gson().fromJson(value, listType);
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        ArrayList<Long> dateLongList = new Gson().fromJson(value, listType);

        ArrayList<Date> dateArrayList = new ArrayList<>();
        for(int i=0; i < dateLongList.size(); i++) {
            dateArrayList.add(new Date(dateLongList.get(i)));
        }

        return dateArrayList;
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<Date> list) {
        //convert date to long
        ArrayList<Long> dateLongList = new ArrayList<>();
        for(int i=0; i < list.size(); i++) {
            dateLongList.add(list.get(i).getTime());
        }

        Gson gson = new Gson();
        String json = gson.toJson(dateLongList);
        return json;
    }
}
