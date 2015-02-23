package com.dyonovan.modernalchemy.crafting;

import net.minecraft.item.Item;

import java.util.HashMap;
import java.util.Map;

public class RecipeArcFurnace {
    public static final RecipeArcFurnace instance = new RecipeArcFurnace();

    private Map<String, Integer> recipies = new HashMap<String, Integer>();

    /**
     * Add a recipe to the Arc Furnace
     * @param item The input item
     * @param value the amount of Actinium to return
     */
    public void addRecipe(Item item, int value){
        recipies.put(item.getUnlocalizedName(), value);
    }

    /**
     * Get the return value of the item
     * @param item Input
     * @return mb of Actinium
     */
    public int getRecipeOutput(Item item) {
        if(recipies.containsKey(item.getUnlocalizedName()))
            return recipies.get(item.getUnlocalizedName());
        else
            return 0;
    }
}
