package com.dyonovan.modernalchemy.crafting;

import com.dyonovan.modernalchemy.helpers.LogHelper;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
     *
     * @param itemArray  Input item array
     * @param output the Resulting Item
     */
    public void addRecipe(ArrayList<Item> itemArray, Item output, int qty, int processTime, int requiredMode) {
        Collections.sort(itemArray, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return o1.getUnlocalizedName().compareTo(o2.getUnlocalizedName());
            }
        });

        if (!recipes.contains(new RecipeAdvancedCrafter(itemArray, output, qty, processTime, requiredMode)))
            recipes.add(new RecipeAdvancedCrafter(itemArray, output, qty, processTime, requiredMode));
        else
            LogHelper.warning("Someone tried to add a recipe for " + output.getUnlocalizedName() + " when it already exists. Aborting.");
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
