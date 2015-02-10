package com.dyonovan.itemreplication.util;

import java.util.HashMap;

public class ReplicatorUtils {
    public ReplicatorUtils INSTANCE = new ReplicatorUtils();
    public static HashMap<String, Integer> values;

    public ReplicatorUtils() {
        values = new HashMap<String, Integer>();
    }

    public void addItemWithValue(String item, int value) {
        values.put(item, value);
    }
}
