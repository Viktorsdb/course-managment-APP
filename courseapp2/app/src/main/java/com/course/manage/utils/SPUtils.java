package com.course.manage.utils;

import android.content.Context;

import com.course.manage.App;

public class SPUtils {

    public static String getString(String key){
        return App.app.getSharedPreferences("course", Context.MODE_PRIVATE).getString(key, "");
    }

    public static int getInt(String key){
        return App.app.getSharedPreferences("course", Context.MODE_PRIVATE).getInt(key, -1);
    }

    public static void setString(String key,String value){
        App.app.getSharedPreferences("course", Context.MODE_PRIVATE).edit().putString(key,value).commit();
    }

    public static void setInt(String key,int value){
        App.app.getSharedPreferences("course", Context.MODE_PRIVATE).edit().putInt(key,value).commit();
    }
}
