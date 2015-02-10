package com.dyonovan.itemreplication.util;

import java.io.File;
import java.util.HashMap;

public class ReplicatorUtils {

    public static ReplicatorUtils INSTANCE = new ReplicatorUtils();
    public static HashMap<String, Integer> values;

    private static String fileDirectory;

    public ReplicatorUtils() {
        values = new HashMap<String, Integer>();
    }

    public static void buildDirectory(String folderLocation) {
        File file = new File(folderLocation);
        file.mkdirs();
        fileDirectory = folderLocation;
        buildList();
    }

    public static void buildList() {
        File[] files = new File(fileDirectory).listFiles();
        if(files != null) {
            for(File file : files) {
                System.out.println("HELLO");
            }
        }
        else
            System.out.println("No Files Found for Replicator Values");
    }
}
