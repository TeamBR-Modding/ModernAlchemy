package com.dyonovan.modernalchemy.crafting;

import com.dyonovan.modernalchemy.helpers.LogHelper;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class MAFurnaceRecipeRegistry {

    /**
     * The instance of the recipies
     */
    public static MAFurnaceRecipeRegistry instance = new MAFurnaceRecipeRegistry();

    /**
     * The stored recipes
     */
    public List<RecipeMAFurnace> recipes = new ArrayList<RecipeMAFurnace>();

    /**
     * Adds a recipe to the registry if it doesn't already have a value
     *
     * @param itemArray  Input item array
     * @param output the Resulting Item
     */
    public void addRecipe(ArrayList<Item> itemArray, Item output) {
        if (!recipes.contains(new RecipeMAFurnace(itemArray, output)))
            recipes.add(new RecipeMAFurnace(itemArray, output));
        else
            LogHelper.warning("Someone tried to add a recipe for " + output.getUnlocalizedName() + " when it already exists. Aborting.");
    }

    /**
     * Check if item is valid for crafting
     * @param input The item to check {@link net.minecraft.item.Item}
     * @return True if it is
     */
    public boolean checkInput(Item input) {
        for(RecipeMAFurnace recipe : recipes) {
            if (recipe.getInput().contains(input)) return true;
        }
        return false;
    }
}
