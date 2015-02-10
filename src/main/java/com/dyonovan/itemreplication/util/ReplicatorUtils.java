package com.dyonovan.itemreplication.util;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;

import java.io.File;
import java.util.HashMap;

public class ReplicatorUtils {
    public static HashMap<String, Integer> values;

    public static String fileDirectory;

    public ReplicatorUtils() {
        values = new HashMap<String, Integer>();
    }

    public static void buildDirectory(String folderLocation) {
        File file = new File(folderLocation);
        file.mkdirs();
        fileDirectory = folderLocation;
        //buildList();
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
