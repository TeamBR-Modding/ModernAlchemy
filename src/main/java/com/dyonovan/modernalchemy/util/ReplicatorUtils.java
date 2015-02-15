package com.dyonovan.modernalchemy.util;

import com.dyonovan.modernalchemy.ItemReplication;
import com.dyonovan.modernalchemy.helpers.LogHelper;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ReplicatorUtils {
    public static HashMap<String, Integer> values = new HashMap<String, Integer>();

    public static String fileDirectory;

    public static void buildDirectory(String folderLocation) {
        File dir = new File(folderLocation);
        dir.mkdirs();
        fileDirectory = folderLocation;
        moveJson();
        LogHelper.info("Building Replicator Values List");
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
                if(map != null) {
                    int num = 0;
                    LogHelper.info("Adding values for: " + modid);
                    for(Map.Entry<String, Integer> entry : map.entrySet()) {
                        values.put(entry.getKey(), entry.getValue());
                        num++;
                    }
                    LogHelper.info("Added " + num + " values for " + modid);
                }
                else
                    LogHelper.warning("Could not add " + modid + ".json");
            }
            LogHelper.info("Done with Replicator values");
        }
        else
            LogHelper.warning("No Files Found for Replicator Values");
    }

    public static int getValueForItem(ItemStack stack) {
        GameRegistry.UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(stack.getItem());
        HashMap<String, Integer> map = JsonUtils.readJson(id.modId);

        //Check our generated map first
        if(values != null) {
            if(values.containsKey(id.name + ":" + stack.getItemDamage())) {
                return values.get(id.name + ":" + stack.getItemDamage());
            }
            else if(values.containsKey(id.name))
                return values.get(id.name);
        }

        //Check from files
        else if(map != null) {
            values.putAll(map);

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
