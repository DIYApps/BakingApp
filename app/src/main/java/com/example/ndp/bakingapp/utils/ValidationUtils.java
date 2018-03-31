package com.example.ndp.bakingapp.utils;

import java.util.List;

public class ValidationUtils {

    public static boolean isStringEmptyOrNull(String str){
        return str == null || str.isEmpty();
    }

    public static boolean isListEmptyOrNull(List list){
        return list == null || list.isEmpty();
    }
}
