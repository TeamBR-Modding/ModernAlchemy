package com.dyonovan.modernalchemy.util;

import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.collections.Couple;
import com.dyonovan.modernalchemy.crafting.CraftingRecipeHelper;
import com.dyonovan.modernalchemy.handlers.ConfigHandler;
import com.dyonovan.modernalchemy.helpers.LogHelper;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
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
        if (stack == null)
            return -1;
        GameRegistry.UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(stack.getItem());

        //Check our generated map first
        if (values != null) {
            if (values.containsKey(id.name + ":" + stack.getItemDamage())) {
                return values.get(id.name + ":" + stack.getItemDamage());
            } else if (values.containsKey(id.name))
                return values.get(id.name);
        }

        HashMap<String, Integer> map = JsonUtils.readJson(id.modId);

        //Check from files
        if (map != null && map.size() > 0) {
            values.putAll(map);

            if (map.containsKey(id.name + ":" + stack.getItemDamage())) {
                return map.get(id.name + ":" + stack.getItemDamage());
            } else if (map.containsKey(id.name))
                return map.get(id.name);
        }

        //Check crafting
        int value = getValueFromRecipe(stack, 0);
        if(value > -1)
            return value;

        return -1;
    }

    public static int getValueFromRecipe(ItemStack inputStack, int depth) {
        if(depth > 10) //Too deep
            return -1;
        if(inputStack == null) //Nothing
            return 0;
        int shallowValue = getShallowValue(inputStack);
        if(shallowValue > -1) { //If its registered (Our end point hopefully)
            return shallowValue;
        }
        else if(CraftingRecipeHelper.getRecipe(inputStack) != null) {
            Couple<ItemStack[], IRecipe> recipe = CraftingRecipeHelper.getRecipe(inputStack);
            GameRegistry.UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(inputStack.getItem());
            int sum = 0;

            for (ItemStack stack : recipe.getA()) {
                int value = getValueFromRecipe(stack, ++depth);
                if (value > -1)
                    sum += value;
                else return -1;
            }

            if(ConfigHandler.debugMode)
                LogHelper.info("Adding value for: " + inputStack.getDisplayName() + " - " + (sum / recipe.getB().getRecipeOutput().stackSize));
            values.put(id.name + ":" + inputStack.getItemDamage(), (sum / recipe.getB().getRecipeOutput().stackSize));
            return sum / recipe.getB().getRecipeOutput().stackSize;
        }
        return -1;
    }

    public static int getShallowValue(ItemStack stack) {
        GameRegistry.UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(stack.getItem());
        if (values != null) {
            if (values.containsKey(id.name + ":" + stack.getItemDamage())) {
                return values.get(id.name + ":" + stack.getItemDamage());
            } else if (values.containsKey(id.name))
                return values.get(id.name);
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
