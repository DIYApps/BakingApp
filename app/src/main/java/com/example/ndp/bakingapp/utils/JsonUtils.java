package com.example.ndp.bakingapp.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtils {

    //parse the json and return Object of class T
    public  static <T>T parseJson(String jsonString ,Class<T> cls){
        if (jsonString == null || jsonString.isEmpty()) {
            return null;
        }

        //create GSON builder to parse json
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.fromJson(jsonString, cls);
    }
}
