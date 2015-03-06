package com.dyonovan.modernalchemy.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class JsonUtils {

    public static boolean writeJson(HashMap<String, Integer> values, String modID) {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(values);

        try {
            FileWriter fw = new FileWriter(ReplicatorUtils.fileDirectory + File.separator + modID + ".json");
            fw.write(json);
            fw.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public static HashMap<String, Integer> readJson(String modID) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(ReplicatorUtils.fileDirectory + File.separator + modID + ".json"));

            Gson gson = new Gson();
            Map<String, Integer> map = gson.fromJson(br, new TypeToken<Map<String, Integer>>(){}.getType());
            if (map != null) {
                HashMap<String, Integer> hashMap = (map instanceof HashMap) ? (HashMap) map : new HashMap<>(map);
                return hashMap.size() == 0 ? null : hashMap;
            } else {
                return new HashMap<>();
            }
        } catch (FileNotFoundException e) {
            return new HashMap<>();
        }
    }
}
