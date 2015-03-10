package com.dyonovan.modernalchemy.util;

import com.dyonovan.modernalchemy.helpers.LogHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class JsonUtils {

    public static boolean writeJson(ArrayList<ReplicatorValues> values, String modID) {

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
    public static ArrayList<ReplicatorValues> readJson(String modID) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(ReplicatorUtils.fileDirectory + File.separator + modID + ".json"));

            Gson gson = new Gson();
            ArrayList<ReplicatorValues> values = gson.fromJson(br, new TypeToken<ArrayList<ReplicatorValues>>(){}.getType());
            return values;
        } catch (FileNotFoundException e) {
            LogHelper.severe(modID + ".json not Found!");
            return null;
        }
    }
}
