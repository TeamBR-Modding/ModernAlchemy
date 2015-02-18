package com.dyonovan.modernalchemy.util;

import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.helpers.LogHelper;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
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
            URL jsonUrl = ModernAlchemy.class.getResource("/minecraft.json");
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

        //Check our generated map first
        if(values != null) {
            if(values.containsKey(id.name + ":" + stack.getItemDamage())) {
                return values.get(id.name + ":" + stack.getItemDamage());
            }
            else if(values.containsKey(id.name))
                return values.get(id.name);
        }

        HashMap<String, Integer> map = JsonUtils.readJson(id.modId);

        //Check from files
       if(map != null && map.size() > 0) {
            values.putAll(map);

            if(map.containsKey(id.name + ":" + stack.getItemDamage())) {
                return map.get(id.name + ":" + stack.getItemDamage());
            }
            else if(map.containsKey(id.name))
                return map.get(id.name);
            else
                return 0;
        }
        return -1;
    }

    public static ItemStack getReturn(String item) {
        ItemStack stack;
        String itemReturn[] = item.split(":");
        if (GameRegistry.findItem(itemReturn[0], itemReturn[1]) != null) {
            Item objReturn = GameRegistry.findItem(itemReturn[0], itemReturn[1]);
                stack = new ItemStack(objReturn, 1, Integer.valueOf(itemReturn[2]));
        } else {
            Block objReturn = GameRegistry.findBlock(itemReturn[0], itemReturn[1]);
                stack = new ItemStack(Item.getItemFromBlock(objReturn), 1, Integer.valueOf(itemReturn[2]));
        }
        return stack;
    }
}
