package com.dyonovan.modernalchemy.crafting;

import com.dyonovan.modernalchemy.helpers.LogHelper;
import com.dyonovan.modernalchemy.util.InventoryUtils;
import com.google.common.collect.Lists;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

public class AdvancedCrafterRecipeRegistry {

    /**
     * The instance of the recipies
     */
    public static AdvancedCrafterRecipeRegistry instance = new AdvancedCrafterRecipeRegistry();

    /**
     * The stored recipes
     */
    public List<RecipeAdvancedCrafter> recipes = new ArrayList<RecipeAdvancedCrafter>();

    /**
     * Adds a recipe to the registry if it doesn't already have a value
     * @param itemArray  Input item array
     * @param output the Resulting Item
     */
    public void addRecipe(List<Object> itemArray, Object output, int processTime, int requiredMode) {
        Collections.sort(itemArray,InventoryUtils.itemStackComparator);

        if (!recipes.contains(new RecipeAdvancedCrafter(itemArray, output,  processTime, requiredMode)))
            recipes.add(new RecipeAdvancedCrafter(itemArray, output, processTime, requiredMode));
        else
            LogHelper.warning("Someone tried to add a recipe for " + output + " when it already exists. Aborting.");
    }

    /**
     * Creates a recipe that can contain ore dict strings
     * @param itemArray Items and Strings for ore dict stuff
     * @param output The output {@link net.minecraft.item.Item}
     * @param processTime How long in ticks
     * @param requiredMode Which mode is needed
     */
    public void addOreDictRecipe(List<Object> itemArray, Object output, int processTime, int requiredMode) {
        List<Object> newInput = new ArrayList<Object>();

        for(Object obj : itemArray) {
            if (obj instanceof ItemStack)
                newInput.add(obj);
            else if (obj instanceof Item)
                newInput.add(new ItemStack((Item) obj));
            else if (obj instanceof OreDictStack) {
                if (OreDictionary.getOres(((OreDictStack)obj).oreId) != null) {
                    newInput.add(obj);
                } else {
                    LogHelper.severe("No ore dictionary value exists for: " + obj + ". Aborting");
                    return;
                }
            }
        }
        addRecipe(newInput, output, processTime, requiredMode);
    }

    /**
     * Check if item is valid for crafting
     * @param input The item to check {@link net.minecraft.item.Item}
     * @return True if it is
     */
    public boolean checkInput(Item input) {
        for(RecipeAdvancedCrafter recipe : recipes) {
            if (recipe.getInput().contains(input)) return true;
        }
        return false;
    }
}
