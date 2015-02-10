package com.dyonovan.itemreplication.util;

import com.dyonovan.itemreplication.ItemReplication;
import com.dyonovan.itemreplication.lib.Constants;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

public class ReplicatorUtils {
    public static HashMap<String, Integer> values;

    public static String fileDirectory;

    public ReplicatorUtils() {
        values = new HashMap<String, Integer>();
    }

    public static void buildDirectory(String folderLocation) {
        File dir = new File(folderLocation);
        dir.mkdirs();
        fileDirectory = folderLocation;
        moveJson();
        buildList();
    }

    private static void moveJson() {
        File file = new File(fileDirectory + File.separator + "minecraft.json");
        if (!file.exists()) {
            URL jsonUrl = ItemReplication.class.getResource("/minecraft.json");
            try {
                FileUtils.copyURLToFile(jsonUrl, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void buildList() {
        File[] files = new File(fileDirectory).listFiles();
        if(files != null) {
            for(File file : files) {
                String modid = file.getName().substring(0, file.getName().length() - 5);
                HashMap<String, Integer> map = JsonUtils.readJson(modid);
                if(map != null)
                    values.putAll(map);
                else
                    System.out.println("Could not add " + modid + ".json");
            }
        }
        else
            System.out.println("No Files Found for Replicator Values");
    }

    public static int getValueForItem(ItemStack stack) {
        GameRegistry.UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(stack.getItem());
        HashMap<String, Integer> map = JsonUtils.readJson(id.modId);
        if(map != null) {
            if(map.containsKey(id.name + ":" + stack.getItemDamage())) {
                return map.get(id.name + ":" + stack.getItemDamage());
            }
            else if(map.containsKey(id.name))
                return map.get(id.name);
            else
                return 0;
        }
        return 0;
    }
}
