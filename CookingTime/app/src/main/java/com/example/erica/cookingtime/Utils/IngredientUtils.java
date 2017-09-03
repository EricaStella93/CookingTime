package com.example.erica.cookingtime.Utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class IngredientUtils {

    public static Date fromStringToDate(String string){
        SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy");
        Date date = null;
        try{
            date = ft.parse(string);
        }catch (ParseException e){
            Log.e("PARSEEXC", "DATEPARSINGFAILED");
        }
        return date;
    }

    public static String fromDateToString(Date date){
        SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy");
        return ft.format(date);
    }

    public static boolean fromIntToBool(int a){
        if(a == 0){
            return false;
        }
        return true;
    }

    public static int fromBoolToInt(boolean b){
        if(b){
            return 1;
        }
        return 0;
    }

    public static ArrayList<Integer> indexOfAll(Object obj, ArrayList list){
        ArrayList<Integer> indexList = new ArrayList<Integer>();
        for (int i = 0; i < list.size(); i++)
            if(obj.equals(list.get(i)))
                indexList.add(i);
        return indexList;
    }

    public static int recipeIDFromString(String id){
        String[] split = id.split("-");
        return Integer.parseInt(split[split.length -1]);
    }
}
