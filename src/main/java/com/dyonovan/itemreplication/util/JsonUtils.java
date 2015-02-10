package com.dyonovan.itemreplication.util;

import com.dyonovan.itemreplication.ItemReplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.HashMap;

public class JsonUtils {

    public static boolean writeJson(HashMap<String, Integer> values, String modID) {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(values);

        try {
            FileWriter fw = new FileWriter(ItemReplication.configDir + modID);
            fw.write(json);
            fw.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static HashMap<String, Integer> readJson(String modID) {

        try {
            BufferedReader br = new BufferedReader(new FileReader(ItemReplication.configDir + modID));
            Gson gson = new Gson();
            HashMap<String, Integer> values = gson.fromJson(br, new TypeToken<HashMap<String, Integer>>(){}.getType());
            return values.size() == 0 ? null : values;
        } catch (FileNotFoundException e) {
            return null;
        }
    }
}
