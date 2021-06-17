package com.cagneymoreau.fitlog.data;

import android.util.Pair;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import kotlin.Triple;

public class ListConverters {



    @TypeConverter
    public static ArrayList<String> fromString(String dbString)
    {
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        return new Gson().fromJson(dbString, listType);
    }

    @TypeConverter
    public static String  fromArrayList(ArrayList<String> list)
    {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static  ArrayList<ArrayList<String>> fromStringTwo(String dbString)
    {
        Type listType = new TypeToken<ArrayList<ArrayList<String>>>() {}.getType();
        return new Gson().fromJson(dbString, listType);
    }

    @TypeConverter
    public static String  fromArrayListTwo(ArrayList<ArrayList<String>> list)
    {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static ArrayList<Pair<String, Boolean>> fromStringThree(String dbString)
    {
        Type listType = new TypeToken<ArrayList<Pair<String, Boolean>>>() {}.getType();
        return new Gson().fromJson(dbString, listType);
    }

    @TypeConverter
    public static String  fromPairList(ArrayList<Pair<String, Boolean>> list)
    {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }


    @TypeConverter
    public static ArrayList<Integer> fromStringFour(String dbString)
    {
        Type listType = new TypeToken<ArrayList<Integer>>() {}.getType();
        return new Gson().fromJson(dbString, listType);
    }

    @TypeConverter
    public static String  fromIntList(ArrayList<Integer> list)
    {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }


    @TypeConverter
    public static ArrayList<Triple<Long, Long, Boolean>> fromStringFive(String dbString)
    {
        Type listType = new TypeToken<ArrayList<Triple<Long, Long, Boolean>>>() {}.getType();
        return new Gson().fromJson(dbString, listType);
    }

    @TypeConverter
    public static String  fromLongList(ArrayList<Triple<Long, Long, Boolean>> list)
    {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }


    @TypeConverter
    public static ArrayList<Pair<Integer, Integer>> fromStringSix(String dbString)
    {
        Type listType = new TypeToken<ArrayList<Pair<Integer, Integer>>>() {}.getType();
        return new Gson().fromJson(dbString, listType);
    }

    @TypeConverter
    public static String  fromIntPairList(ArrayList<Pair<Integer, Integer>> list)
    {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }




}
